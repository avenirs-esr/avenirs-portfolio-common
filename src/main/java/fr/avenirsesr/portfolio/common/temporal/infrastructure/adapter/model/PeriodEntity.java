package fr.avenirsesr.portfolio.common.temporal.infrastructure.adapter.model;

import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.model.AvenirsBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.temporal.Temporal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class PeriodEntity<T extends Temporal> extends AvenirsBaseEntity {
  @Column(name = "start_date", nullable = false)
  protected T startDate;

  @Column(name = "end_date", nullable = false)
  protected T endDate;
}
