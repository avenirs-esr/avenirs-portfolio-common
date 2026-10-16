package fr.avenirsesr.portfolio.common.group.application.adapter.dto;

import fr.avenirsesr.portfolio.common.group.domain.model.enums.EGroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(requiredProperties = {"id", "name", "type", "usersCount"})
public record GroupOverviewDTO(
    UUID id,
    String name,
    @Schema(ref = "#/components/schemas/EGroupType") EGroupType type,
    int usersCount,
    UUID parentId,
    String parentName) {}
