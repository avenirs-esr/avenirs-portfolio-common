package fr.avenirsesr.portfolio.common.language.infrastructure.adapter.model;

import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model.AvenirsBaseEntity;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class TranslationEntity extends AvenirsBaseEntity {
  @Column(name = "language", nullable = false)
  @Enumerated(EnumType.STRING)
  protected ELanguage language;
}
