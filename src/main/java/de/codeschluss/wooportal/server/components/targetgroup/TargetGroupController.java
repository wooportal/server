package de.codeschluss.wooportal.server.components.targetgroup;

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
 * The Class TargetGroupController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class TargetGroupController extends CrudController<TargetGroupEntity, TargetGroupService> {

  /** The translation service. */
  private final TranslationService translationService;
  
  public TargetGroupController(
      TargetGroupService service,
      TranslationService translationService) {
    super(service);
    this.translationService = translationService;
  }

  @Override
  @GetMapping("/targetgroups")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/targetgroups/{id}")
  public Resource<TargetGroupEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/targetgroups")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody TargetGroupEntity newTargetGroup)
      throws Exception {
    return super.create(newTargetGroup);
  }

  @Override
  @PutMapping("/targetgroups/{id}")
  @SuperUserPermission
  public ResponseEntity<?> update(
      @RequestBody TargetGroupEntity newTargetGroup,
      @PathVariable String id) throws URISyntaxException {
    return super.update(newTargetGroup, id);
  }

  @Override
  @DeleteMapping("/targetgroups/{id}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }
  
  /**
   * Read translations.
   *
   * @param id the target group id
   * @return the response entity
   */
  @GetMapping("/targetgroups/{id}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String id) {
    try {
      return ok(translationService.getAllTranslations(service.getById(id)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
