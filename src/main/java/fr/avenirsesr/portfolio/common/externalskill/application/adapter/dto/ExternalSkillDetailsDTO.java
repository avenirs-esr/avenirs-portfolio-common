package fr.avenirsesr.portfolio.common.externalskill.application.adapter.dto;

import fr.avenirsesr.portfolio.common.externalskill.domain.model.enums.EExternalSkillType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(requiredProperties = {"id", "title", "categoryPath", "type"})
public record ExternalSkillDetailsDTO(
    UUID id,
    String title,
    List<ExternalSkillCategoryDTO> categoryPath,
    @Schema(ref = "#/components/schemas/EExternalSkillType") EExternalSkillType type) {}
