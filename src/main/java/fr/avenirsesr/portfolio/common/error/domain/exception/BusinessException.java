package fr.avenirsesr.portfolio.common.error.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;
import java.util.Map;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final EErrorCode errorCode;
  private final Map<String, Object> details;

  public BusinessException(EErrorCode errorCode) {
    this(errorCode, errorCode != null ? errorCode.getMessage() : null, Map.of());
  }

  public BusinessException(EErrorCode errorCode, String message) {
    this(errorCode, message, Map.of());
  }

  public BusinessException(EErrorCode errorCode, Map<String, Object> details) {
    this(errorCode, errorCode != null ? errorCode.getMessage() : null, details);
  }

  private BusinessException(EErrorCode errorCode, String message, Map<String, Object> details) {
    super(message);
    this.errorCode = errorCode;
    this.details = details;
  }
}
