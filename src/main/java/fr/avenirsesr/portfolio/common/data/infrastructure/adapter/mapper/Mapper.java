package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.mapper;

import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.EntityGrapher;

public interface Mapper<E, D> {
    E fromDomain(D domain);
    D toDomain(E entity);

    default D toDomain(E entity, EntityGrapher<?> graph) {
        return toDomain(entity); // fallback
    }
}
