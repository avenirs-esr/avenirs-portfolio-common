package fr.avenirsesr.portfolio.common.file.application.adapter.controller;

import static fr.avenirsesr.portfolio.common.file.application.adapter.MultipartFileReader.asUploadRequest;

import fr.avenirsesr.portfolio.common.file.application.adapter.client.FileClient;
import fr.avenirsesr.portfolio.common.file.application.adapter.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public abstract class AbstractFileController {
  protected final FileClient fileClient;

  protected AbstractFileController(FileClient fileClient) {
    this.fileClient = fileClient;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<FileDTO> upload(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "isRestricted", defaultValue = "false") boolean isRestricted) {
    log.debug("Received request to upload file [{}]", file.getOriginalFilename());

    FileDTO uploaded = fileClient.upload(asUploadRequest(file, isRestricted));

    return ResponseEntity.status(HttpStatus.CREATED).body(uploaded);
  }
}
