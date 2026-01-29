package fr.avenirsesr.portfolio.common.seeder.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class SeedingHealthIndicator implements HealthIndicator {

  private final boolean seederEnabled;
  private final SeedingState seedingState;

  public SeedingHealthIndicator(
      @Value("${seeder.enabled:false}") boolean seederEnabled, SeedingState seedingState) {
    this.seederEnabled = seederEnabled;
    this.seedingState = seedingState;
  }

  @Override
  public Health health() {
    if (!seederEnabled) {
      return Health.up().withDetail("seeder.enabled", false).build();
    }

    if (seedingState.isCompleted()) {
      return Health.up().withDetail("seeding", "completed").build();
    }

    var err = seedingState.getError();
    if (err != null && !err.isBlank()) {
      return Health.down().withDetail("seeding", "failed").withDetail("error", err).build();
    }
    return Health.down().withDetail("seeding", "running_or_not_started").build();
  }
}
