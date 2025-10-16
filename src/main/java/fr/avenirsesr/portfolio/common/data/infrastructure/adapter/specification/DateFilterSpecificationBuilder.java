package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.specification;

import fr.avenirsesr.portfolio.common.data.domain.model.enums.EDateFilterKey;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class DateFilterSpecificationBuilder<T>
    extends FilterSpecificationBuilder<T, EDateFilterKey> {
  @Override
  public Specification<T> getSpecification(EDateFilterKey key, Object value) {
    return switch (key) {
      case FROM_DATE -> fromDate((LocalDate) value);
      case TO_DATE -> toDate((LocalDate) value);
    };
  }

  private Specification<T> fromDate(LocalDate date) {
    return (root, query, cb) ->
        date == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), date.atStartOfDay());
  }

  private Specification<T> toDate(LocalDate date) {
    return (root, query, cb) ->
        date == null ? null : cb.lessThan(root.get("createdAt"), date.plusDays(1).atStartOfDay());
  }
}
