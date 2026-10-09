package fr.avenirsesr.portfolio.common.file.domain.port.output.service;

import java.util.UUID;

/**
 * Tells the registry who is uploading.
 *
 * <p>Each service resolves its own notion of a current user — the portfolio api reads it from the
 * request context, the back office from the authenticated external user — and the registry only
 * ever holds the resulting identifier. This is what lets the same registry run in a service that
 * owns no user table.
 */
public interface FileUploaderProvider {
  UUID currentUploaderId();
}
