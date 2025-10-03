package fr.avenirsesr.portfolio.common.language.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class LanguageException extends BusinessException {
  public LanguageException() {
    super(EErrorCode.LANGUAGE_NOT_SUPPORTED);
  }

  public LanguageException(String customMessage) {
    super(EErrorCode.LANGUAGE_NOT_SUPPORTED, customMessage);
  }
}
