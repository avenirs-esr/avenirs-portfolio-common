package fr.avenirsesr.portfolio.common.security.infrastructure.adapter.model;

public class AvenirsSecurityHeaders {
  public static final String API_KEY = "X-API-Key";
  public static final String CONTEXT_SIGNATURE = "X-Context-Signature";
  public static final String CONTEXT_KID = "X-Context-Kid";
  public static final String SIGNED_CONTEXT = "X-Signed-Context";

  private AvenirsSecurityHeaders() {}
}
