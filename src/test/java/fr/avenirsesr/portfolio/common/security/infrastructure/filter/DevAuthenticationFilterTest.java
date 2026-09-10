package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import fr.avenirsesr.portfolio.common.security.accesscontrol.domain.model.enums.EPermission;
import fr.avenirsesr.portfolio.common.security.accesscontrol.domain.model.enums.ERole;
import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.HmacAuthenticationToken;
import fr.avenirsesr.portfolio.common.testutils.BddLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

class DevAuthenticationFilterTest {

  private static final String TEST_EPPN = "dev.user@university.com";

  private DevAuthenticationFilter filter;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain filterChain;

  @BeforeEach
  void setUp() {
    filter = new DevAuthenticationFilter();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    filterChain = mock(FilterChain.class);
    SecurityContextHolder.clearContext();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldSetAuthenticationWithEveryPermissionAndRoleWhenEppnHeaderIsPresent()
      throws ServletException, IOException {
    BddLogger.given("a request with an eppn header");
    when(request.getHeader("eppn")).thenReturn(TEST_EPPN);

    BddLogger.when("the DevAuthenticationFilter processes the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should authenticate the user with every permission and role granted");
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    assertNotNull(auth);
    assertInstanceOf(HmacAuthenticationToken.class, auth);
    assertEquals(TEST_EPPN, auth.getPrincipal());
    assertEquals(EPermission.values().length, auth.getAuthorities().size());

    HmacAuthenticationToken token = (HmacAuthenticationToken) auth;
    assertEquals(
        Arrays.stream(ERole.values()).map(Enum::name).collect(Collectors.toSet()),
        token.getRoles());

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldGrantEveryPermissionAndRoleWhenEppnHeaderIsPresent()
      throws ServletException, IOException {
    BddLogger.given("a request with the eppn header");
    when(request.getHeader("eppn")).thenReturn(TEST_EPPN);

    BddLogger.when("the DevAuthenticationFilter processes the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should grant every permission and role, as this filter is dev-only");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    assertNotNull(auth);
    assertInstanceOf(HmacAuthenticationToken.class, auth);
    assertEquals(EPermission.values().length, auth.getAuthorities().size());

    HmacAuthenticationToken token = (HmacAuthenticationToken) auth;
    assertEquals(ERole.values().length, token.getRoles().size());
  }

  @Test
  void shouldPersistSecurityContextAsRequestAttributeOnSuccessfulAuthentication()
      throws ServletException, IOException {
    BddLogger.given("a request with the eppn header");
    when(request.getHeader("eppn")).thenReturn(TEST_EPPN);

    BddLogger.when("the DevAuthenticationFilter processes the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then(
        "the SecurityContext should be saved as a request attribute, so it survives the async"
            + " redispatch used by streamed responses (StreamingResponseBody)");

    ArgumentCaptor<SecurityContext> captor = ArgumentCaptor.forClass(SecurityContext.class);

    verify(request)
        .setAttribute(
            eq(RequestAttributeSecurityContextRepository.DEFAULT_REQUEST_ATTR_NAME),
            captor.capture());

    assertNotNull(captor.getValue().getAuthentication());
    assertEquals(TEST_EPPN, captor.getValue().getAuthentication().getPrincipal());
  }

  @Test
  void shouldNotSetAuthenticationWhenEppnHeaderIsMissing() throws ServletException, IOException {
    BddLogger.given("a request without an eppn header");
    when(request.getHeader("eppn")).thenReturn(null);

    BddLogger.when("the DevAuthenticationFilter processes the request");
    filter.doFilterInternal(request, response, filterChain);

    BddLogger.then("it should not set authentication");

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldNotSetAuthenticationWhenEppnHeaderIsBlank() throws ServletException, IOException {
    BddLogger.given("a request with a blank eppn header");
    when(request.getHeader("eppn")).thenReturn(" ");

    BddLogger.when("the DevAuthenticationFilter processes the request");
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
