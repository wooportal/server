package de.codeschluss.wooportal.server.components.user;

import de.codeschluss.wooportal.server.core.repository.DataRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface UserRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface UserRepository extends DataRepository<UserEntity> {

}
