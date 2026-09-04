package fr.avenirsesr.portfolio.common.security.infrastructure.configuration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.avenirsesr.portfolio.common.testutils.BddLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    classes = fr.avenirsesr.portfolio.common.TestApplication.class,
    properties = {"spring.profiles.active=test", "security.authentication.filter=api-key"})
@AutoConfigureMockMvc
class APIKeySecurityConfigTest {

  private static final String PERMIT_ALL_PATHS =
      "/avenirs-portfolio-api/api-docs/**,/avenirs-portfolio-api/swagger-ui/**,/favicon.ico,/actuator/health,/photo/**,/cover/**";

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldRejectProtectedEndpointWhenApiKeyHeaderIsMissing() throws Exception {
    BddLogger.given("a protected endpoint without API key header");
    BddLogger.when("performing a GET without X-API-Key");
    BddLogger.then("it should return 401 Unauthorized");

    mockMvc.perform(get("/api/some-protected-endpoint")).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldRejectProtectedEndpointWhenApiKeyIsInvalid() throws Exception {
    BddLogger.given("a protected endpoint with invalid API key header");
    BddLogger.when("performing a GET with wrong X-API-Key");
    BddLogger.then("it should return 401 Unauthorized");

    mockMvc
        .perform(get("/api/some-protected-endpoint").header("X-API-Key", "wrong-api-key"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldAllowAccessToPermitAllPathsWithApiKeyFilterEnabled() throws Exception {
    BddLogger.given("all permit-all paths with API key filter enabled");
    BddLogger.when("performing GET on each path");
    BddLogger.then("they should be accessible according to existing rules");

    String[] paths = PERMIT_ALL_PATHS.split(",");
    for (String path : paths) {
      String cleanPath = path.trim().replace("/**", "");
      if (!cleanPath.isEmpty()) {
        if (cleanPath.contains("swagger-ui")) {
          mockMvc
              .perform(get(cleanPath))
              .andExpect(
                  result -> {
                    int s = result.getResponse().getStatus();
                    if (s != 200 && s != 302) {
                      throw new AssertionError("Status expected 200 or 302 but was: " + s);
                    }
                  });
        } else if (cleanPath.startsWith("/photo") || cleanPath.startsWith("/cover")) {
          mockMvc
              .perform(get(cleanPath))
              .andExpect(
                  result -> {
                    int s = result.getResponse().getStatus();
                    if (s != 200 && s != 404 && s != 500) {
                      throw new AssertionError("Status expected 200, 404 or 500 but was: " + s);
                    }
                  });
        } else {
          mockMvc.perform(get(cleanPath)).andExpect(status().isOk());
        }
      }
    }
  }
}
