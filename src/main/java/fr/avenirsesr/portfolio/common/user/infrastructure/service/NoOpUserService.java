package fr.avenirsesr.portfolio.common.user.infrastructure.service;

import fr.avenirsesr.portfolio.common.data.domain.model.User;
import fr.avenirsesr.portfolio.common.user.domain.port.output.BaseUserService;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnMissingBean(BaseUserService.class)
public class NoOpUserService implements BaseUserService {
  @Override
  public User getUser(UUID userId) {
    return null;
  }
}
