package fr.avenirsesr.portfolio.common.file.infrastructure.adapter.repository;

import fr.avenirsesr.portfolio.common.data.infrastructure.adapter.repository.GenericJpaRepositoryAdapter;
import fr.avenirsesr.portfolio.common.file.domain.model.File;
import fr.avenirsesr.portfolio.common.file.domain.port.output.repository.FileRepository;
import fr.avenirsesr.portfolio.common.file.infrastructure.adapter.mapper.FileMapper;
import fr.avenirsesr.portfolio.common.file.infrastructure.adapter.model.FileEntity;
import org.springframework.stereotype.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Part of the shared file registry, wired only where {@code file.registry.enabled} is true.
 *
 * <p>Every service embeds the sources of this library, including the ones it has no use for. Gating
 * the registry keeps a service that owns no file from having to declare a storage backend, an
 * uploader provider or a file table just to start up.
 */
@ConditionalOnProperty(name = "file.registry.enabled", havingValue = "true")
@Repository
public class FileDatabaseRepository extends GenericJpaRepositoryAdapter<File, FileEntity>
    implements FileRepository {
  public FileDatabaseRepository(FileJpaRepository jpaRepository) {
    super(jpaRepository, jpaRepository, FileEntity.class, FileMapper.INSTANCE);
  }
}
