package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class DisabledAuthenticationFilter extends OncePerRequestFilter {

  public DisabledAuthenticationFilter() {}

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    return false;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    log.warn("Authentication is completely disabled - no authentication performed");
    filterChain.doFilter(request, response);
  }
}
