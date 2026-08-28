package fr.avenirsesr.portfolio.common.error.application.adapter.exception;

import fr.avenirsesr.portfolio.common.configuration.domain.exception.ConfigurationException;
import fr.avenirsesr.portfolio.common.error.application.adapter.response.ErrorResponse;
import fr.avenirsesr.portfolio.common.error.domain.exception.BusinessException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
public abstract class BaseRestExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("Erreur technique inattendue", ex);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("INTERNAL_ERROR", "Une erreur technique est survenue."));
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException ex) {
    log.warn("Accès refusé : {}", ex.getMessage());

    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            new ErrorResponse(
                EErrorCode.ACCESS_DENIED.name(), EErrorCode.ACCESS_DENIED.getMessage()));
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    log.error("Erreur métier : {}", ex.getMessage());

    return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(ex.getErrorCode().name(), ex.getMessage()));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handle(MaxUploadSizeExceededException ex) {
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(
            new ErrorResponse(
                EErrorCode.MAX_FILE_SIZE_EXCEEDED.name(),
                EErrorCode.MAX_FILE_SIZE_EXCEEDED.getMessage()));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
    String name = ex.getParameterName();
    String message = String.format("Missing required parameter: %s", name);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorResponse(
                EErrorCode.INVALID_ARGUMENT_TYPE.name(),
                EErrorCode.INVALID_ARGUMENT_TYPE.getMessage()));
  }

  @ExceptionHandler(ConfigurationException.class)
  public ResponseEntity<ErrorResponse> handleConfigurationError(ConfigurationException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ErrorResponse(
                EErrorCode.CONFIGURATION_ERROR.name(),
                EErrorCode.CONFIGURATION_ERROR.getMessage()));
  }
}
