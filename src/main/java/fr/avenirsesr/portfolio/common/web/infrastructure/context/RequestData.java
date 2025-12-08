package fr.avenirsesr.portfolio.common.web.infrastructure.context;

import fr.avenirsesr.portfolio.common.data.domain.model.User;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import java.util.Optional;

public record RequestData(Optional<User> userLoggedIn, ELanguage preferredLanguage) {}
