package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model;

import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class HmacAuthenticationToken extends AbstractAuthenticationToken {

  private final String eppn;

  public HmacAuthenticationToken(String eppn) {
    super(Collections.emptyList());
    this.eppn = eppn;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return "";
  }

  @Override
  public Object getPrincipal() {
    return eppn;
  }
}
