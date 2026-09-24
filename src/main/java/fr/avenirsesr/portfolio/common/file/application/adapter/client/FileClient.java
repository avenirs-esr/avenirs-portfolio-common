package fr.avenirsesr.portfolio.common.file.application.adapter.client;

import fr.avenirsesr.portfolio.common.file.application.adapter.dto.FileDTO;
import fr.avenirsesr.portfolio.common.file.application.adapter.request.FileUploadRequest;
import java.util.UUID;

public interface FileClient {
  FileDTO upload(FileUploadRequest request);

  FileDTO get(UUID fileId);

  void delete(UUID fileId);
}
