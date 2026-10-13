package fr.avenirsesr.portfolio.common.file.application.adapter.request;

import fr.avenirsesr.portfolio.common.file.domain.model.FileContent;

public record FileUploadRequest(
    String fileName, String mimeType, FileContent content, long size, boolean isRestricted) {}
