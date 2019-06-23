package de.codeschluss.wooportal.server.components.user;

import org.springframework.stereotype.Repository;

import de.codeschluss.wooportal.server.core.repository.DataRepository;

/**
 * The Interface UserRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface UserRepository extends DataRepository<UserEntity> {

}
