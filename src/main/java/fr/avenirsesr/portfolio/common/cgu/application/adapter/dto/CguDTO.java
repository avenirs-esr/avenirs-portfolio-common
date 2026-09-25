package fr.avenirsesr.portfolio.common.cgu.application.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(requiredProperties = {"id", "version", "uploadedAt", "content"})
public record CguDTO(UUID id, int version, Instant uploadedAt, String content) {}
