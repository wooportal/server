package de.codeschluss.wooportal.server.core.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The Interface DataRepository.
 * 
 * @author Valmir Etemi
 *
 */
@NoRepositoryBean
public interface DataRepository<T>
    extends CrudRepository<T, String>, QueryableReadRepository<T> {
  
  @Override
  List<T> findAll();
}
