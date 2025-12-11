package fr.avenirsesr.portfolio.common.error.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class FieldValidationException extends BusinessException {

  public FieldValidationException(EErrorCode code) {
    super(code);
  }

  public FieldValidationException(EErrorCode code, String fieldName) {
    super(code, code.formatMessage(fieldName));
  }
}
