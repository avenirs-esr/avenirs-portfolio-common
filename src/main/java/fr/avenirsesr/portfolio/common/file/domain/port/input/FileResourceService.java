package fr.avenirsesr.portfolio.common.file.domain.port.input;

import fr.avenirsesr.portfolio.common.file.domain.model.File;
import fr.avenirsesr.portfolio.common.file.domain.model.FileContent;
import fr.avenirsesr.portfolio.common.file.domain.model.FileDownload;
import fr.avenirsesr.portfolio.common.file.domain.model.FileResource;
import java.util.UUID;

public interface FileResourceService {
  File upload(
      String fileName, String mimeType, long size, FileContent content, boolean isRestricted);

  File copy(UUID fileId);

  File get(UUID fileId);

  FileResource fetchContent(UUID fileId);

  FileDownload download(UUID fileId);

  void delete(UUID fileId);
}
