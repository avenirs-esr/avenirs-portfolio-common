package fr.avenirsesr.portfolio.common.dependency.infrastructure.exception;

import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class DependencyTimeoutException extends BusinessException {

  public DependencyTimeoutException() {
    super(EErrorCode.DEPENDENCY_CHECK_TIMEOUT);
  }

  public DependencyTimeoutException(String customMessage) {
    super(EErrorCode.DEPENDENCY_CHECK_TIMEOUT, customMessage);
  }

  public DependencyTimeoutException(String serviceName, String healthUrl, int timeoutSeconds) {
    super(
        EErrorCode.DEPENDENCY_CHECK_TIMEOUT,
        String.format(
            "Dependency check timeout for service '%s' at '%s' after %d seconds",
            serviceName, healthUrl, timeoutSeconds));
  }
}
