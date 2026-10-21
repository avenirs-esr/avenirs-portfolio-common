package fr.avenirsesr.portfolio.common.skill.application.adapter.dto;

import fr.avenirsesr.portfolio.common.skill.domain.model.enums.ESkillReferentialType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(requiredProperties = {"type", "externalId", "title", "nodes"})
public record SkillCatalogDTO(
    @Schema(ref = "#/components/schemas/ESkillReferentialType") ESkillReferentialType type,
    String externalId,
    String title,
    List<SkillNodeDTO> nodes) {}
