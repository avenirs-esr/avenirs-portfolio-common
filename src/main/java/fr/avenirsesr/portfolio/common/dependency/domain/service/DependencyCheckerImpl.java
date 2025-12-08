package fr.avenirsesr.portfolio.common.dependency.domain.service;

import fr.avenirsesr.portfolio.common.dependency.domain.model.DependencyCheckConfig;
import fr.avenirsesr.portfolio.common.dependency.domain.model.EDependencyBehaviour;
import fr.avenirsesr.portfolio.common.dependency.domain.port.input.DependencyChecker;
import fr.avenirsesr.portfolio.common.dependency.infrastructure.exception.DependencyCheckFailedException;
import fr.avenirsesr.portfolio.common.dependency.infrastructure.exception.DependencyTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class DependencyCheckerImpl implements DependencyChecker {

  private static final Logger logger = LoggerFactory.getLogger(DependencyCheckerImpl.class);

  private final DependencyCheckConfig config;
  private final WebClient webClient;

  public DependencyCheckerImpl(DependencyCheckConfig config, WebClient webClient) {
    this.config = config;
    this.webClient = webClient;
  }

  @Override
  public void checkAndWait(String serviceName, String healthUrl) {
    logger.info("Starting dependency check for service '{}' at '{}'", serviceName, healthUrl);
    logger.info(
        "Configuration: behaviour={}, timeout={}s, retry interval={}s",
        config.behaviour(),
        config.timeoutSeconds(),
        config.retryIntervalSeconds());

    if (isServiceHealthy(healthUrl)) {
      logger.info("Service '{}' is already healthy", serviceName);
      return;
    }

    if (config.behaviour() == EDependencyBehaviour.FAIL) {
      logger.error(
          "Service '{}' is not available at '{}' and behaviour is set to FAIL",
          serviceName,
          healthUrl);
      throw new DependencyCheckFailedException(serviceName, healthUrl);
    }

    waitForService(serviceName, healthUrl);
  }

  private void waitForService(String serviceName, String healthUrl) {
    long startTime = System.currentTimeMillis();
    int attemptCount = 0;

    while (true) {
      attemptCount++;
      long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;

      if (config.hasTimeout() && elapsedSeconds >= config.timeoutSeconds()) {
        String errorMessage =
            String.format(
                "Timeout waiting for service '%s' after %d attempts (%d seconds)",
                serviceName, attemptCount, elapsedSeconds);
        logger.error(errorMessage);
        throw new DependencyTimeoutException(serviceName, healthUrl, config.timeoutSeconds());
      }

      logger.info(
          "Waiting for service '{}' - Attempt {}, Elapsed: {}s{}",
          serviceName,
          attemptCount,
          elapsedSeconds,
          config.hasTimeout()
              ? String.format(", Timeout in: %ds", config.timeoutSeconds() - elapsedSeconds)
              : " (no timeout)");

      if (isServiceHealthy(healthUrl)) {
        logger.info(
            "Service '{}' is now healthy after {} attempts ({} seconds)",
            serviceName,
            attemptCount,
            elapsedSeconds);
        return;
      }

      try {
        Thread.sleep(config.retryIntervalMillis());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        String errorMessage =
            String.format("Interrupted while waiting for service '%s'", serviceName);
        logger.error(errorMessage, e);
        throw new DependencyTimeoutException(serviceName, healthUrl, config.timeoutSeconds());
      }
    }
  }

  private boolean isServiceHealthy(String healthUrl) {
    try {
      String response = webClient.get().uri(healthUrl).retrieve().bodyToMono(String.class).block();
      return response != null;
    } catch (Exception e) {
      logger.debug("Health check failed for '{}': {}", healthUrl, e.getMessage());
      return false;
    }
  }
}
