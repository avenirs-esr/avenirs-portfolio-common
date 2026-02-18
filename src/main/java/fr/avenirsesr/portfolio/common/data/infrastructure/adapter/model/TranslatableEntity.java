package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model;

import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.model.TranslationEntity;
import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class TranslatableEntity<T extends TranslationEntity> extends AvenirsBaseEntity {
  @Transient protected Set<T> translations = new HashSet<>();
}
