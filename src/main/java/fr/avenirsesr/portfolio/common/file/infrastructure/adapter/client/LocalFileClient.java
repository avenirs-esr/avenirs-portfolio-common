package fr.avenirsesr.portfolio.common.file.infrastructure.adapter.client;

import fr.avenirsesr.portfolio.common.file.application.adapter.client.FileClient;
import fr.avenirsesr.portfolio.common.file.application.adapter.dto.FileDTO;
import fr.avenirsesr.portfolio.common.file.application.adapter.request.FileUploadRequest;
import fr.avenirsesr.portfolio.common.file.domain.exception.FileStorageException;
import fr.avenirsesr.portfolio.common.file.domain.port.input.FileResourceService;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "file.registry.enabled", havingValue = "true")
public class LocalFileClient implements FileClient {
  private final FileResourceService fileResourceService;

  @Override
  public FileDTO upload(FileUploadRequest request) {
    log.debug("Uploading file {} of type {}", request.fileName(), request.mimeType());
    var file =
        fileResourceService.upload(
            request.fileName(),
            request.mimeType(),
            request.size(),
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
    try (InputStream content = fileResourceService.fetchContent(fileId).content().open()) {
      return content.readAllBytes();
    } catch (IOException e) {
      throw new FileStorageException("Failed to read the content of file " + fileId, e);
    }
  }

  @Override
  public void delete(UUID fileId) {
    log.debug("Deleting file {}", fileId);
    fileResourceService.delete(fileId);
  }
}
