package fr.avenirsesr.portfolio.common.skill.application.adapter.dto;

import fr.avenirsesr.portfolio.common.skill.domain.model.enums.ESkillKind;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {"libelle", "type"})
public record ExternalSkillCategoryDTO(
    String libelle, @Schema(ref = "#/components/schemas/ESkillKind") ESkillKind type) {}
