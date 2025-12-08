package fr.avenirsesr.portfolio.common.dependency.domain.model;

public record DependencyCheckConfig(
    EDependencyBehaviour behaviour, int timeoutSeconds, int retryIntervalSeconds) {
  public DependencyCheckConfig {
    if (timeoutSeconds < 0) {
      throw new IllegalArgumentException("Timeout seconds cannot be negative");
    }
    if (retryIntervalSeconds <= 0) {
      throw new IllegalArgumentException("Retry interval seconds must be positive");
    }
  }

  public boolean hasTimeout() {
    return timeoutSeconds > 0;
  }

  public long timeoutMillis() {
    return timeoutSeconds * 1000L;
  }

  public long retryIntervalMillis() {
    return retryIntervalSeconds * 1000L;
  }
}
