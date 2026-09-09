package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import fr.avenirsesr.portfolio.common.security.accesscontrol.domain.model.enums.EPermission;
import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.HmacAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class DevAuthenticationFilter extends OncePerRequestFilter {
  private final SecurityContextRepository securityContextRepository;

  public DevAuthenticationFilter() {
    this.securityContextRepository = new RequestAttributeSecurityContextRepository();
  }

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
    String devUser = request.getHeader("eppn");

    if (devUser != null && !devUser.isBlank()) {
      List<SimpleGrantedAuthority> authorities =
          Arrays.stream(EPermission.values())
              .map(permission -> new SimpleGrantedAuthority(permission.authority()))
              .toList();
      Authentication auth = new HmacAuthenticationToken(devUser, authorities);
      SecurityContext context = SecurityContextHolder.getContext();

      // Persists the context as a request attribute so it survives the async thread hop used by
      // streamed responses. Removing it breaks nothing synchronously but silently reintroduces 401s
      // on any endpoint returning StreamingResponseBody.
      context.setAuthentication(auth);
      securityContextRepository.saveContext(context, request, response);

      log.debug("Dev authentication enabled for user: {} with every permission granted", devUser);
    }

    filterChain.doFilter(request, response);
  }
}
