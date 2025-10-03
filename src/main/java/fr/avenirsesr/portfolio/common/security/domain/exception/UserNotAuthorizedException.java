package fr.avenirsesr.portfolio.common.security.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class UserNotAuthorizedException extends BusinessException {
  public UserNotAuthorizedException() {
    super(EErrorCode.USER_NOT_AUTHORIZED);
  }

  public UserNotAuthorizedException(String customMessage) {
    super(EErrorCode.USER_NOT_AUTHORIZED, customMessage);
  }
}
