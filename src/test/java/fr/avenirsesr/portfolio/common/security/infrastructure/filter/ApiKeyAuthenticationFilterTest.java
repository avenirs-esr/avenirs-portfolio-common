package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import fr.avenirsesr.portfolio.common.testutils.BddLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class ApiKeyAuthenticationFilterTest {

  private static final String EXPECTED_API_KEY = "expected-key";
  private static final String PERMIT_ALL =
      "/avenirs-portfolio-api/swagger-ui/**,/avenirs-portfolio-api/api-docs/**,/favicon.ico,/actuator/health";

  private ApiKeyAuthenticationFilter filter;

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
    filter = new ApiKeyAuthenticationFilter(EXPECTED_API_KEY, PERMIT_ALL);
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldAuthenticateInternalRequestWithValidApiKey() throws ServletException, IOException {
    BddLogger.given("an internal request with a valid API key");
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.5");
    Mockito.when(request.getHeader("X-API-Key")).thenReturn(EXPECTED_API_KEY);

    BddLogger.when("the ApiKeyAuthenticationFilter processes the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("the request is authenticated and the chain continues");
    Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    Mockito.verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldNotFilterPublicPaths() {
    BddLogger.given("a request on any public path");
    BddLogger.when("evaluating shouldNotFilter");
    BddLogger.then("it should skip filtering for permitAll paths");

    Mockito.when(request.getRequestURI())
        .thenReturn("/avenirs-portfolio-api/swagger-ui/index.html");
    Assertions.assertTrue(filter.shouldNotFilter(request));

    Mockito.when(request.getRequestURI()).thenReturn("/avenirs-portfolio-api/api-docs");
    Assertions.assertTrue(filter.shouldNotFilter(request));

    Mockito.when(request.getRequestURI()).thenReturn("/favicon.ico");
    Assertions.assertTrue(filter.shouldNotFilter(request));

    Mockito.when(request.getRequestURI()).thenReturn("/actuator/health");
    Assertions.assertTrue(filter.shouldNotFilter(request));

    Mockito.when(request.getRequestURI()).thenReturn("/protected/resource");
    Assertions.assertFalse(filter.shouldNotFilter(request));
  }

  @Test
  void shouldPassThroughOnExternalRequest() throws ServletException, IOException {
    BddLogger.given("an external request (no internal network)");
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.10");

    BddLogger.when("the ApiKeyAuthenticationFilter processes the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should pass through without setting authentication");
    Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    Mockito.verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldReturn401OnInternalRequestWithMissingApiKey() throws ServletException, IOException {
    BddLogger.given("an internal request missing the API key header");
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.20");
    Mockito.when(request.getHeader("X-API-Key")).thenReturn(null);

    BddLogger.when("the ApiKeyAuthenticationFilter processes the request");
    // mock response writer to avoid NPE when filter writes error message
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
    Mockito.when(response.getWriter()).thenReturn(pw);
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should return 401 and not continue the chain");
    Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
    Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
    Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void shouldReturn401OnInternalRequestWithInvalidApiKey() throws ServletException, IOException {
    BddLogger.given("an internal request with an invalid API key");
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("172.16.0.10");
    Mockito.when(request.getHeader("X-API-Key")).thenReturn("wrong-key");

    BddLogger.when("the ApiKeyAuthenticationFilter processes the request");
    // mock response writer to avoid NPE when filter writes error message
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
    Mockito.when(response.getWriter()).thenReturn(pw);
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should return 401 and not continue the chain");
    Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
    Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
    Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void shouldReturn401OnInternalRequestWithBlankApiKey() throws ServletException, IOException {
    BddLogger.given("an internal request with a blank API key header");
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("10.1.2.3");
    Mockito.when(request.getHeader("X-API-Key")).thenReturn("   ");

    BddLogger.when("the ApiKeyAuthenticationFilter processes the request");
    // mock response writer to avoid NPE when filter writes error message
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
    Mockito.when(response.getWriter()).thenReturn(pw);
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should return 401 and not continue the chain");
    Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
    Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
    Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
  }
}
