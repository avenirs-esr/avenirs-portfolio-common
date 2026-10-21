package fr.avenirsesr.portfolio.common.skill.application.adapter.dto;

import fr.avenirsesr.portfolio.common.skill.domain.model.enums.ESkillKind;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(requiredProperties = {"id", "title", "assessable"})
public record SkillNodeDTO(
    UUID id,
    String externalCode,
    String title,
    String description,
    UUID parentId,
    @Schema(ref = "#/components/schemas/ESkillKind") ESkillKind kind,
    boolean assessable) {}
