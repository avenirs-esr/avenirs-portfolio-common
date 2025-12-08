package fr.avenirsesr.portfolio.common.externalskill.application.adapter.dto;

import fr.avenirsesr.portfolio.common.externalskill.domain.model.enums.EExternalSkillCategoryType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {"libelle", "type"})
public record ExternalSkillCategoryDTO(
    String libelle,
    @Schema(ref = "#/components/schemas/EExternalSkillCategoryType")
        EExternalSkillCategoryType type) {}
