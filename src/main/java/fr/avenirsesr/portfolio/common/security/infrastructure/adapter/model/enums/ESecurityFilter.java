package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model.enums;

/** Enum representing the available security filter types for authentication. */
public enum ESecurityFilter {
  /** HMAC-based authentication filter (production use). */
  HMAC("hmac"),

  /** Development authentication filter (accepts user-id header). */
  DEV("dev"),

  /** API Key authentication filter (validates X-API-Key header). */
  API_KEY("api-key"),

  /** API key for internal calls, hmac otherwise. */
  API_KEY_OR_HMAC("api-key-or-hmac"),

  /** Disabled authentication filter (no authentication performed). */
  DISABLED("disabled");

  private final String value;

  ESecurityFilter(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  /**
   * Parse string value to enum.
   *
   * @param value the string value
   * @return the corresponding enum
   * @throws IllegalArgumentException if value is not recognized
   */
  public static ESecurityFilter fromValue(String value) {
    for (ESecurityFilter filter : values()) {
      if (filter.value.equals(value)) {
        return filter;
      }
    }
    throw new IllegalArgumentException("Unknown security filter: " + value);
  }
}
