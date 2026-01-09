package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model;

import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.model.TranslationEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public abstract class TranslatableEntity<T extends TranslationEntity> extends AvenirsBaseEntity {
  @Transient protected Set<T> translations = new HashSet<>();
}
