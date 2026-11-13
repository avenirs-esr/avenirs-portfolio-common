package fr.avenirsesr.portfolio.common.error.application.adapter.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(requiredProperties = {"code", "message"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(String code, String message, Map<String, Object> details) {

  public ErrorResponse(String code, String message) {
    this(code, message, null);
  }
}
