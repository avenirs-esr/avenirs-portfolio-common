package fr.avenirsesr.portfolio.common.openapi.infrastructure.adapter;

import io.swagger.v3.oas.models.OpenAPI;

public class BaseOpenApiConfiguration {

  public void baseCustomization(OpenAPI openApi) {

    openApi
        .getComponents()
        .addSchemas("EErrorCode", BaseSwaggerSchema.errorCodeSchema)
        .addSchemas("ELanguage", BaseSwaggerSchema.languageSchema)
        .addSchemas("ESortField", BaseSwaggerSchema.sortFieldSchema)
        .addSchemas("ESortOrder", BaseSwaggerSchema.sortOrderSchema)
        .addSchemas("EDurationUnit", BaseSwaggerSchema.durationUnitSchema);
  }
}
