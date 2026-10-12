package fr.avenirsesr.portfolio.common.file.domain.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface FileContent {
  InputStream open() throws IOException;

  static FileContent ofBytes(byte[] bytes) {
    return () -> new ByteArrayInputStream(bytes);
  }
}
