package fr.avenirsesr.portfolio.common.file.infrastructure.service;

import fr.avenirsesr.portfolio.common.file.domain.port.input.FileResourceService;
import fr.avenirsesr.portfolio.common.file.domain.port.output.repository.FileRepository;
import fr.avenirsesr.portfolio.common.file.domain.port.output.service.FileStorageService;
import fr.avenirsesr.portfolio.common.file.domain.port.output.service.FileUploaderProvider;
import fr.avenirsesr.portfolio.common.file.domain.service.FileResourceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Part of the shared file registry, wired only where {@code file.registry.enabled} is true.
 *
 * <p>Every service embeds the sources of this library, including the ones it has no use for. Gating
 * the registry keeps a service that owns no file from having to declare a storage backend, an
 * uploader provider or a file table just to start up.
 */
@ConditionalOnProperty(name = "file.registry.enabled", havingValue = "true")
@Configuration
@RequiredArgsConstructor
public class FileResourceServiceConfig {
  private final FileStorageService fileStorageService;
  private final FileRepository fileRepository;
  private final FileUploaderProvider fileUploaderProvider;

  @Bean
  @Primary
  public FileResourceService fileResourceService() {
    return new FileResourceServiceImpl(fileStorageService, fileRepository, fileUploaderProvider);
  }
}
