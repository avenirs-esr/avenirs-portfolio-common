package fr.avenirsesr.portfolio.common.validation.infrastructure.adapter.utils;

import java.util.Collection;

public class ValidationUtils {
  public static <T> void requireNonEmpty(Collection<T> collection, String message) {
    if (collection == null || collection.isEmpty()) {
      throw new IllegalArgumentException(message);
    }
  }
}
