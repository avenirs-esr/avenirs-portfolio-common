package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class HmacAuthenticationToken extends AbstractAuthenticationToken {

  private final String eppn;
  private final Set<String> roles;

  public HmacAuthenticationToken(String eppn) {
    this(eppn, Collections.emptyList(), Collections.emptySet());
  }

  public HmacAuthenticationToken(String eppn, Collection<? extends GrantedAuthority> authorities) {
    this(eppn, authorities, Collections.emptySet());
  }

  public HmacAuthenticationToken(
      String eppn, Collection<? extends GrantedAuthority> authorities, Set<String> roles) {
    super(authorities);
    this.eppn = eppn;
    this.roles = roles == null ? Collections.emptySet() : roles;
    setAuthenticated(true);
  }

  public Set<String> getRoles() {
    return roles;
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
