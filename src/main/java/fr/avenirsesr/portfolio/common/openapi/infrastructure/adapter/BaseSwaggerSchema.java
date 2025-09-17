package fr.avenirsesr.portfolio.common.openapi.infrastructure.adapter;

import fr.avenirsesr.portfolio.common.data.domain.model.enums.ESortField;
import fr.avenirsesr.portfolio.common.data.domain.model.enums.ESortOrder;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.temporal.domain.model.enums.EDurationUnit;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.util.Arrays;

public interface BaseSwaggerSchema {

  Schema<String> errorCodeSchema =
      new StringSchema()
          .name("EErrorCode")
          ._enum(Arrays.stream(EErrorCode.values()).map(Enum::name).toList())
          .description("Enum for error codes");

  Schema<String> languageSchema =
      new StringSchema()
          .name("ELanguage")
          ._enum(Arrays.stream(ELanguage.values()).map(Enum::name).toList())
          .description("Enum for languages");

  Schema<String> sortFieldSchema =
      new StringSchema()
          .name("ESortField")
          ._enum(Arrays.stream(ESortField.values()).map(Enum::name).toList())
          .description("Enum for sort fields");

  Schema<String> sortOrderSchema =
      new StringSchema()
          .name("ESortOrder")
          ._enum(Arrays.stream(ESortOrder.values()).map(Enum::name).toList())
          .description("Enum for sort orders");

  Schema<String> durationUnitSchema =
      new StringSchema()
          .name("EDurationUnit")
          ._enum(Arrays.stream(EDurationUnit.values()).map(Enum::name).toList())
          .description("Enum for duration units");

  static Schema<String> createEnumSchema(Class<? extends Enum<?>> enumClass, String description) {
    return new StringSchema()
        .name(enumClass.getSimpleName())
        ._enum(Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toList())
        .description(description);
  }
}
