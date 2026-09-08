package fr.avenirsesr.portfolio.common.web.infrastructure.context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.avenirsesr.portfolio.common.data.domain.model.User;
import fr.avenirsesr.portfolio.common.error.domain.exception.UserNotFoundException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.testutils.BddLogger;
import fr.avenirsesr.portfolio.common.user.domain.exceptions.ExternalUserNotFoundException;
import fr.avenirsesr.portfolio.common.user.domain.port.output.BaseUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;

class RequestContextFilterTest {

  private final BaseUserService userService = mock(BaseUserService.class);
  private final RequestContextFilter filter =
      new RequestContextFilter(userService, new ObjectMapper());

  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final FilterChain filterChain = mock(FilterChain.class);

  @BeforeEach
  void setup() {
    RequestContext.clear();
  }

  @AfterEach
  void cleanup() {
    RequestContext.clear();
  }

  @Test
  void shouldSetPreferredLanguageWhenHeaderPresent() throws Exception {
    BddLogger.given("a request");
    BddLogger.when("Accept-Language header is present");

    when(request.getUserPrincipal()).thenReturn(() -> "test.test@university.com");
    when(userService.getUserByEppn("test.test@university.com")).thenReturn(mock(User.class));
    when(request.getHeader("Accept-Language")).thenReturn(ELanguage.FALLBACK.getCode());

    BddLogger.then("it should set preferred language");
    doAnswer(
            invocation -> {
              RequestData contextData = RequestContext.get();
              assertNotNull(contextData);
              assertEquals(ELanguage.FRENCH, contextData.preferredLanguage());
              return null;
            })
        .when(filterChain)
        .doFilter(any(), any());

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldUseFallbackLanguageWhenHeaderMissing() throws Exception {
    BddLogger.given("a request");
    BddLogger.when("Accept-Language header is missing");

    when(request.getUserPrincipal()).thenReturn(() -> "test.test@university.com");
    when(userService.getUserByEppn("test.test@university.com")).thenReturn(mock(User.class));
    when(request.getHeader("Accept-Language")).thenReturn(null);

    BddLogger.then("it should use fallback language");
    doAnswer(
            invocation -> {
              RequestData contextData = RequestContext.get();
              assertNotNull(contextData);
              assertEquals(ELanguage.FALLBACK, contextData.preferredLanguage());
              return null;
            })
        .when(filterChain)
        .doFilter(any(), any());

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldClearRequestContextAfterFilterChain() throws Exception {
    BddLogger.given("a request");
    when(request.getHeader("Accept-Language")).thenReturn("en");

    BddLogger.when("filter chain has ended");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should clear request context");
    assertNull(RequestContext.get());
  }

  @Test
  void shouldPropagateRequestContextAcrossAsyncDispatch() throws Exception {
    BddLogger.given("a request handled asynchronously");

    when(request.getUserPrincipal()).thenReturn(() -> "test.test@university.com");
    when(userService.getUserByEppn("test.test@university.com")).thenReturn(mock(User.class));
    when(request.getHeader("Accept-Language")).thenReturn(ELanguage.FALLBACK.getCode());

    WebAsyncManager asyncManager = mock(WebAsyncManager.class);
    ArgumentCaptor<CallableProcessingInterceptor> interceptorCaptor =
        ArgumentCaptor.forClass(CallableProcessingInterceptor.class);
    AtomicReference<Throwable> asyncThreadFailure = new AtomicReference<>();

    try (MockedStatic<WebAsyncUtils> webAsyncUtils = mockStatic(WebAsyncUtils.class)) {
      webAsyncUtils.when(() -> WebAsyncUtils.getAsyncManager(request)).thenReturn(asyncManager);

      BddLogger.when(
          "the filter chain runs (simulating the original thread, before the async handoff)");
      doAnswer(
              invocation -> {
                verify(asyncManager)
                    .registerCallableInterceptor(any(), interceptorCaptor.capture());
                CallableProcessingInterceptor interceptor = interceptorCaptor.getValue();

                NativeWebRequest nativeWebRequest = mock(NativeWebRequest.class);

                @SuppressWarnings("unchecked")
                Callable<Object> task = mock(Callable.class);

                interceptor.beforeConcurrentHandling(nativeWebRequest, task);

                Thread asyncThread =
                    new Thread(
                        () -> {
                          try {
                            interceptor.preProcess(nativeWebRequest, task);
                            RequestData restored = RequestContext.get();
                            assertNotNull(
                                restored,
                                "RequestContext devrait être restauré sur le thread async");
                            assertEquals(ELanguage.FALLBACK, restored.preferredLanguage());

                            interceptor.afterCompletion(nativeWebRequest, task);
                            assertNull(
                                RequestContext.get(),
                                "RequestContext devrait être nettoyé après afterCompletion");
                          } catch (Throwable t) {
                            asyncThreadFailure.set(t);
                          }
                        });
                asyncThread.start();
                asyncThread.join();

                return null;
              })
          .when(filterChain)
          .doFilter(any(), any());

      filter.doFilterInternal(request, response, filterChain);
    }

    BddLogger.then("the async thread should see the context propagated, then cleared");
    if (asyncThreadFailure.get() != null) {
      throw new AssertionError("Propagation failed on the async thread", asyncThreadFailure.get());
    }
  }

  @Test
  void shouldReturnNotFoundAndSkipFilterChainWhenUserResolutionFails() throws Exception {
    BddLogger.given("a principal that userService cannot resolve");
    when(request.getUserPrincipal()).thenReturn((Principal) () -> "unknown@university.com");
    when(userService.getUserByEppn("unknown@university.com"))
        .thenThrow(new UserNotFoundException());

    StringWriter body = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(body));

    BddLogger.when("the filter runs");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then(
        "it should write a 404 carrying USER_NOT_FOUND, never reach the controller, and still clear"
            + " the context");
    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    verify(filterChain, never()).doFilter(any(), any());
    assertTrue(body.toString().contains("USER_NOT_FOUND"));
    assertNull(RequestContext.get());
  }

  @Test
  void shouldPreserveExternalUserNotFoundErrorCodeDistinctFromGenericUserNotFound()
      throws Exception {
    BddLogger.given("an eppn genuinely unknown to the external directory");
    when(request.getUserPrincipal()).thenReturn((Principal) () -> "ghost@university.com");
    when(userService.getUserByEppn("ghost@university.com"))
        .thenThrow(new ExternalUserNotFoundException());

    StringWriter body = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(body));

    BddLogger.when("the filter runs");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("the response should still be a 404, but not tagged as the generic user code");
    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    assertFalse(body.toString().contains("\"USER_NOT_FOUND\""));
    assertNull(RequestContext.get());
  }

  @Test
  void shouldReturnInternalErrorAndSkipFilterChainWhenUnexpectedExceptionOccurs() throws Exception {
    BddLogger.given("user resolution throws something that is not a BusinessException");
    when(request.getUserPrincipal()).thenReturn((Principal) () -> "test.test@university.com");
    when(userService.getUserByEppn("test.test@university.com"))
        .thenThrow(new IllegalStateException("db connection reset"));

    StringWriter body = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(body));

    BddLogger.when("the filter runs");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then(
        "it should fall back to a generic internal error, never reach the controller, still clear"
            + " the context, and not leak the raw exception message");
    verify(response).setStatus(EErrorCode.UNEXPECTED_ERROR.getHttpStatus().value());
    verify(filterChain, never()).doFilter(any(), any());
    assertTrue(body.toString().contains("UNEXPECTED_ERROR"));
    assertFalse(body.toString().contains("db connection reset"));
    assertNull(RequestContext.get());
  }

  @Test
  void shouldPropagateErrorAndSkipFilterChainWhenUnrecoverableErrorOccurs() throws Exception {
    BddLogger.given("user resolution throws an Error rather than an Exception");
    when(request.getUserPrincipal()).thenReturn((Principal) () -> "test.test@university.com");
    when(userService.getUserByEppn("test.test@university.com"))
        .thenThrow(new AssertionError("simulated unrecoverable failure"));

    BddLogger.when("the filter runs");
    BddLogger.then(
        "it should propagate the Error untouched, never write a response, never reach the"
            + " controller, and still clear the context");

    assertThrows(
        AssertionError.class, () -> filter.doFilterInternal(request, response, filterChain));

    verifyNoInteractions(response);
    verify(filterChain, never()).doFilter(any(), any());
    assertNull(RequestContext.get());
  }
}
