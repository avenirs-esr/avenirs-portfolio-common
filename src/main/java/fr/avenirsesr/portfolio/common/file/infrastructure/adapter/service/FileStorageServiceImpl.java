package fr.avenirsesr.portfolio.common.file.infrastructure.adapter.service;

import fr.avenirsesr.portfolio.common.file.domain.exception.FileStorageException;
import fr.avenirsesr.portfolio.common.file.domain.exception.FileNotFoundException;
import fr.avenirsesr.portfolio.common.file.domain.model.FileContent;
import fr.avenirsesr.portfolio.common.file.domain.model.FileResource;
import fr.avenirsesr.portfolio.common.file.domain.port.output.service.FileStorageService;
import fr.avenirsesr.portfolio.common.file.infrastructure.configuration.FileStorageConstants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Stores files on the local filesystem. The locator handed back to the domain is the absolute path
 * of the written file.
 *
 * <p>Default backend: it stays active as long as {@code file.storage.type} is not set to {@code
 * s3}.
 */
@Slf4j
@Component
@Primary
@ConditionalOnProperty(name = "file.storage.type", havingValue = "local", matchIfMissing = true)
public class FileStorageServiceImpl implements FileStorageService {

  @Override
  public String upload(FileResource fileResource) {
    String uploadDir = System.getProperty("user.dir") + FileStorageConstants.STORAGE_PATH;

    File dir = new File(uploadDir);
    if (!dir.exists()) dir.mkdirs();

    var fileName = fileResource.id() + "." + fileResource.fileType().name().toLowerCase();

    String filePath = String.join("", uploadDir, "/", fileName);
    try (InputStream content = fileResource.content().open()) {
      java.nio.file.Files.copy(
          content, java.nio.file.Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new FileStorageException("Failed to upload file " + fileResource.fileName(), e);
    }

    log.info("File {} has been uploaded as {}", fileResource.fileName(), fileName);
    return filePath;
  }

  @Override
  public FileContent get(String locator) {
    File file = new File(locator);

    if (!file.exists()) {
      throw new FileNotFoundException();
    }

    return () -> java.nio.file.Files.newInputStream(file.toPath());
  }

  @Override
  public void delete(String locator) {
    File file = new File(locator);

    if (!file.exists()) {
      log.error("No file found at path {}", locator);
      throw new FileNotFoundException();
    }

    if (!file.delete()) {
      throw new FileStorageException("Failed to delete file at path " + locator, null);
    }

    log.info("File at path {} has been deleted", locator);
  }
}
