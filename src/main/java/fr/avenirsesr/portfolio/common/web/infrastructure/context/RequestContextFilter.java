package fr.avenirsesr.portfolio.common.web.infrastructure.context;

import fr.avenirsesr.portfolio.common.error.domain.exception.UserNotFoundException;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.user.domain.port.output.BaseUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component("userRequestContextFilter")
@Slf4j
public class RequestContextFilter extends OncePerRequestFilter {
  private final BaseUserService userService;

  public RequestContextFilter(BaseUserService userService) {
    this.userService = userService;
  }

  @Override
  public void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      var languageCode = Optional.ofNullable(request.getHeader("Accept-Language"));
      if (languageCode.isEmpty()) {
        languageCode = Optional.of(ELanguage.FALLBACK.getCode());
      }
      ELanguage preferredLanguage = ELanguage.fromCode(languageCode.get());

      try {
        var userLoggedIn =
            Optional.ofNullable(request.getUserPrincipal())
                .map(p -> userService.getUser(UUID.fromString(p.getName())));

        RequestContext.set(new RequestData(userLoggedIn, preferredLanguage));
      } catch (UserNotFoundException e) {
        log.debug("User not found for principal {}", request.getUserPrincipal());
        RequestContext.set(new RequestData(Optional.empty(), preferredLanguage));
      }

      filterChain.doFilter(request, response);
    } finally {
      RequestContext.clear();
    }
  }
}
