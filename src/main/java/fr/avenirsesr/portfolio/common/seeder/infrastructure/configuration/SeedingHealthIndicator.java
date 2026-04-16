package fr.avenirsesr.portfolio.common.seeder.infrastructure.configuration;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeedingHealthIndicator {

  private final boolean seederEnabled;
  private final SeedingState seedingState;

  public SeedingHealthIndicator(
      @Value("${seeder.enabled:false}") boolean seederEnabled, SeedingState seedingState) {
    this.seederEnabled = seederEnabled;
    this.seedingState = seedingState;
  }

  @GetMapping("/health/seeding")
  public Map<String, Object> health() {
    if (!seederEnabled) {
      return Map.of("status", "UP", "seeder.enabled", false);
    }

    if (seedingState.isCompleted()) {
      return Map.of(
          "status", "UP",
          "seeding", "completed");
    }

    String err = seedingState.getError();
    if (err != null && !err.isBlank()) {
      return Map.of(
          "status", "DOWN",
          "seeding", "failed",
          "error", err);
    }

    return Map.of(
        "status", "DOWN",
        "seeding", "running_or_not_started");
  }
}
