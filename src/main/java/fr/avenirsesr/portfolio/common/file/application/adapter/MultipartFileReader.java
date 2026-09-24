package fr.avenirsesr.portfolio.common.file.application.adapter;

import fr.avenirsesr.portfolio.common.file.domain.exception.FileStorageException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface MultipartFileReader {
  static byte[] readBytes(MultipartFile file) {
    try {
      return file.getBytes();
    } catch (IOException e) {
      throw new FileStorageException("Failed to read uploaded file", e);
    }
  }
}
