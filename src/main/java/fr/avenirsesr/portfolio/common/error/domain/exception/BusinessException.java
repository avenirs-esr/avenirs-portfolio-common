package fr.avenirsesr.portfolio.common.error.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final EErrorCode errorCode;

  public BusinessException(EErrorCode errorCode) {
    super(errorCode != null ? errorCode.getMessage() : null);
    this.errorCode = errorCode;
  }

  public BusinessException(EErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
