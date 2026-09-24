package fr.avenirsesr.portfolio.common.file.application.adapter.request;

public record FileUploadRequest(
    String fileName, String mimeType, byte[] content, boolean isRestricted) {}
