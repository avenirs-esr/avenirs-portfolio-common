package fr.avenirsesr.portfolio.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Test application configuration for avenirs-portfolio-common module tests. This class provides
 * the @SpringBootConfiguration needed for Spring Boot tests. It includes a test controller to
 * provide endpoints for security testing.
 */
@SpringBootApplication
public class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }
}
