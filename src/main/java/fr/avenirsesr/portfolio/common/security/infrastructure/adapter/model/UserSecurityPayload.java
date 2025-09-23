package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class UserSecurityPayload {
  private UUID sub;
  private Instant iat;
  private Instant exp;
}
