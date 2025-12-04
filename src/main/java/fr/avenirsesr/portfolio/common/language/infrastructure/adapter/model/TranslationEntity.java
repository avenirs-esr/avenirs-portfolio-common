package fr.avenirsesr.portfolio.common.language.infrastructure.adapter.model;

import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model.AvenirsBaseEntity;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.filter.LanguageFilterSupplier;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@MappedSuperclass
@FilterDef(
    name = "langFilter",
    parameters =
        @ParamDef(name = "lang", type = String.class, resolver = LanguageFilterSupplier.class),
    autoEnabled = true,
    defaultCondition = "language in (:lang, 'FRENCH')")
public abstract class TranslationEntity extends AvenirsBaseEntity {
  @Column(name = "language", nullable = false)
  @Enumerated(EnumType.STRING)
  protected ELanguage language;
}
