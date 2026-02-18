package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.repository;

import fr.avenirsesr.portfolio.common.data.domain.model.AvenirsBaseModel;
import fr.avenirsesr.portfolio.common.data.domain.port.output.repository.GenericTranslatableRepositoryPort;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.mapper.Mapper;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model.TranslatableEntity;
import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.model.TranslationEntity;
import fr.avenirsesr.portfolio.common.language.infrastructure.adapter.utils.TranslationUtil;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public abstract class GenericTranslatableJpaRepositoryAdapter<
        D extends AvenirsBaseModel, T extends TranslationEntity, E extends TranslatableEntity<T>>
    extends GenericJpaRepositoryAdapter<D, E> implements GenericTranslatableRepositoryPort<D> {
  protected final JpaRepository<E, UUID> jpaRepository;
  protected final JpaSpecificationExecutor<E> jpaSpecificationExecutor;
  private final Mapper<E, D> mapper;
  TranslationUtil.CreationFunction<AvenirsBaseModel, E, ELanguage, T> create;
  TranslationUtil.UpdateFunction<TranslationEntity, AvenirsBaseModel> update;

  protected GenericTranslatableJpaRepositoryAdapter(
      JpaRepository<E, UUID> jpaRepository,
      JpaSpecificationExecutor<E> jpaSpecificationExecutor,
      Class<E> entityClass,
      Mapper<E, D> mapper,
      TranslationUtil.CreationFunction<AvenirsBaseModel, E, ELanguage, T> create,
      TranslationUtil.UpdateFunction<TranslationEntity, AvenirsBaseModel> update) {
    super(jpaRepository, jpaSpecificationExecutor, entityClass, mapper);
    this.jpaRepository = jpaRepository;
    this.jpaSpecificationExecutor = jpaSpecificationExecutor;
    this.mapper = mapper;
    this.create = create;
    this.update = update;
  }

  @Override
  public D save(D domain) {
    var entity = jpaRepository.findById(domain.getId());
    if (entity.isPresent()) {
      TranslationUtil.addTranslation(domain, entity.get(), create, update);
      jpaRepository.save(entity.get());
      return mapper.toDomain(entity.get());
    } else return mapper.toDomain(jpaRepository.save(mapper.fromDomain(domain)));
  }
}
