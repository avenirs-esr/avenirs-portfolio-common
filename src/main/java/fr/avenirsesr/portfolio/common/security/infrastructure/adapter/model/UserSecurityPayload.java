package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model;

import java.time.Instant;
import java.util.Set;
import lombok.Data;

@Data
public class UserSecurityPayload {
  private String sub;
  private Instant iat;
  private Instant exp;
  private Set<String> authorities;
  private Set<String> roles;
}
