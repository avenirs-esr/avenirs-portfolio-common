package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class HmacAuthenticationToken extends AbstractAuthenticationToken {

  private final String eppn;

  public HmacAuthenticationToken(String eppn) {
    this(eppn, Collections.emptyList());
  }

  public HmacAuthenticationToken(String eppn, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
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
