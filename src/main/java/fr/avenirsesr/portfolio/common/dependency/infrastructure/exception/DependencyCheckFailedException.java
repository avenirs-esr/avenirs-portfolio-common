package fr.avenirsesr.portfolio.common.dependency.infrastructure.exception;

import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class DependencyCheckFailedException extends BusinessException {

  public DependencyCheckFailedException() {
    super(EErrorCode.DEPENDENCY_CHECK_FAILED);
  }

  public DependencyCheckFailedException(String customMessage) {
    super(EErrorCode.DEPENDENCY_CHECK_FAILED, customMessage);
  }

  public DependencyCheckFailedException(String serviceName, String healthUrl) {
    super(
        EErrorCode.DEPENDENCY_CHECK_FAILED,
        String.format(
            "Service '%s' is not available at '%s' and behaviour is set to FAIL",
            serviceName, healthUrl));
  }
}
