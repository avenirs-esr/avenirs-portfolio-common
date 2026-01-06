package fr.avenirsesr.portfolio.common.error.domain.model.enums;

import java.text.MessageFormat;
import lombok.Getter;

@Getter
public enum EErrorCode {
  USER_NOT_FOUND("User not found"),
  USER_ALREADY_EXISTS("User already exists"),
  USER_NOT_AUTHORIZED("User not authorized"),
  USER_CATEGORY_NOT_FOUND("User category not found"),
  MAX_FILE_SIZE_EXCEEDED("Maximum upload size exceeded"),
  UNKNOWN_FILE_TYPE("File type is not supported"),
  PROGRAM_NOT_FOUND("Program not found"),
  TRAINING_PATH_NOT_FOUND("Training path not found"),
  SKILL_NOT_FOUND("Skill not found"),
  SKILL_LEVEL_NOT_FOUND("Skill level not found"),
  TRACE_NOT_FOUND("Trace not found"),
  AMS_NOT_FOUND("AMS not found"),
  USER_IS_NOT_STUDENT_EXCEPTION("User is not student"),
  USER_IS_NOT_TEACHER_EXCEPTION("User is not teacher"),
  LANGUAGE_NOT_SUPPORTED("Language not supported"),
  INVALID_ARGUMENT_TYPE("The type of the argument is invalid"),
  DECLARED_SKILL_NOT_AVAILABLE("Declared skill not available"),
  DECLARED_SKILL_NOT_FOUND("Declared skill not found"),
  DECLARED_SKILL_PROGRESS_NOT_FOUND("Declared skill progress not found"),
  STUDENT_DECLARED_ALREADY_EXIST("This declared skill is already assigned to the student"),
  STUDENT_PROGRESS_NOT_FOUND("Student progress not found"),
  FILE_NOT_FOUND("File not found"),
  CONFIGURATION_ERROR("Configuration error"),
  TITLE_TOO_LONG("The length of the title is too long"),
  DESCRIPTION_TOO_LONG("The length of the description is too long"),
  RATING_OUT_OF_BOUNCE("Rating is out of bounce"),
  ASSOCIATION_NOT_FOUND("Association not found"),
  SELF_KNOWLEDGE_ELEMENT_NOT_FOUND("Self knowledge element not found"),
  SELF_KNOWLEDGE_ELEMENTS_ARE_NOT_IN_SAME_CATEGORY(
      "Self knowledge elements are not in the same category"),
  SELF_KNOWLEDGE_CATEGORY_LIST_EMPTY("Self knowledge category list is empty"),
  SELF_KNOWLEDGE_CATEGORY_NOT_FOUND("Self knowledge category not found"),
  SELF_KNOWLEDGE_CATEGORY_NOT_AVAILABLE("Self knowledge category not available"),
  SELF_KNOWLEDGE_CATEGORY_IS_MANDATORY("Self knowledge category is mandatory"),
  SELF_KNOWLEDGE_CATEGORY_NOT_LINKED("Self knowledge category is not linked to the student"),
  DEPENDENCY_CHECK_TIMEOUT("Dependency check timeout"),
  DEPENDENCY_CHECK_FAILED("Dependency check failed"),
  NOT_BLANK("The field {0} cannot be blank"),
  NOT_NULL("The field {0} cannot be null"),
  TOO_LONG("The field {0} exceeds the maximum allowed length"),
  END_DATE_BEFORE_START_DATE("The end date cannot be before the start date"),
  DECLARED_EXPERIENCE_NOT_FOUND("Declared experience"),
  DECLARED_PROGRAM_NOT_FOUND("Declared program not found"),
  INSTITUTION_CONFIG_NOT_FOUND("Institution configuration not found"),
  INSTITUTION_ID_NULL("Institution id cannot be null");

  private final String message;

  EErrorCode(String message) {
    this.message = message;
  }

  public String formatMessage(Object... args) {
    return MessageFormat.format(this.message, args);
  }
}
