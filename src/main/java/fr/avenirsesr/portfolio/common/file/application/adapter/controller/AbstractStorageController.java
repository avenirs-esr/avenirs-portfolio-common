package fr.avenirsesr.portfolio.common.file.application.adapter.controller;

import fr.avenirsesr.portfolio.common.file.domain.model.FileResource;
import fr.avenirsesr.portfolio.common.file.domain.port.input.FileResourceService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
public abstract class AbstractStorageController {
  protected final FileResourceService fileResourceService;

  protected AbstractStorageController(FileResourceService fileResourceService) {
    this.fileResourceService = fileResourceService;
  }

  @GetMapping("/{fileId}")
  public ResponseEntity<Resource> getResourceByFileId(@Valid @PathVariable UUID fileId)
      throws IOException {
    log.debug("Received request to get the content of file [{}]", fileId);
    FileResource file = fileResourceService.fetchContent(fileId);

    return ResponseEntity.ok()
        .contentType(MediaType.asMediaType(MimeType.valueOf(file.fileType().getMimeType())))
        .contentLength(file.size())
        .body(new InputStreamResource(file.content().open()));
  }
}
