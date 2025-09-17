package fr.avenirsesr.portfolio.common.web.infrastructure.context;

import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;

public record RequestData(ELanguage preferredLanguage) {}
