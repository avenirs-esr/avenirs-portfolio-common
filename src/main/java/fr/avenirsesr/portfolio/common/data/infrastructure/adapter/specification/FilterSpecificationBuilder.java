package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.specification;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

public abstract class FilterSpecificationBuilder<T, K extends Enum<K>> {
  public Optional<Specification<T>> build(Map<K, Object> filters) {
    Specification<T> spec = null;

    for (var entry : filters.entrySet()) {
      Specification<T> s = getSpecification(entry.getKey(), entry.getValue());
      if (s != null) {
        spec = spec == null ? Specification.where(s) : spec.and(s);
      }
    }

    return Optional.ofNullable(spec);
  }

  protected abstract Specification<T> getSpecification(K key, Object value);
}
