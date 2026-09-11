package fr.avenirsesr.portfolio.common.institution.application.adapter.dto;

import fr.avenirsesr.portfolio.common.institution.domain.model.enums.EInstitutionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

/** Institution exposed by the back-office to the other Avenirs services. */
@Schema(requiredProperties = {"id", "name", "type"})
public record InstitutionDTO(
    UUID id,
    String name,
    @Schema(ref = "#/components/schemas/EInstitutionType") EInstitutionType type,
    UUID parentId) {}
