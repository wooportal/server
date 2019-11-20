package de.codeschluss.wooportal.server.components.provider;

import de.codeschluss.wooportal.server.core.repository.DataRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface ProviderRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface ProviderRepository extends DataRepository<ProviderEntity> {
  
}
