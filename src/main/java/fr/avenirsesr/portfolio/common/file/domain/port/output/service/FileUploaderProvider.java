package fr.avenirsesr.portfolio.common.file.domain.port.output.service;

import java.util.UUID;

public interface FileUploaderProvider {
  UUID currentUploaderId();
}
