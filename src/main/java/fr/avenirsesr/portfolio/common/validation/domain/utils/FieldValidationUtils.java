package fr.avenirsesr.portfolio.common.validation.domain.utils;

import fr.avenirsesr.portfolio.common.error.domain.exception.FieldValidationException;
import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;
import java.net.URI;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FieldValidationUtils {

  public static void requireNotBlankAndMaxLength(String fieldName, String value, int maxLength) {
    if (value == null || value.trim().isEmpty()) {
      throw new FieldValidationException(EErrorCode.NOT_BLANK, fieldName);
    }

    String trimmed = value.trim();
    if (trimmed.length() > maxLength) {
      throw new FieldValidationException(EErrorCode.TOO_LONG, fieldName);
    }
  }

  public static void requireNotBlankAndEnrichedMaxLength(String fieldName, String value, int maxLength) {
    if (value == null || value.trim().isEmpty()) {
      throw new FieldValidationException(EErrorCode.NOT_BLANK, fieldName);
    }

    String trimmed = value.trim();
    if (trimmed.length() > maxLength) {
      throw new FieldValidationException(EErrorCode.ENRICHED_TEXT_TOO_LONG, fieldName);
    }
  }

  public static void validateOptionalTextMaxLength(String fieldName, String value, int maxLength) {
    if (value == null) {
      return;
    }

    String trimmed = value.trim();
    if (trimmed.length() > maxLength) {
      throw new FieldValidationException(EErrorCode.TOO_LONG, fieldName);
    }
  }

  public static void validateOptionalEnrichedTextMaxLength(String fieldName, String value, int maxLength) {
    if (value == null) {
      return;
    }

    String trimmed = value.trim();
    if (trimmed.length() > maxLength) {
      throw new FieldValidationException(EErrorCode.ENRICHED_TEXT_TOO_LONG, fieldName);
    }
  }

  public static void validateUrl(String url) {
    if (url == null || url.trim().isEmpty()) {
      return;
    }
    try {
      URI uri = new java.net.URI(url);
      if (uri.getScheme() == null) throw new FieldValidationException(EErrorCode.NOT_URL, url);
      uri.toURL();
    } catch (Exception e) {
      throw new FieldValidationException(EErrorCode.NOT_URL, url);
    }
  }

  public static <T> void requireNotNull(String fieldName, T value) {
    if (value == null) {
      throw new FieldValidationException(EErrorCode.NOT_NULL, fieldName);
    }
  }

  public static void validateDateOrder(LocalDate startDate, LocalDate endDate) {

    if (endDate == null) {
      return;
    }

    if (startDate != null && endDate.isBefore(startDate)) {
      throw new FieldValidationException(EErrorCode.END_DATE_BEFORE_START_DATE);
    }
  }
}
