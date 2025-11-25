package fr.avenirsesr.portfolio.common.data.infrastructure.adapter.specification;

import fr.avenirsesr.portfolio.user.infrastructure.adapter.model.StudentEntity;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public final class SharedSpecification {
  public static <T> Specification<T> hasStudent(StudentEntity student) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("student"), student);
  }
}
