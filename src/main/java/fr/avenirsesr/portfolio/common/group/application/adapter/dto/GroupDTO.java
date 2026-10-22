package fr.avenirsesr.portfolio.common.group.application.adapter.dto;

import fr.avenirsesr.portfolio.common.group.domain.model.enums.EGroupType;
import fr.avenirsesr.portfolio.common.institution.application.adapter.dto.InstitutionReferenceDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(requiredProperties = {"id", "name", "type", "institution", "usersCount"})
public record GroupDTO(
    UUID id,
    String name,
    @Schema(ref = "#/components/schemas/EGroupType") EGroupType type,
    InstitutionReferenceDTO institution,
    GroupReferenceDTO parent,
    int usersCount) {}
