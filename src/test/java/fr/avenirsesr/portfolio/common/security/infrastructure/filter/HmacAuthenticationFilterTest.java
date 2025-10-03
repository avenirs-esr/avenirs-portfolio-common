package fr.avenirsesr.portfolio.common.security.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.UserSecurityPayload;
import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.enums.ESecurityKeys;
import fr.avenirsesr.portfolio.common.testutils.BddLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class HmacAuthenticationFilterTest {

  @Spy @InjectMocks private HmacAuthenticationFilter filter;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  private ObjectMapper objectMapper;
  private static final String TEST_KEY = "TEST_KEY";
  private static final String TEST_SECRET = "test-secret";
  private static final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    ReflectionTestUtils.setField(
        filter,
        "permitAllPathsString",
        "/avenirs-portfolio-api/swagger-ui/**,/avenirs-portfolio-api/api-docs/**,/favicon.ico,/actuator/health");
  }

  @Test
  void shouldNotFilterPublicPaths() throws ServletException {
    BddLogger.given("a request on any public path");
    BddLogger.when("performing the request");
    BddLogger.then("it should not filter");
    Mockito.when(request.getRequestURI())
        .thenReturn("/avenirs-portfolio-api/swagger-ui/index.html");
    Assertions.assertTrue(filter.shouldNotFilter(request));

    Mockito.when(request.getRequestURI()).thenReturn("/avenirs-portfolio-api/api-docs");
    Assertions.assertTrue(filter.shouldNotFilter(request));

    Mockito.when(request.getRequestURI()).thenReturn("/favicon.ico");
    Assertions.assertTrue(filter.shouldNotFilter(request));

    Mockito.when(request.getRequestURI()).thenReturn("/actuator/health");
    Assertions.assertTrue(filter.shouldNotFilter(request));

    Mockito.when(request.getRequestURI()).thenReturn("/api/protected");
    Assertions.assertFalse(filter.shouldNotFilter(request));
  }

  @Test
  void shouldNotFilterPublicPath() {
    BddLogger.given("a request on any public path");
    BddLogger.when("performing the request");
    BddLogger.then("it should not filter");
    try {
      ReflectionTestUtils.setField(filter, "permitAllPathsString", "/public/path,/another/path");
      ReflectionTestUtils.setField(
          filter, "permitAllPathsList", java.util.Arrays.asList("/public/path", "/another/path"));

      Mockito.when(request.getRequestURI()).thenReturn("/public/path/resource");

      Assertions.assertTrue(filter.shouldNotFilter(request));
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldFilterNonPublicPath() {
    BddLogger.given("a request on any non public path");
    BddLogger.when("performing the request");
    BddLogger.then("it should filter");
    try {
      ReflectionTestUtils.setField(filter, "permitAllPathsString", "/public/path,/another/path");
      ReflectionTestUtils.setField(
          filter, "permitAllPathsList", java.util.Arrays.asList("/public/path", "/another/path"));

      Mockito.when(request.getRequestURI()).thenReturn("/protected/resource");

      Assertions.assertFalse(filter.shouldNotFilter(request));
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldAuthenticateWithValidSignature() throws ServletException, IOException {
    BddLogger.given("a request");
    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(TEST_UUID);
    userSecurityPayload.setExp(Instant.now().plusSeconds(3600));

    String payload = objectMapper.writeValueAsString(userSecurityPayload);
    String signature = generateHmacSignature(payload);

    Mockito.when(request.getHeader("X-Context-Kid")).thenReturn(TEST_KEY);
    Mockito.when(request.getHeader("X-Context-Signature")).thenReturn(signature);
    Mockito.when(request.getHeader("X-Signed-Context")).thenReturn(payload);

    BddLogger.when("performing the request with valid signature");
    BddLogger.then("it should authenticate");
    try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
      mockedStatic.when(() -> ESecurityKeys.getSecretByKey(TEST_KEY)).thenReturn(TEST_SECRET);

      filter.doFilterInternal(request, response, filterChain);

      Mockito.verify(filterChain).doFilter(request, response);
      Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
      Assertions.assertEquals(
          TEST_UUID, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
  }

  @Test
  void shouldRejectExpiredPayload() {
    BddLogger.given("a request");
    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(TEST_UUID);
    userSecurityPayload.setExp(Instant.now().minusSeconds(3600));

    BddLogger.when("performing the request with expired payload");
    BddLogger.then("it should reject it");
    try {
      String payload = objectMapper.writeValueAsString(userSecurityPayload);
      String signature = generateHmacSignature(payload);

      Mockito.when(request.getHeader("X-Context-Kid")).thenReturn(TEST_KEY);
      Mockito.when(request.getHeader("X-Context-Signature")).thenReturn(signature);
      Mockito.when(request.getHeader("X-Signed-Context")).thenReturn(payload);

      try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
        mockedStatic.when(() -> ESecurityKeys.getSecretByKey(TEST_KEY)).thenReturn(TEST_SECRET);

        Assertions.assertThrows(
            BusinessException.class, () -> filter.doFilterInternal(request, response, filterChain));
      }
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldRejectInvalidSignature() {
    BddLogger.given("a request");
    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(TEST_UUID);
    userSecurityPayload.setExp(Instant.now().plusSeconds(3600));

    BddLogger.when("performing the request with invalid signature");
    BddLogger.then("it should reject it");
    try {
      String payload = objectMapper.writeValueAsString(userSecurityPayload);
      String invalidSignature = "invalid-signature";

      Mockito.when(request.getHeader("X-Context-Kid")).thenReturn(TEST_KEY);
      Mockito.when(request.getHeader("X-Context-Signature")).thenReturn(invalidSignature);
      Mockito.when(request.getHeader("X-Signed-Context")).thenReturn(payload);

      try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
        mockedStatic.when(() -> ESecurityKeys.getSecretByKey(TEST_KEY)).thenReturn(TEST_SECRET);

        Assertions.assertThrows(
            BusinessException.class, () -> filter.doFilterInternal(request, response, filterChain));
      }
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldHandleExceptionDuringSignatureVerification() {
    BddLogger.given("a request");
    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(TEST_UUID);
    userSecurityPayload.setExp(Instant.now().plusSeconds(3600));

    BddLogger.when("performing the request and an exception occurs during signature verification");
    BddLogger.then("it should handle it");
    try {
      String payload = objectMapper.writeValueAsString(userSecurityPayload);

      Mockito.when(request.getHeader("X-Context-Kid")).thenReturn(TEST_KEY);
      Mockito.when(request.getHeader("X-Context-Signature")).thenReturn("valid-looking-signature");
      Mockito.when(request.getHeader("X-Signed-Context")).thenReturn(payload);

      try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
        mockedStatic.when(() -> ESecurityKeys.getSecretByKey(TEST_KEY)).thenReturn(null);

        Assertions.assertThrows(
            BusinessException.class, () -> filter.doFilterInternal(request, response, filterChain));
      }
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldRejectNullPayload() {
    BddLogger.given("a request");
    BddLogger.when("performing the request with null payload");
    BddLogger.then("it should reject it");
    try {
      Mockito.when(request.getHeader("X-Context-Kid")).thenReturn(TEST_KEY);
      Mockito.when(request.getHeader("X-Context-Signature")).thenReturn("some-signature");
      Mockito.when(request.getHeader("X-Signed-Context")).thenReturn("invalid-json");

      try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
        mockedStatic.when(() -> ESecurityKeys.getSecretByKey(TEST_KEY)).thenReturn(TEST_SECRET);

        Assertions.assertThrows(
            Exception.class, () -> filter.doFilterInternal(request, response, filterChain));
      }
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldRejectNullPayloadString() {
    BddLogger.given("a request");
    BddLogger.when("performing the request with null payload string");
    BddLogger.then("it should reject it");
    try {
      Mockito.when(request.getHeader("X-Context-Kid")).thenReturn(TEST_KEY);
      Mockito.when(request.getHeader("X-Context-Signature")).thenReturn("some-signature");
      Mockito.when(request.getHeader("X-Signed-Context")).thenReturn(null);

      try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
        mockedStatic.when(() -> ESecurityKeys.getSecretByKey(TEST_KEY)).thenReturn(TEST_SECRET);

        Assertions.assertThrows(
            Exception.class, () -> filter.doFilterInternal(request, response, filterChain));
      }
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldRejectMissingSecretKey() {
    BddLogger.given("a request");
    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(TEST_UUID);
    userSecurityPayload.setExp(Instant.now().plusSeconds(3600));

    BddLogger.when("performing the request without secret key");
    BddLogger.then("it should reject it");
    try {
      String payload = objectMapper.writeValueAsString(userSecurityPayload);
      String signature = generateHmacSignature(payload);

      Mockito.when(request.getHeader("X-Context-Kid")).thenReturn("UNKNOWN_KEY");
      Mockito.when(request.getHeader("X-Context-Signature")).thenReturn(signature);
      Mockito.when(request.getHeader("X-Signed-Context")).thenReturn(payload);

      try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
        mockedStatic.when(() -> ESecurityKeys.getSecretByKey("UNKNOWN_KEY")).thenReturn(null);

        Assertions.assertThrows(
            BusinessException.class, () -> filter.doFilterInternal(request, response, filterChain));
      }
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldRejectMissingSignature() {
    BddLogger.given("a request");
    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(TEST_UUID);
    userSecurityPayload.setExp(Instant.now().plusSeconds(3600));

    BddLogger.when("performing the request without signature");
    BddLogger.then("it should reject it");
    try {
      String payload = objectMapper.writeValueAsString(userSecurityPayload);

      Mockito.when(request.getHeader("X-Context-Kid")).thenReturn(TEST_KEY);
      Mockito.when(request.getHeader("X-Context-Signature")).thenReturn(null);
      Mockito.when(request.getHeader("X-Signed-Context")).thenReturn(payload);

      try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
        mockedStatic.when(() -> ESecurityKeys.getSecretByKey(TEST_KEY)).thenReturn(TEST_SECRET);

        Assertions.assertThrows(
            BusinessException.class, () -> filter.doFilterInternal(request, response, filterChain));
      }
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldRejectPayloadWithNullExpiration() {
    BddLogger.given("a request");
    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(TEST_UUID);
    userSecurityPayload.setExp(null);

    BddLogger.when("performing the request without expiration");
    BddLogger.then("it should reject it");
    try {
      String payload = objectMapper.writeValueAsString(userSecurityPayload);
      String signature = generateHmacSignature(payload);

      Mockito.when(request.getHeader("X-Context-Kid")).thenReturn(TEST_KEY);
      Mockito.when(request.getHeader("X-Context-Signature")).thenReturn(signature);
      Mockito.when(request.getHeader("X-Signed-Context")).thenReturn(payload);

      try (var mockedStatic = Mockito.mockStatic(ESecurityKeys.class)) {
        mockedStatic.when(() -> ESecurityKeys.getSecretByKey(TEST_KEY)).thenReturn(TEST_SECRET);

        Assertions.assertThrows(
            Exception.class, () -> filter.doFilterInternal(request, response, filterChain));
      }
    } catch (Exception e) {
      Assertions.fail("Test setup failed: " + e.getMessage());
    }
  }

  private String generateHmacSignature(String payload) {
    try {
      Mac sha256Hmac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec =
          new SecretKeySpec(
              HmacAuthenticationFilterTest.TEST_SECRET.getBytes(StandardCharsets.UTF_8),
              "HmacSHA256");
      sha256Hmac.init(secretKeySpec);

      byte[] signedBytes = sha256Hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(signedBytes);
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate HMAC signature", e);
    }
  }
}
