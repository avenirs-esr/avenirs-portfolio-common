package fr.avenirsesr.portfolio.common.web.infrastructure.context;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.avenirsesr.portfolio.common.data.domain.model.User;
import fr.avenirsesr.portfolio.common.error.application.adapter.response.ErrorResponse;
import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.user.domain.port.output.BaseUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component("userRequestContextFilter")
@Slf4j
public class RequestContextFilter extends OncePerRequestFilter {
  private static final String INTERCEPTOR_KEY = RequestContextFilter.class.getName();

  private final BaseUserService userService;
  private final ObjectMapper objectMapper;

  public RequestContextFilter(BaseUserService userService, ObjectMapper objectMapper) {
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // This filter's own try/finally only covers the synchronous path. For requests handled
    // asynchronously (e.g. StreamingResponseBody), filterChain.doFilter() returns before the real
    // work runs, so RequestContext must be re-armed on the worker thread.
    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
    CallableProcessingInterceptor interceptor = new RequestContextInterceptor();
    asyncManager.registerCallableInterceptor(INTERCEPTOR_KEY, interceptor);

    try {
      try {
        Principal principal = request.getUserPrincipal();

        if (principal != null) {
          String name = principal.getName();
          User user = userService.getUserByEppn(name);

          String languageCode = request.getHeader("Accept-Language");
          ELanguage preferredLanguage =
              languageCode == null || languageCode.isEmpty()
                  ? ELanguage.FALLBACK
                  : ELanguage.fromCode(languageCode);

          RequestData data = new RequestData(Optional.ofNullable(user), preferredLanguage);
          RequestContext.set(data);
        }
      } catch (BusinessException ex) {
        log.warn("Rejected request while building request context: {}", ex.getErrorCode());
        writeErrorResponse(response, ex.getErrorCode());
        return;
      } catch (Exception ex) {
        log.error("Unexpected error while building request context", ex);
        writeErrorResponse(response, EErrorCode.UNEXPECTED_ERROR);
        return;
      }

      filterChain.doFilter(request, response);
    } finally {
      RequestContext.clear();
    }
  }

  private void writeErrorResponse(HttpServletResponse response, EErrorCode errorCode)
      throws IOException, StreamWriteException, DatabindException {
    response.setStatus(errorCode.getHttpStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(
        response.getWriter(), new ErrorResponse(errorCode.name(), errorCode.getMessage()));
  }
}
