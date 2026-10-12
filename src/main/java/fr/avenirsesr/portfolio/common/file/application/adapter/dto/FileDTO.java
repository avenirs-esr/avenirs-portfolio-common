package fr.avenirsesr.portfolio.common.file.application.adapter.dto;

import fr.avenirsesr.portfolio.common.file.domain.model.File;
import fr.avenirsesr.portfolio.common.file.domain.model.enums.EFileType;
import fr.avenirsesr.portfolio.common.file.infrastructure.configuration.FileStorageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Schema(requiredProperties = {"id", "fileName", "fileType", "fileSize", "url", "uploadedAt"})
public record FileDTO(
    UUID id,
    String fileName,
    @Schema(ref = "#/components/schemas/EFileType") EFileType fileType,
    long fileSize,
    String url,
    Instant uploadedAt) {

  public static FileDTO fromDomain(File file) {
    if (file == null) {
      return null;
    }

    return new FileDTO(
        file.getId(),
        file.getFileName(),
        file.getFileType(),
        file.getSize(),
        FileStorageConstants.publicUrlOf(file.getId()),
        file.getUploadedAt());
  }

  public static FileDTO fromDomain(Optional<File> file) {
    return file.map(FileDTO::fromDomain).orElse(null);
  }
}
