package fr.avenirsesr.portfolio.common.seeder.infrastructure.configuration;

import fr.avenirsesr.portfolio.common.seeder.domain.port.output.SharedDataGenerator;
import fr.avenirsesr.portfolio.common.seeder.infrastructure.adapter.data.ESeederSource;
import fr.avenirsesr.portfolio.common.seeder.infrastructure.adapter.data.SharedFakerDataGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedDataGeneratorConfig {
  @Value("${seeder.source}")
  private ESeederSource seederSource;

  @Bean
  public SharedDataGenerator sharedDataGenerator() {
    return switch (seederSource) {
      case FAKER, CSV -> new SharedFakerDataGenerator();
    };
  }
}
