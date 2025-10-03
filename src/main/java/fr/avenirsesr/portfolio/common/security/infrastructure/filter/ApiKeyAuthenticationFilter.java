package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

  private static final String API_KEY_HEADER = "X-API-Key";

  private final String expectedApiKey;

  private final String permitAllPathsString;

  private List<String> permitAllPathsList;

  public ApiKeyAuthenticationFilter(String expectedApiKey, String permitAllPathsString) {
    this.expectedApiKey = expectedApiKey;
    this.permitAllPathsString = permitAllPathsString;
  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    if (permitAllPathsList == null && permitAllPathsString != null) {
      permitAllPathsList =
          Arrays.stream(permitAllPathsString.split(","))
              .map(path -> path.trim().replace("/**", ""))
              .toList();
    }

    if (permitAllPathsList == null) {
      return false;
    }

    String path = request.getRequestURI();
    return permitAllPathsList.stream().anyMatch(path::startsWith);
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String providedApiKey = request.getHeader(API_KEY_HEADER);

    if (providedApiKey == null || providedApiKey.isBlank()) {
      log.warn("API Key authentication failed: missing {} header", API_KEY_HEADER);
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("Missing API Key");
      return;
    }

    if (expectedApiKey == null || !expectedApiKey.equals(providedApiKey)) {
      log.warn("API Key authentication failed: invalid API key provided");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("Invalid API Key");
      return;
    }

    log.debug("API Key authentication successful");
    filterChain.doFilter(request, response);
  }
}
