package fr.avenirsesr.portfolio.common.configuration.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class ConfigurationException extends BusinessException {
  public ConfigurationException() {
    super(EErrorCode.CONFIGURATION_ERROR);
  }

  public ConfigurationException(String customMessage) {
    super(EErrorCode.CONFIGURATION_ERROR, customMessage);
  }
}
