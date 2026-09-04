package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.avenirsesr.portfolio.common.security.accesscontrol.domain.model.enums.EPermission;
import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.HmacAuthenticationToken;
import fr.avenirsesr.portfolio.common.testutils.BddLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class DevAuthenticationFilterTest {

  private DevAuthenticationFilter filter;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain filterChain;

  private static final String TEST_EPPN = "mockedeppn@avenirs-esr.fr";

  @BeforeEach
  void setUp() {
    filter = new DevAuthenticationFilter();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    filterChain = mock(FilterChain.class);
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldSetAuthenticationWithEveryPermissionWhenEppnHeaderIsPresent()
      throws ServletException, IOException {
    BddLogger.given("a request with an eppn header");
    when(request.getHeader("eppn")).thenReturn(TEST_EPPN);

    BddLogger.when("filtering the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should authenticate the user with every permission granted");
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    assertNotNull(auth);
    assertTrue(auth instanceof HmacAuthenticationToken);
    assertEquals(TEST_EPPN, auth.getPrincipal());
    assertEquals(EPermission.values().length, auth.getAuthorities().size());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldNotSetAuthenticationWhenEppnHeaderIsMissing() throws ServletException, IOException {
    BddLogger.given("a request without an eppn header");
    when(request.getHeader("eppn")).thenReturn(null);

    BddLogger.when("filtering the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should not set authentication");
    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldNotSetAuthenticationWhenEppnHeaderIsBlank() throws ServletException, IOException {
    BddLogger.given("a request with a blank eppn header");
    when(request.getHeader("eppn")).thenReturn(" ");

    BddLogger.when("filtering the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should not set authentication");
    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldAlwaysFilter() {
    BddLogger.given("any request");
    BddLogger.when("performing the request");
    BddLogger.then("it should filter");
    assertFalse(filter.shouldNotFilter(request));
  }
}
