package fr.avenirsesr.portfolio.common.utils;

public final class RedirectUtils {
  private static final String DEFAULT_REDIRECT_PATH = "/cofolio/student";
  private static final String DEFAULT_HOST = "localhost";

  private RedirectUtils() {}

  public static String toSafeRelativePath(String value) {
    return toSafeRelativePath(value, DEFAULT_REDIRECT_PATH);
  }

  public static String toSafeRelativePath(String value, String fallback) {
    if (value == null || value.isBlank()) {
      return fallback;
    }

    if (!value.startsWith("/")) {
      return fallback;
    }

    if (value.startsWith("//")) {
      return fallback;
    }

    return value;
  }

  public static String toSafeHost(String host) {
    return toSafeHost(host, DEFAULT_HOST);
  }

  public static String toSafeHost(String host, String fallback) {
    return host == null || host.isBlank() ? fallback : host;
  }
}
