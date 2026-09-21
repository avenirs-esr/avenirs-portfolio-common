package fr.avenirsesr.portfolio.common.user.application.adapter.dto;

import fr.avenirsesr.portfolio.common.data.domain.model.enums.EUserCategory;
import fr.avenirsesr.portfolio.common.user.domain.model.enums.EUserStatus;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ExternalUserDTO(
    String eppn,
    String firstName,
    String lastName,
    String email,
    Set<EUserCategory> categories,
    String externalId,
    String source,
    List<UUID> institutionIds,
    List<UUID> groupIds,
    EUserStatus status) {

  public boolean isActive() {
    return EUserStatus.ACTIVE.equals(status);
  }
}
