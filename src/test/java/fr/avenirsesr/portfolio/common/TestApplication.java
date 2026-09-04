package fr.avenirsesr.portfolio.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.avenirsesr.portfolio.common.user.domain.port.output.BaseUserService;
import fr.avenirsesr.portfolio.common.user.infrastructure.service.NoOpUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Test application configuration for avenirs-portfolio-common module tests. This class provides
 * the @SpringBootConfiguration needed for Spring Boot tests. It includes a test controller to
 * provide endpoints for security testing.
 */
@SpringBootApplication
public class TestApplication {
  @Bean
  public BaseUserService baseUserService() {
    return new NoOpUserService();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().findAndRegisterModules();
  }

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }
}
