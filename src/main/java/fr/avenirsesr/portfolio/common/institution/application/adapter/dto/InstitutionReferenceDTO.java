package fr.avenirsesr.portfolio.common.institution.application.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(requiredProperties = {"id", "name"})
public record InstitutionReferenceDTO(UUID id, String name) {}
