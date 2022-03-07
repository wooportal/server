package de.codeschluss.wooportal.server.components.category;

import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class LanguageController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class CategoryController extends CrudController<CategoryEntity, CategoryService> {
  
  /** The translation service. */
  private final TranslationService translationService;

  public CategoryController(
      CategoryService service,
      TranslationService translationService) {
    super(service);
    this.translationService = translationService;
  }

  @Override
  @GetMapping("/categories")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/categories/{id}")
  public Resource<CategoryEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/categories")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody CategoryEntity newCategory) 
      throws Exception {
    return super.create(newCategory);
  }

  @Override
  @PutMapping("/categories/{id}")
  @SuperUserPermission
  public ResponseEntity<?> update(@RequestBody CategoryEntity newCategory,
      @PathVariable String id) throws URISyntaxException {
    return super.update(newCategory, id);
  }

  @Override
  @DeleteMapping("/categories/{id}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }
  
  /**
   * Read translations.
   *
   * @param id the category id
   * @return the response entity
   */
  @GetMapping("/categories/{id}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String id) {
    try {
      return ok(translationService.getAllTranslations(service.getById(id)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
