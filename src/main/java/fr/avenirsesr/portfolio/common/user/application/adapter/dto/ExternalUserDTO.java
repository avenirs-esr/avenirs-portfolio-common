package fr.avenirsesr.portfolio.common.user.application.adapter.dto;

import fr.avenirsesr.portfolio.common.data.domain.model.enums.EUserCategory;
import fr.avenirsesr.portfolio.common.user.domain.model.enums.EUserStatus;

public record ExternalUserDTO(
    String eppn,
    String firstName,
    String lastName,
    String email,
    EUserCategory category,
    String externalId,
    String source,
    EUserStatus status) {

  public boolean isActive() {
    return EUserStatus.ACTIVE.equals(status);
  }
}
