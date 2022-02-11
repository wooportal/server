package de.codeschluss.wooportal.server.components.blog;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerService;
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
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.security.permissions.BloggerPermission;
import de.codeschluss.wooportal.server.core.security.permissions.OwnBlogOrSuperuserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import de.codeschluss.wooportal.server.core.security.services.AuthorizationService;

/**
 * The Class BlogController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class BlogController extends CrudController<BlogEntity, BlogService> {
  
  /** The auth service. */
  private final AuthorizationService authService;
  
  /** The blogger service. */
  private final BloggerService bloggerService;
  
  /** The image service. */
  private final ImageService imageService;
  
  /** The push service. */
  private final PushService pushService;
  
  /** The subscription service. */
  private final SubscriptionService subscriptionService;
  
  /** The translation service. */
  private final TranslationService translationService;
  
  private final TopicService topicService;

  /**
   * Instantiates a new blog controller.
   *
   * @param service the service
   */
  public BlogController(
      AuthorizationService authService,
      BlogService service,
      BloggerService bloggerService,
      ImageService imageService,
      PushService pushService,
      SubscriptionService subscriptionService,
      TranslationService translationService,
      TopicService topicService) {
    super(service);
    this.authService = authService;
    this.bloggerService = bloggerService;
    this.imageService = imageService;
    this.pushService = pushService;
    this.subscriptionService = subscriptionService;
    this.translationService = translationService;
    this.topicService = topicService;
  }
  
  @Override
  @GetMapping("/blogs")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }
  
  @Override
  @GetMapping("/blogs/{blogId}")
  public Resource<BlogEntity> readOne(@PathVariable String blogId) {
    return super.readOne(blogId);
  }

  @Override
  @PostMapping("/blogs")
  @BloggerPermission
  public ResponseEntity<?> create(@RequestBody BlogEntity newBlog) throws Exception {
    
    BloggerEntity blogger = getBlogger();
    newBlog.setBlogger(blogger);
    
    TopicEntity topic = topicService.getById(newBlog.getTopicId());
    newBlog.setTopic(topic);
    
    ResponseEntity<?> result = super.create(newBlog);
    pushService.pushNewBlog(newBlog);
    return result;
  }
  
  private BloggerEntity getBlogger() {
    return bloggerService.getByUser(authService.getCurrentUser().getId());
  }

  @Override
  @PutMapping("/blogs/{blogId}")
  @OwnBlogOrSuperuserPermission
  public ResponseEntity<?> update(@RequestBody BlogEntity newBlog, @PathVariable String blogId)
      throws URISyntaxException {
    return super.update(newBlog, blogId);
  }

  @Override
  @DeleteMapping("/blogs/{blogId}")
  @OwnBlogOrSuperuserPermission
  public ResponseEntity<?> delete(@PathVariable String blogId) {
    return super.delete(blogId);
  }
  
  /**
   * Read images.
   *
   * @param blogId the blog id
   * @return the response entity
   */
  @GetMapping("/blogs/{blogId}/images")
  public ResponseEntity<?> readImages(@PathVariable String blogId) {
    return ok(service.getImages(blogId));
  }
  
  @GetMapping("/blogs/{blogId}/topic")
  public ResponseEntity<?> readTopic(@PathVariable String blogId) {
    return ok(topicService.getResourceByBlog(blogId));
  }
  
  @PutMapping("/blogs/{blogId}/topic")
  @SuperUserPermission
  public ResponseEntity<?> updateTopic(@PathVariable String blogId,
      @RequestBody StringPrimitive topicId) {
    if (topicService.existsById(topicId.getValue()) 
        && service.existsById(blogId)) {
      return ok(
          service.updateResourceWithTopic(blogId, topicService.getById(topicId.getValue())));
    } else {
      throw new BadParamsException("Page or Topic with given ID do not exist!");
    }
  }

  /**
   * Adds the image.
   *
   * @param blogId the blog id
   * @param images the images
   * @return the response entity
   */
  @PostMapping("/blogs/{blogId}/images")
  @OwnBlogOrSuperuserPermission
  public ResponseEntity<?> addImage(@PathVariable String blogId,
      @RequestBody List<ImageEntity> images) {
    validateImages(images);
    try {
      List<ImageEntity> saved = service.addImages(blogId, imageService.addAll(images));
      return ok(saved);
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Blog does not exist");
    } catch (IOException e) {
      throw new BadParamsException("Image Upload not possible");
    }
  }
  
  private void validateImages(List<ImageEntity> images) {
    if (images == null || images.isEmpty()) {
      throw new BadParamsException("Image File must not be null");
    }
    for (ImageEntity image : images) {
      if (!imageService.validCreateFieldConstraints(image)) {
        throw new BadParamsException("Image or Mime Type with correct form required");
      }
    }
  }

  /**
   * Delete images.
   *
   * @param blogId the blog id
   * @param imageIds the image ids
   * @return the response entity
   */
  @DeleteMapping("/blogs/{blogId}/images")
  @OwnBlogOrSuperuserPermission
  public ResponseEntity<?> deleteImages(@PathVariable String blogId,
      @RequestParam(value = "imageIds", required = true) List<String> imageIds) {
    try {
      imageService.deleteAll(imageIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
    }
  }
  
  /**
   * Read blogger.
   *
   * @param blogId the blog id
   * @return the response entity
   */
  @GetMapping("/blogs/{blogId}/blogger")
  public ResponseEntity<?> readBlogger(@PathVariable String blogId) {
    return ok(bloggerService.getByBlog(blogId));
  }
  
  /**
   * Read translations.
   *
   * @param blogId
   *          the blog id
   * @return the response entity
   */
  @GetMapping("/blogs/{blogId}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String blogId) {
    try {
      return ok(translationService.getAllTranslations(service.getById(blogId)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Increase like.
   *
   * @param blogId the blog id
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @PutMapping("/blogs/{blogId}/like")
  public ResponseEntity<?> increaseLike(
      @PathVariable String blogId,
      @RequestBody(required = false) StringPrimitive subscriptionId) {
    try {
      service.increaseLike(blogId);
      if (subscriptionId != null && !subscriptionId.getValue().isEmpty()) {
        subscriptionService.addLikedBlog(
            subscriptionId.getValue(), 
            service.getById(blogId));
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Blog does not exist");
    }
  }
}
