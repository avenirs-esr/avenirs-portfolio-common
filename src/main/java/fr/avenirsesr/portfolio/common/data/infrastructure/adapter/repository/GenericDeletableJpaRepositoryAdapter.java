package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.repository;

import fr.avenirsesr.portfolio.common.data.domain.model.DeletableAvenirsBaseModel;
import fr.avenirsesr.portfolio.common.data.domain.port.output.repository.GenericDeletableRepositoryPort;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.mapper.Mapper;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model.DeletableAvenirsBaseEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public abstract class GenericDeletableJpaRepositoryAdapter<
        D extends DeletableAvenirsBaseModel, E extends DeletableAvenirsBaseEntity>
    extends GenericJpaRepositoryAdapter<D, E> implements GenericDeletableRepositoryPort<D> {
  protected final JpaRepository<E, UUID> jpaRepository;
  protected final JpaSpecificationExecutor<E> jpaSpecificationExecutor;
  private final Mapper<E, D> mapper;

  protected GenericDeletableJpaRepositoryAdapter(
      JpaRepository<E, UUID> jpaRepository,
      JpaSpecificationExecutor<E> jpaSpecificationExecutor,
      Class<E> entityClass,
      Mapper<E, D> mapper) {
    super(jpaRepository, jpaSpecificationExecutor, entityClass, mapper);
    this.jpaRepository = jpaRepository;
    this.jpaSpecificationExecutor = jpaSpecificationExecutor;
    this.mapper = mapper;
  }

  @Override
  public void delete(D domain) {
    jpaRepository.delete(mapper.fromDomain(domain));
  }
}
