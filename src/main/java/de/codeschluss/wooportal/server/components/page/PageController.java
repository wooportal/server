package de.codeschluss.wooportal.server.components.page;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.components.topic.TopicService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
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
 * The Class PageController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class PageController extends CrudController<PageEntity, PageService> {
  
  /** The topic service. */
  private final TopicService topicService;
  
  /** The translation service. */
  private final TranslationService translationService;
  
  /** The push service. */
  private final PushService pushService;
  
  private final SubscriptionService subscriptionService;

  /**
   * Instantiates a new page controller.
   *
   * @param service the service
   * @param topicService the topic service
   * @param translationService the translation service
   */
  public PageController(
      PageService service,
      TopicService topicService,
      TranslationService translationService,
      PushService pushService,
      SubscriptionService subscriptionService) {
    super(service);
    this.topicService = topicService;
    this.translationService = translationService;
    this.pushService = pushService;
    this.subscriptionService = subscriptionService;
  }

  @Override
  @GetMapping("/pages")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/pages/{pageId}")
  public Resource<PageEntity> readOne(@PathVariable String pageId) {
    return super.readOne(pageId);
  }

  @Override
  @PostMapping("/pages")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody PageEntity newPage) 
      throws Exception {
    try {
      TopicEntity topic = topicService.getById(newPage.getTopicId());
      newPage.setTopic(topic);
      ResponseEntity<?> result = super.create(newPage);
      pushService.pushNewPage(newPage);
      return result;
    } catch (NotFoundException e) {
      throw new BadParamsException("Given topic does not exist");
    }
  }

  @Override
  @PutMapping("/pages/{pageId}")
  @SuperUserPermission
  public ResponseEntity<?> update(@RequestBody PageEntity newPage,
      @PathVariable String pageId) throws URISyntaxException {
    try {
      TopicEntity topic = topicService.getById(newPage.getTopicId());
      newPage.setTopic(topic);
      return super.update(newPage, pageId);
    } catch (NotFoundException e) {
      throw new BadParamsException("Given topic does not exist");
    }
  }

  @Override
  @DeleteMapping("/pages/{pageId}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String pageId) {
    return super.delete(pageId);
  }
  
  @GetMapping("/pages/{pageId}/topic")
  public ResponseEntity<?> readTopic(@PathVariable String pageId) {
    return ok(topicService.getResourceByPage(pageId));
  }
  
  /**
   * Update topic.
   *
   * @param pageId the page id
   * @param topicId the topic id
   * @return the response entity
   */
  @PutMapping("/pages/{pageId}/topic")
  @SuperUserPermission
  public ResponseEntity<?> updateTopic(@PathVariable String pageId,
      @RequestBody StringPrimitive topicId) {
    if (topicService.existsById(topicId.getValue()) 
        && service.existsById(pageId)) {
      return ok(
          service.updateResourceWithTopic(pageId, topicService.getById(topicId.getValue())));
    } else {
      throw new BadParamsException("Page or Topic with given ID do not exist!");
    }
  }
  
  /**
   * Read translations.
   *
   * @param pageId the page id
   * @return the response entity
   */
  @GetMapping("/pages/{pageId}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String pageId) {
    try {
      return ok(translationService.getAllTranslations(service.getById(pageId)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Increase like.
   *
   * @param pageId the page id
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @PutMapping("/pages/{pageId}/like")
  public ResponseEntity<?> increaseLike(
      @PathVariable String pageId,
      @RequestBody(required = false) StringPrimitive subscriptionId) {
    try {
      service.increaseLike(pageId);
      if (subscriptionId != null && !subscriptionId.getValue().isEmpty()) {
        subscriptionService.addLikedPage(
            subscriptionId.getValue(), 
            service.getById(pageId));
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Page does not exist");
    }
  }
}
