package fr.avenirsesr.portfolio.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller for security configuration tests. Provides endpoints to test permit-all and
 * protected paths.
 */
@RestController
public class TestController {

  @GetMapping("/avenirs-portfolio-api/api-docs")
  public ResponseEntity<String> apiDocs() {
    return ResponseEntity.ok("API Documentation");
  }

  @GetMapping("/avenirs-portfolio-api/swagger-ui")
  public ResponseEntity<String> swaggerUi() {
    return ResponseEntity.ok("Swagger UI");
  }

  @GetMapping("/favicon.ico")
  public ResponseEntity<String> favicon() {
    return ResponseEntity.ok("Favicon");
  }

  @GetMapping("/actuator/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Health Check");
  }

  @GetMapping("/photo")
  public ResponseEntity<String> photo() {
    return ResponseEntity.ok("Photo endpoint");
  }

  @GetMapping("/cover")
  public ResponseEntity<String> cover() {
    return ResponseEntity.ok("Cover endpoint");
  }

  @GetMapping("/api/some-protected-endpoint")
  public ResponseEntity<String> protectedEndpoint() {
    return ResponseEntity.ok("Protected endpoint");
  }
}
