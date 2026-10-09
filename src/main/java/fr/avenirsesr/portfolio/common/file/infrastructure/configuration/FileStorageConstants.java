package fr.avenirsesr.portfolio.common.file.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Settings shared by every file storage backend.
 *
 * <p>Holds only what the registry itself needs. Anything tied to a given kind of business file —
 * the default user pictures for instance — belongs to the service that owns that kind of file.
 */
@Getter
@Setter
/**
 * Part of the shared file registry, wired only where {@code file.registry.enabled} is true.
 *
 * <p>Every service embeds the sources of this library, including the ones it has no use for. Gating
 * the registry keeps a service that owns no file from having to declare a storage backend, an
 * uploader provider or a file table just to start up.
 */
@ConditionalOnProperty(name = "file.registry.enabled", havingValue = "true")
@Configuration
public class FileStorageConstants {
  @Value("${file.storage.local-path}")
  private String storagePath;

  @Value("${file.storage.endpoint-prefix}")
  private String endpointPrefix;

  public static String STORAGE_PATH;

  public static String PHOTO_ENDPOINT_PREFIX;

  public static UUID PLACEHOLDER_FILE_UUID =
      UUID.fromString("00000000-0000-0000-0000-000000000000");

  /**
   * Builds the URL serving the content of the given file.
   *
   * <p>This is the only supported way to expose a file: {@code File.uri} holds a storage locator
   * that is meaningful to the storage adapter alone, never to a client.
   */
  public static String publicUrlOf(UUID fileId) {
    return PHOTO_ENDPOINT_PREFIX + "/" + fileId;
  }

  @PostConstruct
  private void init() {
    STORAGE_PATH = storagePath;
    PHOTO_ENDPOINT_PREFIX = endpointPrefix;
  }
}
