package fr.avenirsesr.portfolio.common.dependency.domain.model;

import fr.avenirsesr.portfolio.common.dependency.domain.model.enums.EHealthCheckMode;

public record DependencyCheckConfig(
    EDependencyBehaviour behaviour,
    int timeoutSeconds,
    int retryIntervalSeconds,
    EHealthCheckMode healthCheckMode) {
  public DependencyCheckConfig {
    if (healthCheckMode == null) {
      healthCheckMode = EHealthCheckMode.RESPONSE_EXISTS;
    }
  }

  public boolean hasTimeout() {
    return timeoutSeconds > 0;
  }

  public long retryIntervalMillis() {
    return retryIntervalSeconds * 1000L;
  }
}
