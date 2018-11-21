package de.codeschluss.portal.components.category;

import com.querydsl.core.types.Predicate;

import de.codeschluss.portal.core.common.DataService;
import de.codeschluss.portal.core.exception.NotFoundException;

import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryService.
 */
@Service
public class CategoryService extends DataService<CategoryEntity, QCategoryEntity> {

  /** The default sort prop. */
  protected final String defaultSortProp = "name";

  /**
   * Instantiates a new category service.
   *
   * @param repo
   *          the repo
   * @param assembler
   *          the assembler
   */
  public CategoryService(
      CategoryRepository repo, 
      CategoryResourceAssembler assembler) {
    super(repo, assembler, QCategoryEntity.categoryEntity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.codeschluss.portal.core.common.DataService#getExisting(de.codeschluss.
   * portal.core.common.BaseEntity)
   */
  public CategoryEntity getExisting(CategoryEntity newCategory) {
    return repo.findOne(query.name.eq(newCategory.getName())).orElse(null);
  }

  /**
   * Gets the resource by activity.
   *
   * @param activityId
   *          the activity id
   * @return the resource by activity
   */
  public Resource<CategoryEntity> getResourceByActivity(String activityId) {
    CategoryEntity category = repo.findOne(query.activities.any().id.eq(activityId))
        .orElseThrow(() -> new NotFoundException(activityId));
    return assembler.toResource(category);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.codeschluss.portal.core.common.DataService#update(java.lang.String,
   * de.codeschluss.portal.core.common.BaseEntity)
   */
  @Override
  public CategoryEntity update(String id, CategoryEntity newCategory) {
    return repo.findById(id).map(category -> {
      category.setName(newCategory.getName());
      category.setDescription(newCategory.getDescription());
      category.setColor(newCategory.getColor());
      return repo.save(category);
    }).orElseGet(() -> {
      newCategory.setId(id);
      return repo.save(newCategory);
    });
  }

  @Override
  protected Predicate getFilteredPredicate(String filter) {
    return query.name.likeIgnoreCase(filter)
        .or(query.description.likeIgnoreCase(filter));
  }
}
