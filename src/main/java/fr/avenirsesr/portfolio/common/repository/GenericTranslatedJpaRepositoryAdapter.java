package fr.avenirsesr.portfolio.common.repository;

import fr.avenirsesr.portfolio.common.data.domain.model.AvenirsBaseModel;
import fr.avenirsesr.portfolio.common.data.domain.model.PageInfo;
import fr.avenirsesr.portfolio.common.data.domain.model.PagedResult;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model.AvenirsBaseEntity;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.repository.GenericJpaRepositoryAdapter;
import fr.avenirsesr.portfolio.common.error.domain.exception.UnsupportedTranslationMappingException;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.model.TranslationEntity;
import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.utils.TranslationUtil;
import fr.avenirsesr.portfolio.common.specification.TranslationSpecification;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Slf4j
public abstract class GenericTranslatedJpaRepositoryAdapter<
        D extends AvenirsBaseModel, E extends AvenirsBaseEntity, T extends TranslationEntity>
        extends GenericJpaRepositoryAdapter<D, E> {

    protected final JpaRepository<T, UUID> jpaTranslationRepository;
    protected final JpaSpecificationExecutor<T> jpaTranslationSpecificationExecutor;
    private final Function<T, D> toDomain;

    protected GenericTranslatedJpaRepositoryAdapter(
            JpaRepository<E, UUID> jpaRepository,
            JpaSpecificationExecutor<E> jpaSpecificationExecutor,
            JpaRepository<T, UUID> jpaTranslationRepository,
            JpaSpecificationExecutor<T> jpaTranslationSpecificationExecutor,
            Function<D, E> fromDomain,
            Function<E, D> entityToDomain,
            Function<T, D> toDomain) {

        super(
                jpaRepository,
                jpaSpecificationExecutor,
                fromDomain,
                entityToDomain);
        this.jpaTranslationRepository = jpaTranslationRepository;
        this.jpaTranslationSpecificationExecutor = jpaTranslationSpecificationExecutor;
        this.toDomain = toDomain;
    }

    protected List<D> mapToDomain(List<T> entities) {
        return entities.stream().map(toDomain).toList();
    }

    protected D mapToDomain(T entity) {
        return toDomain.apply(entity);
    }

    protected PagedResult<D> mapToPagedResult(Page<T> page) {
        List<D> content = mapToDomain(page.getContent());

        return new PagedResult<>(
                content,
                new PageInfo(
                        page.getPageable().getPageNumber(),
                        page.getPageable().getPageSize(),
                        page.getTotalElements()));
    }

    private void loggingFallBack(
            ELanguage language, int resultsSize, int resultsFallBackLanguageSize) {
        log.warn(
                "Translation fallback activated: number of results for language {} ({}) differs from fallback language {} ({})",
                language,
                resultsSize,
                ELanguage.FALLBACK,
                resultsFallBackLanguageSize);
    }

    private Specification<T> createSpecification(Specification<T> specification, ELanguage language) {
        return Specification.where(specification).and(TranslationSpecification.hasLanguage(language));
    }

    protected List<D> findAllByTranslation(Specification<T> specification) {
        ELanguage language = TranslationUtil.getRequestLanguage();

        Specification<T> specificationWithLanguage = createSpecification(specification, language);
        Specification<T> specificationWithFallBackLanguage =
                createSpecification(specification, ELanguage.FALLBACK);

        List<T> results = jpaTranslationSpecificationExecutor.findAll(specificationWithLanguage);
        List<T> resultsFallBackLanguage =
                jpaTranslationSpecificationExecutor.findAll(specificationWithFallBackLanguage);

        if (results.size() != resultsFallBackLanguage.size()) {
            loggingFallBack(language, results.size(), resultsFallBackLanguage.size());
            return mapToDomain(resultsFallBackLanguage);
        }
        return mapToDomain(results);
    }

    protected PagedResult<D> findAllByTranslation(
            Specification<T> specification, PageRequest pageRequest) {
        ELanguage language = TranslationUtil.getRequestLanguage();

        Specification<T> specificationWithLanguage = createSpecification(specification, language);
        Specification<T> specificationWithFallBackLanguage =
                createSpecification(specification, ELanguage.FALLBACK);

        Page<T> results =
                jpaTranslationSpecificationExecutor.findAll(specificationWithLanguage, pageRequest);
        Page<T> resultsFallBackLanguage =
                jpaTranslationSpecificationExecutor.findAll(specificationWithFallBackLanguage, pageRequest);

        if (results.getContent().size() != resultsFallBackLanguage.getContent().size()) {
            loggingFallBack(
                    language, results.getContent().size(), resultsFallBackLanguage.getContent().size());
            return mapToPagedResult(resultsFallBackLanguage);
        }
        return mapToPagedResult(results);
    }
}
