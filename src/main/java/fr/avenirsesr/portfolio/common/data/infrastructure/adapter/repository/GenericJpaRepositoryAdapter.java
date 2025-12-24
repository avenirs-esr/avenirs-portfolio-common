package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.repository;

import fr.avenirsesr.portfolio.common.data.domain.FetchGraph;
import fr.avenirsesr.portfolio.common.data.domain.model.AvenirsBaseModel;
import fr.avenirsesr.portfolio.common.data.domain.model.PageCriteria;
import fr.avenirsesr.portfolio.common.data.domain.model.PageInfo;
import fr.avenirsesr.portfolio.common.data.domain.model.PagedResult;
import fr.avenirsesr.portfolio.common.data.domain.port.output.repository.GenericRepositoryPort;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.EntityGrapher;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.mapper.Mapper;
import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model.AvenirsBaseEntity;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import fr.avenirsesr.portfolio.student.progress.imported.infrastructure.adapter.model.StudentProgressEntity;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public abstract class GenericJpaRepositoryAdapter<
        D extends AvenirsBaseModel, E extends AvenirsBaseEntity>
    implements GenericRepositoryPort<D> {
  protected final JpaRepository<E, UUID> jpaRepository;
  protected final JpaSpecificationExecutor<E> jpaSpecificationExecutor;
  protected final Class<E> entityClass;
  private final Mapper<E, D> mapper;
  @PersistenceContext private EntityManager em;

  protected GenericJpaRepositoryAdapter(
      JpaRepository<E, UUID> jpaRepository,
      JpaSpecificationExecutor<E> jpaSpecificationExecutor,
      Class<E> entityClass,
      Mapper<E, D> mapper) {
    this.jpaRepository = jpaRepository;
    this.jpaSpecificationExecutor = jpaSpecificationExecutor;
    this.entityClass = entityClass;
    this.mapper = mapper;
  }

  @Override
  public D save(D domain) {
    return mapper.toDomain(jpaRepository.save(mapper.fromDomain(domain)));
  }

  @Override
  public List<D> saveAll(List<D> domains) {
    return jpaRepository.saveAll(domains.stream().map(mapper::fromDomain).toList()).stream()
        .map(mapper::toDomain)
        .toList();
  }

  public void saveAllEntities(List<E> entities) {
    if (entities != null && !entities.isEmpty()) {
      jpaRepository.saveAll(entities);
    }
  }

  @Override
  public Optional<D> findById(UUID id) {
    E entity = jpaRepository.findById(id).orElse(null);
    return entity == null ? Optional.empty() : Optional.of(mapper.toDomain(entity));
  }

  @Override
  public Optional<D> findById(UUID id, FetchGraph fetchGraph) {
    var graph = EntityGrapher.from(fetchGraph, entityClass, em);

    var cb = em.getCriteriaBuilder();
    var cq = cb.createQuery(entityClass);
    var root = cq.from(entityClass);

    cq.select(root).where(cb.equal(root.get("id"), id));

    var query = em.createQuery(cq);
    query.setHint("jakarta.persistence.fetchgraph", graph.build());

    var result = query.getResultList();

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(mapper.toDomain(result.getFirst(), graph));
  }

  @Override
  public List<D> findAllById(List<UUID> ids) {
    return jpaRepository.findAllById(ids).stream().map(mapper::toDomain).toList();
  }

  protected List<D> findAllById(List<UUID> ids, FetchGraph fetchGraph) {

    if (ids == null || ids.isEmpty()) {
      return List.of();
    }

    var graph = EntityGrapher.from(fetchGraph, entityClass, em);

    var cb = em.getCriteriaBuilder();
    var cq = cb.createQuery(entityClass);
    var root = cq.from(entityClass);

    cq.select(root).where(root.get("id").in(ids)).distinct(true);

    var query = em.createQuery(cq);
    query.setHint("jakarta.persistence.fetchgraph", graph.build());

    return query.getResultList().stream().map(e -> mapper.toDomain(e, graph)).toList();
  }

  @Override
  public void removeFromDatabase(D domain) {
    jpaRepository.delete(mapper.fromDomain(domain));
  }

  @Override
  public void removeAllFromDatabase(List<D> domains) {
    jpaRepository.deleteAll(domains.stream().map(mapper::fromDomain).toList());
  }

  protected PagedResult<D> toPagedResult(Page<E> page) {
    var content = page.getContent().stream().map(mapper::toDomain).toList();

    return new PagedResult<>(
        content,
        new PageInfo(
            page.getPageable().getPageNumber(),
            page.getPageable().getPageSize(),
            page.getTotalElements()));
  }

  @Override
  public List<D> findAll() {
    return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
  }

  protected List<D> findAll(Specification<E> specification) {
    return jpaSpecificationExecutor.findAll(specification).stream().map(mapper::toDomain).toList();
  }

  protected List<D> findAll(Specification<E> specification, FetchGraph fetchGraph) {
    var graph = EntityGrapher.from(fetchGraph, entityClass, em);
    var cb = em.getCriteriaBuilder();
    var cq = cb.createQuery(entityClass);
    var root = cq.from(entityClass);

    Predicate predicate = specification.toPredicate(root, cq, cb);
    if (predicate != null) {
      cq.where(predicate);
    }

    cq.select(root).distinct(true);

    var query = em.createQuery(cq);
    query.setHint("jakarta.persistence.fetchgraph", graph.build());

    return query.getResultList().stream().map(e -> mapper.toDomain(e, graph)).toList();
  }

  protected PagedResult<D> findAll(Specification<E> specification, PageRequest pageRequest) {
    var page = jpaSpecificationExecutor.findAll(specification, pageRequest);

    return toPagedResult(page);
  }

  protected PagedResult<D> findAll(
      Specification<E> specification, PageRequest pageRequest, FetchGraph fetchGraph) {
    var graph = EntityGrapher.from(fetchGraph, entityClass, em);
    var cb = em.getCriteriaBuilder();

    var cq = cb.createQuery(entityClass);
    var root = cq.from(entityClass);

    Predicate predicate = specification.toPredicate(root, cq, cb);
    if (predicate != null) {
      cq.where(predicate);
    }

    if (pageRequest.getSort().isSorted()) {
      var orders =
          pageRequest.getSort().stream()
              .map(
                  order ->
                      order.isAscending()
                          ? cb.asc(root.get(order.getProperty()))
                          : cb.desc(root.get(order.getProperty())))
              .toList();
      cq.orderBy(orders);
    }

    cq.select(root).distinct(true);

    var query = em.createQuery(cq);
    query.setHint("jakarta.persistence.fetchgraph", graph.build());
    query.setFirstResult((int) pageRequest.getOffset());
    query.setMaxResults(pageRequest.getPageSize());

    var content = query.getResultList().stream().map(e -> mapper.toDomain(e, graph)).toList();

    return new PagedResult<>(
        content,
        new PageInfo(pageRequest.getPageNumber(), pageRequest.getPageSize(), content.size()));
  }
}
