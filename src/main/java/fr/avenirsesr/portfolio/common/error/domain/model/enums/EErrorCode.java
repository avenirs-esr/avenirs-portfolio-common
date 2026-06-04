package fr.avenirsesr.portfolio.common.error.domain.model.enums;

import java.text.MessageFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EErrorCode {
  // 404 Not Found
  USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
  EXTERNAL_USER_NOT_FOUND("External user not found", HttpStatus.NOT_FOUND),
  EXTERNAL_USER_REMOVED("External user was removed", HttpStatus.NOT_FOUND),
  USER_CATEGORY_NOT_FOUND("User category not found", HttpStatus.NOT_FOUND),
  PROGRAM_NOT_FOUND("Program not found", HttpStatus.NOT_FOUND),
  TRAINING_PATH_NOT_FOUND("Training path not found", HttpStatus.NOT_FOUND),
  SKILL_NOT_FOUND("Skill not found", HttpStatus.NOT_FOUND),
  SKILL_LEVEL_NOT_FOUND("Skill level not found", HttpStatus.NOT_FOUND),
  TRACE_NOT_FOUND("Trace not found", HttpStatus.NOT_FOUND),
  AMS_NOT_FOUND("AMS not found", HttpStatus.NOT_FOUND),
  DECLARED_SKILL_NOT_FOUND("Declared skill not found", HttpStatus.NOT_FOUND),
  DECLARED_SKILL_PROGRESS_NOT_FOUND("Declared skill progress not found", HttpStatus.NOT_FOUND),
  STUDENT_PROGRESS_NOT_FOUND("Student progress not found", HttpStatus.NOT_FOUND),
  FILE_NOT_FOUND("File not found", HttpStatus.NOT_FOUND),
  ATTACHMENT_NOT_FOUND("Attachment not found", HttpStatus.NOT_FOUND),
  ASSOCIATION_NOT_FOUND("Association not found", HttpStatus.NOT_FOUND),
  SELF_KNOWLEDGE_ELEMENT_NOT_FOUND("Self knowledge element not found", HttpStatus.NOT_FOUND),
  SELF_KNOWLEDGE_CATEGORY_NOT_FOUND("Self knowledge category not found", HttpStatus.NOT_FOUND),
  DECLARED_EXPERIENCE_NOT_FOUND("Declared experience", HttpStatus.NOT_FOUND),
  DECLARED_PROGRAM_NOT_FOUND("Declared program not found", HttpStatus.NOT_FOUND),
  INSTITUTION_CONFIG_NOT_FOUND("Institution configuration not found", HttpStatus.NOT_FOUND),
  ACTIVITY_NOT_FOUND("Activity not found", HttpStatus.NOT_FOUND),
  ACTIVITY_DRAFT_NOT_FOUND("Activity draft not found", HttpStatus.NOT_FOUND),
  DECLARED_ACTIVITY_NOT_FOUND("Declared activity not found", HttpStatus.NOT_FOUND),

  // 403 Forbidden
  USER_NOT_AUTHORIZED("User not authorized", HttpStatus.FORBIDDEN),
  USER_IS_NOT_STUDENT_EXCEPTION("User is not student", HttpStatus.FORBIDDEN),
  USER_IS_NOT_STAFF_EXCEPTION("User is not staff", HttpStatus.FORBIDDEN),
  EXTERNAL_USER_BLOCKED("External user was blocked", HttpStatus.FORBIDDEN),
  MAXIMUM_ALLOWED_ASSOCIATIONS_REACHED(
      "The maximum allowed associations is already reached", HttpStatus.FORBIDDEN),

  // 401 Unauthorized
  UNAUTHENTICATED_SESSION("Unauthenticated session", HttpStatus.UNAUTHORIZED),

  // 409 Conflict
  USER_ALREADY_EXISTS("User already exists", HttpStatus.CONFLICT),
  STUDENT_DECLARED_ALREADY_EXIST(
      "This declared skill is already assigned to the student", HttpStatus.CONFLICT),
  DECLARED_ACTIVITY_ALREADY_EXIST("Declared activity already exist", HttpStatus.CONFLICT),
  DECLARED_ACTIVITY_ALREADY_FINISHED("Declared activity already finished", HttpStatus.CONFLICT),
  DECLARED_ACTIVITY_HAS_NOT_STARTED("Declared activity has not started", HttpStatus.CONFLICT),
  ASSOCIATION_ALREADY_EXIST("This association already exist", HttpStatus.CONFLICT),

  // 413 Payload Too Large
  MAX_FILE_SIZE_EXCEEDED("Maximum upload size exceeded", HttpStatus.PAYLOAD_TOO_LARGE),

  // 500 Internal Server Error
  FILE_STORAGE_ERROR("File storage operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
  CONFIGURATION_ERROR("Configuration error", HttpStatus.INTERNAL_SERVER_ERROR),

  // 503 Service Unavailable
  DEPENDENCY_CHECK_TIMEOUT("Dependency check timeout", HttpStatus.SERVICE_UNAVAILABLE),
  DEPENDENCY_CHECK_FAILED("Dependency check failed", HttpStatus.SERVICE_UNAVAILABLE),

  // 400 Bad Request (default)
  WRONG_CLASS_TYPE("The class type should be different"),
  UNKNOWN_FILE_TYPE("File type is not supported"),
  INVALID_TRACE_TYPE("Invalid trace type"),
  LANGUAGE_NOT_SUPPORTED("Language not supported"),
  INVALID_ARGUMENT_TYPE("The type of the argument is invalid"),
  DECLARED_SKILL_NOT_AVAILABLE("Declared skill not available"),
  TITLE_TOO_LONG("The length of the title is too long"),
  DESCRIPTION_TOO_LONG("The length of the description is too long"),
  RATING_OUT_OF_BOUNCE("Rating is out of bounce"),
  SELF_KNOWLEDGE_ELEMENTS_ARE_NOT_IN_SAME_CATEGORY(
      "Self knowledge elements are not in the same category"),
  SELF_KNOWLEDGE_CATEGORY_LIST_EMPTY("Self knowledge category list is empty"),
  SELF_KNOWLEDGE_CATEGORY_NOT_AVAILABLE("Self knowledge category not available"),
  SELF_KNOWLEDGE_CATEGORY_IS_MANDATORY("Self knowledge category is mandatory"),
  SELF_KNOWLEDGE_CATEGORY_NOT_LINKED("Self knowledge category is not linked to the student"),
  NOT_BLANK("The field {0} cannot be blank"),
  NOT_NULL("The field {0} cannot be null"),
  TOO_LONG("The field {0} exceeds the maximum allowed length"),
  NOT_URL("The field {0} is not a valid URL"),
  END_DATE_BEFORE_START_DATE("The end date cannot be before the start date"),
  INSTITUTION_ID_NULL("Institution id cannot be null"),
  FIRSTNAME_IS_NULL("First name cannot be null"),
  LASTNAME_IS_NULL("Last name cannot be null"),
  INVALID_DATE_FORMAT("Invalid date format. Expected format: yyyy-MM-dd."),
  DECLARED_ACTIVITY_DATES("Start and end dates must either be both null or both provided"),
  DECLARED_ACTIVITY_START_DATE_BEFORE_SUBSCRIPTION(
      "Start date cannot be before the subscription date");

  private final String message;
  private final HttpStatus httpStatus;

  EErrorCode(String message) {
    this(message, HttpStatus.BAD_REQUEST);
  }

  EErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public String formatMessage(Object... args) {
    return MessageFormat.format(this.message, args);
  }
}
