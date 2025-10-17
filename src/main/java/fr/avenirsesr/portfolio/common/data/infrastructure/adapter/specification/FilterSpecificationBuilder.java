package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.specification;

import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

public abstract class FilterSpecificationBuilder<T, K extends Enum<K>> {
  public Specification<T> build(Map<K, Object> filters) {
    Specification<T> spec = Specification.where(null);

    for (var entry : filters.entrySet()) {
      Specification<T> s = getSpecification(entry.getKey(), entry.getValue());
      if (s != null) {
        spec = spec.and(s);
      }
    }

    return spec;
  }

  protected abstract Specification<T> getSpecification(K key, Object value);
}
