package fr.avenirsesr.portfolio.common.group.application.adapter.dto;

import fr.avenirsesr.portfolio.common.group.domain.model.enums.EGroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(requiredProperties = {"id", "name", "type", "institutionId", "institutionName", "usersCount"})
public record GroupDTO(
    UUID id,
    String name,
    @Schema(ref = "#/components/schemas/EGroupType") EGroupType type,
    UUID institutionId,
    String institutionName,
    UUID parentId,
    String parentName,
    int usersCount) {}
