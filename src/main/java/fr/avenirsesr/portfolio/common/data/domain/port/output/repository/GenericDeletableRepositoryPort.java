package fr.avenirsesr.portfolio.common.data.domain.port.output.repository;

import fr.avenirsesr.portfolio.common.data.domain.model.DeletableAvenirsBaseModel;

public interface GenericDeletableRepositoryPort<D extends DeletableAvenirsBaseModel>
    extends GenericRepositoryPort<D> {
  void delete(D domain);
}
