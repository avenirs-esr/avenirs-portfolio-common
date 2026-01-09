package fr.avenirsesr.portfolio.common.language.infrastructure.adapter.utils;

import fr.avenirsesr.portfolio.common.data.domain.model.AvenirsBaseModel;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model.TranslatableEntity;
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

  @FunctionalInterface
  interface CreationFunction<A, B, C, R> {
    R apply(A a, B b, C c);
  }

  @FunctionalInterface
  interface UpdateFunction<T, D> {
    T apply(T t, D d);
  }

  static <T extends TranslationEntity, E extends TranslatableEntity<T>> void addTranslation(
      AvenirsBaseModel model,
      E entity,
      ELanguage language,
      CreationFunction<AvenirsBaseModel, E, ELanguage, T> create,
      UpdateFunction<TranslationEntity, AvenirsBaseModel> update) {

    var translationOptional =
        entity.getTranslations().stream().filter(t -> t.getLanguage() == language).findAny();

    if (translationOptional.isPresent()) {
      update.apply(translationOptional.get(), model);
    } else {
      entity.getTranslations().add(create.apply(model, entity, language));
    }
  }

  static <T extends TranslationEntity, E extends TranslatableEntity<T>> void addTranslation(
      AvenirsBaseModel model,
      E entity,
      CreationFunction<AvenirsBaseModel, E, ELanguage, T> create,
      UpdateFunction<TranslationEntity, AvenirsBaseModel> update) {
    addTranslation(
        model,
        entity,
        Optional.ofNullable(RequestContext.get())
            .map(RequestData::preferredLanguage)
            .orElse(ELanguage.FALLBACK),
        create,
        update);
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
        .orElseGet(
            () ->
                translations.stream()
                    .filter(t -> t.getLanguage().equals(ELanguage.FALLBACK))
                    .findFirst()
                    .orElseThrow(
                        () ->
                            new LanguageException(
                                String.format(
                                    "Fallback language [%s] not setup", ELanguage.FALLBACK))));
  }
}
