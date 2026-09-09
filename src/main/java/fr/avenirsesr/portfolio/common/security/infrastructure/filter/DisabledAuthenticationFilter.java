package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import fr.avenirsesr.portfolio.common.security.accesscontrol.domain.model.enums.EPermission;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    // HttpSecurity authorization is permitAll() in this mode, but @PreAuthorize method security
    // still runs, so every permission must be granted here or those checks deny anonymous calls.
    var auth =
        new UsernamePasswordAuthenticationToken(
            "internal-service",
            null,
            Arrays.stream(EPermission.values())
                .map(permission -> new SimpleGrantedAuthority(permission.authority()))
                .toList());
    SecurityContextHolder.getContext().setAuthentication(auth);

    log.warn("Authentication is completely disabled - every permission granted by default");
    filterChain.doFilter(request, response);
  }
}
