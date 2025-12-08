package fr.avenirsesr.portfolio.common.dependency.domain.port.input;

public interface DependencyChecker {

  void checkAndWait(String serviceName, String healthUrl);
}
