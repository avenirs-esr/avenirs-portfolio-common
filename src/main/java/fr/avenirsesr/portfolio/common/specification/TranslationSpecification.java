package fr.avenirsesr.portfolio.common.specification;

import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.model.TranslationEntity;
import org.springframework.data.jpa.domain.Specification;

public class TranslationSpecification {
    public static <T extends TranslationEntity> Specification<T> hasLanguage(ELanguage language) {
        return (root, query, cb) -> cb.equal(root.get("language"), language);
    }
}
