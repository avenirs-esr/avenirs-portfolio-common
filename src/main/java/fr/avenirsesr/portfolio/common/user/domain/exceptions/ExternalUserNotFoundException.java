package fr.avenirsesr.portfolio.common.user.domain.exceptions;

import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class ExternalUserNotFoundException extends BusinessException {

  public ExternalUserNotFoundException() {
    super(EErrorCode.EXTERNAL_USER_NOT_FOUND);
  }

  public ExternalUserNotFoundException(EErrorCode errorCode) {
    super(errorCode);
  }

  public ExternalUserNotFoundException(String customMessage) {
    super(EErrorCode.EXTERNAL_USER_NOT_FOUND, customMessage);
  }
}
