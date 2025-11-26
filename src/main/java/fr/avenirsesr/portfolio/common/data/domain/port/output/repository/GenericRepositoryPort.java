package fr.avenirsesr.portfolio.common.data.domain.port.output.repository;

import fr.avenirsesr.portfolio.common.data.domain.model.AvenirsBaseModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenericRepositoryPort<D extends AvenirsBaseModel> {
  List<D> findAll();

  Optional<D> findById(UUID id);

  List<D> findAllById(List<UUID> ids);

  D save(D domain);

  List<D> saveAll(List<D> collection);

  void flush();

  void removeFromDatabase(D domain);

  void removeAllFromDatabase(List<D> domains);
}
