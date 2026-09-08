package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.avenirsesr.portfolio.common.security.domain.exception.UserNotAuthorizedException;
import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.AvenirsSecurityHeaders;
import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.HmacAuthenticationToken;
import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.UserSecurityPayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class HmacAuthenticationFilter extends OncePerRequestFilter {
  private final SecurityContextRepository securityContextRepository;
  private final String permitAllPathsString;
  private final String secret;

  private List<String> permitAllPathsList;

  public HmacAuthenticationFilter(String permitAllPathsString, String secret) {
    this.securityContextRepository = new RequestAttributeSecurityContextRepository();
    this.permitAllPathsString = permitAllPathsString;
    this.secret = secret;
  }

  @Override
  public void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String signature = request.getHeader(AvenirsSecurityHeaders.CONTEXT_SIGNATURE);
    String payload = request.getHeader(AvenirsSecurityHeaders.SIGNED_CONTEXT);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    UserSecurityPayload userSecurityPayload =
        objectMapper.readValue(payload, UserSecurityPayload.class);

    if (!payloadIsValid(userSecurityPayload)) {
      UserNotAuthorizedException exception = new UserNotAuthorizedException();
      log.error("Invalid HMAC authentication attempt. Payload is expired or invalid. {}", payload);
      throw exception;
    }

    if (signature != null && verifySignature(payload, signature, secret)) {
      String stub = userSecurityPayload.getSub();
      List<GrantedAuthority> grantedAuthority =
          Optional.ofNullable(userSecurityPayload.getAuthorities())
              .orElse(Collections.emptySet())
              .stream()
              .map(SimpleGrantedAuthority::new)
              .map(GrantedAuthority.class::cast)
              .toList();
      Set<String> roles =
          Optional.ofNullable(userSecurityPayload.getRoles()).orElse(Collections.emptySet());
      Authentication auth = new HmacAuthenticationToken(stub, grantedAuthority, roles);
      SecurityContext context = SecurityContextHolder.getContext();

      // Persists the context as a request attribute so it survives the async thread hop used by
      // streamed responses. Removing it breaks nothing synchronously but silently reintroduces 401s
      // on any endpoint returning StreamingResponseBody.
      context.setAuthentication(auth);
      securityContextRepository.saveContext(context, request, response);

      log.info(
          "HMAC authentication succeeded for user [{}] with authorities {} and roles {}",
          stub,
          grantedAuthority,
          roles);

      filterChain.doFilter(request, response);
    } else {
      UserNotAuthorizedException exception = new UserNotAuthorizedException();
      log.error("Invalid HMAC authentication attempt.{}", String.valueOf(exception));
      throw exception;
    }
  }

  @Override
  public boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      return true;
    }

    if (permitAllPathsString == null) {
      return false;
    }

    if (permitAllPathsList == null) {
      String[] permitAllPathsParts = permitAllPathsString.split(",");
      permitAllPathsList =
          Arrays.stream(permitAllPathsParts).map(path -> path.trim().replace("/**", "")).toList();
    }

    String path = request.getRequestURI();
    return permitAllPathsList.stream().anyMatch(path::startsWith);
  }

  private boolean payloadIsValid(UserSecurityPayload payload) {
    return payload != null && payload.getExp().isAfter(Instant.now());
  }

  private boolean verifySignature(String payload, String signature, String secretKey) {
    try {
      Mac sha256Hmac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec =
          new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

      sha256Hmac.init(secretKeySpec);

      byte[] signedBytes = sha256Hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
      String computedSignature = Base64.getEncoder().encodeToString(signedBytes);

      return computedSignature.equals(signature);
    } catch (Exception e) {
      return false;
    }
  }
}
