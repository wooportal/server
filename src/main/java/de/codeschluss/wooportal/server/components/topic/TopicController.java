package de.codeschluss.wooportal.server.components.topic;

import static org.springframework.http.ResponseEntity.ok;
import de.codeschluss.wooportal.server.components.blog.BlogService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
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
 * The Class TopicController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class TopicController extends CrudController<TopicEntity, TopicService> {
  
  private final BlogService blogService;
  
  /** The translation service. */
  private final TranslationService translationService;

  public TopicController(
      BlogService blogService,
      TopicService service,
      TranslationService translationService) {
    super(service);
    this.blogService = blogService;
    this.translationService = translationService;
  }

  @Override
  @GetMapping("/topics")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/topics/{id}")
  public Resource<TopicEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/topics")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody TopicEntity newTopic) 
      throws Exception {
    return super.create(newTopic);
  }

  @Override
  @PutMapping("/topics/{id}")
  @SuperUserPermission
  public ResponseEntity<?> update(@RequestBody TopicEntity newTopic,
      @PathVariable String id) throws URISyntaxException {
    return super.update(newTopic, id);
  }

  @Override
  @DeleteMapping("/topics/{id}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }
  
  /**
   * Read pages.
   *
   * @param id the topic id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/topics/{id}/blogs")
  public ResponseEntity<?> readBlogs(@PathVariable String id, BaseParams params) {
    try {
      return ok(blogService.getResourcesByTopic(id, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Read translations.
   *
   * @param id the topic id
   * @return the response entity
   */
  @GetMapping("/topics/{id}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String id) {
    try {
      return ok(translationService.getAllTranslations(service.getById(id)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
