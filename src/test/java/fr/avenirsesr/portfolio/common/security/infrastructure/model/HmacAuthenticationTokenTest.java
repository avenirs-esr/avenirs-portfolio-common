package fr.avenirsesr.portfolio.common.security.infrastructure.model;

import static org.assertj.core.api.Assertions.assertThat;

import fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.HmacAuthenticationToken;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

class HmacAuthenticationTokenTest {

  @Test
  void shouldCreateTokenWithEppn() {
    HmacAuthenticationToken token = new HmacAuthenticationToken("mockedeppn");

    assertThat(token.getPrincipal()).isEqualTo(userId);
    assertThat(token.getCredentials()).isEqualTo("");
    assertThat(token.isAuthenticated()).isTrue();
  }

  @Test
  void shouldHaveNoAuthorities() {
    UUID userId = UUID.randomUUID();
    HmacAuthenticationToken token = new HmacAuthenticationToken("mockedeppn");

    assertThat(token.getAuthorities()).isEmpty();
  }

  @Test
  void shouldImplementAuthenticationInterface() {
    HmacAuthenticationToken token = new HmacAuthenticationToken("mockedeppn");

    assertThat(token).isInstanceOf(Authentication.class);
  }
}
