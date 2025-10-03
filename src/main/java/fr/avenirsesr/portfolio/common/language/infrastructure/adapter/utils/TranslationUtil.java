package fr.avenirsesr.portfolio.common.language.infrastructure.adapter.utils;

import fr.avenirsesr.portfolio.common.language.domain.exception.LanguageException;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.model.TranslationEntity;
import fr.avenirsesr.portfolio.common.web.infrastructure.context.RequestContext;
import fr.avenirsesr.portfolio.common.web.infrastructure.context.RequestData;
import java.util.Optional;
import java.util.Set;

public interface TranslationUtil {
    static ELanguage getRequestLanguage() {
        return Optional.ofNullable(RequestContext.get())
                .map(RequestData::preferredLanguage)
                .orElse(ELanguage.FALLBACK);
    }

  static <T extends TranslationEntity> T getTranslation(Set<T> translations) {
    var preferredLanguage =
        Optional.ofNullable(RequestContext.get())
            .map(RequestData::preferredLanguage)
            .orElse(ELanguage.FALLBACK);

    return getTranslation(translations, preferredLanguage);
  }

  static <T extends TranslationEntity> T getTranslation(
      Set<T> translations, ELanguage selectedLanguage) {
    return translations.stream()
        .filter(t -> t.getLanguage().equals(selectedLanguage))
        .findFirst()
        .orElseThrow(
            () ->
                new LanguageException(
                    String.format("Fallback language [%s] not setup", ELanguage.FALLBACK)));
  }
}
