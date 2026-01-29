package fr.avenirsesr.portfolio.common.seeder.infrastructure.configuration;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;

@Component
public class SeedingState {

  private final AtomicBoolean completed = new AtomicBoolean(false);
  private final AtomicReference<String> error = new AtomicReference<>(null);

  public boolean isCompleted() {
    return completed.get();
  }

  public String getError() {
    return error.get();
  }

  public void markCompleted() {
    completed.set(true);
    error.set(null);
  }

  public void markFailed(Exception e) {
    completed.set(false);
    error.set(e.getMessage());
  }
}
