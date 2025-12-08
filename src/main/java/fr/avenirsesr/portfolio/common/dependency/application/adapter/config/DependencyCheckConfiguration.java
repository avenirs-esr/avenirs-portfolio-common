package fr.avenirsesr.portfolio.common.dependency.application.adapter.config;

import fr.avenirsesr.portfolio.common.dependency.domain.model.DependencyCheckConfig;
import fr.avenirsesr.portfolio.common.dependency.domain.model.EDependencyBehaviour;
import fr.avenirsesr.portfolio.common.dependency.domain.port.input.DependencyChecker;
import fr.avenirsesr.portfolio.common.dependency.domain.service.DependencyCheckerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnProperty(prefix = "avenirs.microservice.dependency", name = "behaviour")
public class DependencyCheckConfiguration {

  @Value("${avenirs.microservice.dependency.behaviour}")
  private String behaviour;

  @Value("${avenirs.microservice.dependency.timeout.seconds:600}")
  private int timeoutSeconds;

  @Value("${avenirs.microservice.dependency.retry.interval.seconds:10}")
  private int retryIntervalSeconds;

  @Bean
  public DependencyCheckConfig dependencyCheckConfig() {
    EDependencyBehaviour behaviourEnum = parseBehaviour(behaviour);
    return new DependencyCheckConfig(behaviourEnum, timeoutSeconds, retryIntervalSeconds);
  }

  @Bean
  public DependencyChecker dependencyChecker(DependencyCheckConfig config, WebClient webClient) {
    return new DependencyCheckerImpl(config, webClient);
  }

  @Bean
  public WebClient dependencyWebClient() {
    return WebClient.builder().build();
  }

  private EDependencyBehaviour parseBehaviour(String behaviourValue) {
    String cleanValue = behaviourValue.trim().toUpperCase();
    if (cleanValue.contains("|")) {
      cleanValue = cleanValue.split("\\|")[0].trim();
    }

    try {
      return EDependencyBehaviour.valueOf(cleanValue);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          String.format("Invalid dependency behaviour: '%s'. Must be WAIT or FAIL", behaviourValue),
          e);
    }
  }
}
