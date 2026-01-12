package fr.avenirsesr.portfolio.common.error.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class InvalidDateFormatException extends BusinessException {

  public InvalidDateFormatException() {
    super(EErrorCode.INVALID_DATE_FORMAT);
  }

  public InvalidDateFormatException(String customMessage) {
    super(EErrorCode.INVALID_DATE_FORMAT, customMessage);
  }
}
