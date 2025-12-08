package fr.avenirsesr.portfolio.common.user.domain.port.output;

import fr.avenirsesr.portfolio.common.data.domain.model.User;
import java.util.UUID;

public interface BaseUserService {
  User getUser(UUID userId);
}
