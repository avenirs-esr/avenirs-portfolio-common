package fr.avenirsesr.portfolio.common.file.infrastructure.adapter.client;

import fr.avenirsesr.portfolio.common.file.application.adapter.client.FileClient;
import fr.avenirsesr.portfolio.common.file.application.adapter.dto.FileDTO;
import fr.avenirsesr.portfolio.common.file.application.adapter.request.FileUploadRequest;
import fr.avenirsesr.portfolio.common.file.domain.model.FileResource;
import fr.avenirsesr.portfolio.common.file.domain.port.input.FileResourceService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Slf4j
/**
 * Part of the shared file registry, wired only where {@code file.registry.enabled} is true.
 *
 * <p>Every service embeds the sources of this library, including the ones it has no use for. Gating
 * the registry keeps a service that owns no file from having to declare a storage backend, an
 * uploader provider or a file table just to start up.
 */
@ConditionalOnProperty(name = "file.registry.enabled", havingValue = "true")
@Component
@RequiredArgsConstructor
public class LocalFileClient implements FileClient {
  private final FileResourceService fileResourceService;

  @Override
  public FileDTO upload(FileUploadRequest request) {
    log.debug("Uploading file {} of type {}", request.fileName(), request.mimeType());
    var file =
        fileResourceService.upload(
            request.fileName(),
            request.mimeType(),
            request.content().length,
            request.content(),
            request.isRestricted());
    return FileDTO.fromDomain(file);
  }

  @Override
  public FileDTO get(UUID fileId) {
    log.debug("Fetching file {}", fileId);
    return FileDTO.fromDomain(fileResourceService.get(fileId));
  }

  @Override
  public byte[] fetchContent(UUID fileId) {
    log.debug("Fetching file content {}", fileId);
    FileResource file = fileResourceService.fetchContent(fileId);
    return file.content();
  }

  @Override
  public void delete(UUID fileId) {
    log.debug("Deleting file {}", fileId);
    fileResourceService.delete(fileId);
  }
}
