package fr.avenirsesr.portfolio.common.error.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class UnsupportedTranslationMappingException extends BusinessException {

  public UnsupportedTranslationMappingException() {
    super(EErrorCode.UNSUPPORTED_TRANSLATION_MAPPING);
  }

  public UnsupportedTranslationMappingException(String customMessage) {
    super(EErrorCode.UNSUPPORTED_TRANSLATION_MAPPING, customMessage);
  }
}
