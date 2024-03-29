package de.codeschluss.wooportal.server.core.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.naming.ServiceUnavailableException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.repository.DataRepository;



// TODO: Auto-generated Javadoc
/**
 * The Class DataService.
 *
 * @author Valmir Etemi
 * 
 * @param <E>          the element type
 * @param <B> the generic type
 */
public abstract class DataService<E extends BaseEntity, B extends QueryBuilder<?>> {

  /** The repo. */
  protected final DataRepository<E> repo;
  
  /** The entities. */
  protected final B entities;

  /**
   * Instantiates a new data service.
   *
   * @param repo          the repo
   * @param entities the entities
   */
  public DataService(
      DataRepository<E> repo,
      B entities) {
    this.repo = repo;
    this.entities = entities;
  }

  /**
   * Exists by id.
   *
   * @param id
   *          the address id
   * @return true, if successful
   */
  public boolean existsById(String id) {
    return repo.exists(entities.withId(id));
  }
  
  /**
   * Gets the existing.
   *
   * @param newEntity
   *          the new entity
   * @return the existing
   */
  public abstract E getExisting(E newEntity);
  
  /**
   * Validate field constraints.
   *
   * @param newEntity the new entity
   * @return true, if successful
   */
  public abstract boolean validCreateFieldConstraints(E newEntity);
  
  /**
   * Valid update field constraints.
   *
   * @param newEntity the new entity
   * @return true, if successful
   */
  public abstract boolean validUpdateFieldConstraints(E newEntity);

  /**
   * Gets the by id.
   *
   * @param id
   *          the id
   * @return the by id
   */
  public E getById(String id) {
    return repo.findOne(entities.withId(id)).orElseThrow(() -> new NotFoundException(id));
  }

  /**
   * Gets the by ids.
   *
   * @param entityIds
   *          the entity ids
   * @return the by ids
   */
  public List<E> getByIds(List<String> entityIds) {
    List<E> result = repo.findAll(entities.withIdsIn(entityIds));
    
    if (result == null || result.isEmpty()) {
      throw new NotFoundException(entityIds.toString());
    }
    
    return result;
  }

  /**
   * Adds the all.
   *
   * @param newEntities
   *          the new entities
   * @return the list<
   */
  public List<E> addAll(List<E> newEntities) {
    return newEntities.stream().map(entity -> {
      try {
        return add(entity);
      } catch (ServiceUnavailableException e) {
        return null;
      }
    }).collect(Collectors.toList());
  }
  
  /**
   * Adds the.
   *
   * @param newEntity the new entity
   * @return the e
   * @throws ServiceUnavailableException the service unavailable exception
   */
  public E add(E newEntity) throws ServiceUnavailableException {
    E duplicate = getExisting(newEntity);
    return duplicate != null ? duplicate : repo.save(newEntity);
  }

  /**
   * Update.
   *
   * @param id
   *          the id
   * @param updatedEntity
   *          the updated entity
   * @return the e
   */
  public abstract E update(String id, E updatedEntity);

  /**
   * Delete.
   *
   * @param id
   *          the id
   */
  public void delete(String id) {
    repo.delete(getById(id));
  }
  

  /**
   * Delete all.
   *
   * @param ids the ids
   */
  public void deleteAll(List<String> ids) {
    ids.forEach(id -> repo.deleteById(id));
  }
  
  /**
   * Delete existing.
   *
   * @param entity the entity
   */
  public void deleteExisting(E entity) {
    if (entity != null && existsById(entity.getId())) {
      delete(entity.getId());
    }
  }

  /**
   * Gets the sorted list.
   *
   * @param <P> the generic type
   * @param params the params
   * @return the sorted list
   */
  public <P extends FilterSortPaginate> List<E> getSortedList(P params) {
    Sort sort = entities.createSort(params);
    List<E> result = params.isEmptyQuery() && !entities.localized()
        ? repo.findAll(sort)
        : repo.findAll(entities.search(params), sort);
    
    if (result == null || result.isEmpty()) {
      throw new NotFoundException(params.toString());
    }
    
    return result;
  }
  
  /**
   * Find all.
   *
   * @return the list
   */
  public List<E> getAll() {
    return repo.findAll();
  }

  /**
   * Gets the paged.
   *
   * @param <P> the generic type
   * @param params the params
   * @return the paged
   */
  public <P extends FilterSortPaginate> Page<E> getPaged(P params) {
    PageRequest page =
        PageRequest.of(params.getPage(), params.getSize(), entities.createSort(params));

    Page<E> paged = params.isEmptyQuery() && !entities.localized() ? repo.findAll(page)
        : repo.findAll(entities.search(params), page);

    if (paged == null || paged.isEmpty()) {
      throw new NotFoundException(params.toString());
    }

    return paged;
  }
}

