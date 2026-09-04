package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.avenirsesr.portfolio.common.testutils.BddLogger;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserSecurityPayloadTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  void shouldCreateUserPayload() {
    BddLogger.given("user payload properties");
    String sub = UUID.randomUUID().toString();
    Instant iat = Instant.now();
    Instant exp = iat.plusSeconds(3600);

    BddLogger.when("creating an UserPayload");
    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(sub);
    userSecurityPayload.setIat(iat);
    userSecurityPayload.setExp(exp);

    BddLogger.then("it should create the UserPayload");
    assertThat(userSecurityPayload.getSub()).isEqualTo(sub);
    assertThat(userSecurityPayload.getIat()).isEqualTo(iat);
    assertThat(userSecurityPayload.getExp()).isEqualTo(exp);
  }

  @Test
  void shouldSerializeAndDeserializeUserPayload() throws Exception {
    BddLogger.given("an UserPayload");
    String sub = UUID.randomUUID().toString();
    Instant iat = Instant.now();
    Instant exp = iat.plusSeconds(3600);

    UserSecurityPayload userSecurityPayload = new UserSecurityPayload();
    userSecurityPayload.setSub(sub);
    userSecurityPayload.setIat(iat);
    userSecurityPayload.setExp(exp);

    String json = objectMapper.writeValueAsString(userSecurityPayload);

    BddLogger.when("deserializing the UserPayload");
    UserSecurityPayload deserializedUserSecurityPayload =
        objectMapper.readValue(json, UserSecurityPayload.class);

    BddLogger.then("it should allow access to user payload properties");
    assertThat(deserializedUserSecurityPayload.getSub()).isEqualTo(sub);
    assertThat(deserializedUserSecurityPayload.getIat()).isEqualTo(iat);
    assertThat(deserializedUserSecurityPayload.getExp()).isEqualTo(exp);
  }
}
