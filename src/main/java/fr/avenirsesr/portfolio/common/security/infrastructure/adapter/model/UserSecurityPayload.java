package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model;

import java.time.Instant;
import lombok.Data;

@Data
public class UserSecurityPayload {
  private String sub;
  private Instant iat;
  private Instant exp;
}
