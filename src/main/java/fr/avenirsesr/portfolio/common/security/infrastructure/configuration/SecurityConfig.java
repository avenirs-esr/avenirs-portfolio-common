package fr.avenirsesr.portfolio.common.security.infrastructure.configuration;

import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.enums.ESecurityFilter;
import fr.avenirsesr.portfolio.common.security.infrastructure.filter.ApiKeyAuthenticationFilter;
import fr.avenirsesr.portfolio.common.security.infrastructure.filter.DevAuthenticationFilter;
import fr.avenirsesr.portfolio.common.security.infrastructure.filter.DisabledAuthenticationFilter;
import fr.avenirsesr.portfolio.common.security.infrastructure.filter.HmacAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Value("${security.authentication.filter}")
  private String securityFilter;

  @Value("${security.authentication.api-key:default-api-key}")
  private String expectedApiKey;

  @Value("${security.hmac.secret}")
  private String hmacSecret;

  @Value("${security.permit-all-paths}")
  private String[] permitAllPaths;

  @Value("${cors.allowed-origins}")
  private String allowedOriginsString;

  @Value("${cors.allowed-methods}")
  private String allowedMethodsString;

  @Value("${cors.allowed-headers}")
  private String allowedHeadersString;

  @Value("${cors.allow-credentials}")
  private boolean allowCredentials;

  public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
    this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    boolean isSecurityEnabled = true;
    switch (ESecurityFilter.fromValue(securityFilter)) {
      case HMAC:
        {
          log.info(
              "Security enabled (signed header protection) using filter {}",
              HmacAuthenticationFilter.class.getSimpleName());
          http.addFilterBefore(
              new HmacAuthenticationFilter(String.join(",", permitAllPaths), hmacSecret),
              UsernamePasswordAuthenticationFilter.class);
        }
        break;
      case DEV:
        {
          log.warn(
              "Security is disabled, dev mode, using {}, do not use in production",
              DevAuthenticationFilter.class.getSimpleName());
          http.addFilterBefore(
              new DevAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }
        break;
      case API_KEY:
        {
          log.info(
              "Security is enabled (api key protection), using {}",
              ApiKeyAuthenticationFilter.class.getSimpleName());
          http.addFilterBefore(
              new ApiKeyAuthenticationFilter(expectedApiKey, String.join(",", permitAllPaths)),
              UsernamePasswordAuthenticationFilter.class);
        }
        break;

      case API_KEY_OR_HMAC:
        {
          log.info(
              "Security is enabled (api key protection or signed header protection), using {} and {}",
              ApiKeyAuthenticationFilter.class.getSimpleName(),
              HmacAuthenticationFilter.class.getSimpleName());
          http.addFilterBefore(
              new ApiKeyAuthenticationFilter(expectedApiKey, String.join(",", permitAllPaths)),
              UsernamePasswordAuthenticationFilter.class);
          http.addFilterBefore(
              new HmacAuthenticationFilter(String.join(",", permitAllPaths), hmacSecret),
              UsernamePasswordAuthenticationFilter.class);
        }
        break;
      case DISABLED:
        {
          isSecurityEnabled = false;
          log.warn(
              "Security is disabled, using {}, do not use in production",
              DisabledAuthenticationFilter.class.getSimpleName());
          http.addFilterBefore(
              new DisabledAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }
        break;
      default:
        {
          log.info(
              "Security enabled by default (signed header protection) using filter {}",
              HmacAuthenticationFilter.class.getSimpleName());
          http.addFilterBefore(
              new HmacAuthenticationFilter(String.join(",", permitAllPaths), hmacSecret),
              UsernamePasswordAuthenticationFilter.class);
        }
    }
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            isSecurityEnabled
                ? authz ->
                    authz.requestMatchers(permitAllPaths).permitAll().anyRequest().authenticated()
                : authz -> authz.anyRequest().permitAll())
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint));

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    for (String origin : allowedOriginsString.split(",")) {
      configuration.addAllowedOrigin(origin.trim());
    }

    for (String method : allowedMethodsString.split(",")) {
      configuration.addAllowedMethod(method.trim());
    }

    for (String header : allowedHeadersString.split(",")) {
      configuration.addAllowedHeader(header.trim());
    }

    configuration.setAllowCredentials(allowCredentials);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
