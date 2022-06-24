package de.codeschluss.wooportal.server.components.blog;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.HashMap;
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
import de.codeschluss.wooportal.server.components.blog.visitors.BlogVisitorEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerService;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.components.topic.TopicService;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.BooleanPrimitive;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.mail.MailService;
import de.codeschluss.wooportal.server.core.security.permissions.ApprovedBlogOrSuperuser;
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
  
  private final MailService mailService;
  
  /** The push service. */
  private final PushService pushService;
  
  /** The subscription service. */
  private final SubscriptionService subscriptionService;
  
  /** The translation service. */
  private final TranslationService translationService;
  
  private final TopicService topicService;
  
  private final VisitableService<BlogVisitorEntity> visitableService;

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
      MailService mailService,
      PushService pushService,
      SubscriptionService subscriptionService,
      TranslationService translationService,
      TopicService topicService,
      VisitableService<BlogVisitorEntity> visitableService) {
    super(service);
    this.authService = authService;
    this.bloggerService = bloggerService;
    this.imageService = imageService;
    this.mailService = mailService;
    this.pushService = pushService;
    this.subscriptionService = subscriptionService;
    this.translationService = translationService;
    this.topicService = topicService;
    this.visitableService = visitableService;
  }
  
  @GetMapping("/blogs")
  public ResponseEntity<?> readAll(BlogQueryParam params) {
    return super.readAll(params);
  }
  
  @Override
  @GetMapping("/blogs/{id}")
  @ApprovedBlogOrSuperuser
  public Resource<BlogEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/blogs")
  public ResponseEntity<?> create(@RequestBody BlogEntity newBlog) throws Exception {
    
    var blogger = getBlogger();
    if (blogger != null) {
      newBlog.setBlogger(blogger);
      newBlog.setApproved(true);
    } else {
      newBlog.setApproved(false);
      mailService.sendEmail(
          "Beitrag eingereicht",
          "newblog.ftl",
          new HashMap<String, Object>());
    }
    
    TopicEntity topic = topicService.getById(newBlog.getTopicId());
    newBlog.setTopic(topic);
    
    ResponseEntity<?> result = super.create(newBlog);
    pushService.pushNewBlog(newBlog);
    return result;
  }
  
  private BloggerEntity getBlogger() {
    return authService.getCurrentUser() != null
        ? bloggerService.getByUser(authService.getCurrentUser().getId())
        : null;
  }
  
  @PutMapping("/blogs/{id}/approve")
  @SuperUserPermission
  public ResponseEntity<?> grantApproval(
      @PathVariable String id,
      @RequestBody BooleanPrimitive isApproved) {
    try {
      service.setApproval(id, isApproved.getValue());
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Blog does not exist!");
    }
  }

  @Override
  @PutMapping("/blogs/{id}")
  @OwnBlogOrSuperuserPermission
  public ResponseEntity<?> update(@RequestBody BlogEntity newBlog, @PathVariable String id)
      throws URISyntaxException {
    return super.update(newBlog, id);
  }

  @Override
  @DeleteMapping("/blogs/{id}")
  @OwnBlogOrSuperuserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }
  
  /**
   * Read images.
   *
   * @param id the blog id
   * @return the response entity
   */
  @GetMapping("/blogs/{id}/images")
  public ResponseEntity<?> readImages(@PathVariable String id) {
    return ok(service.getImages(id));
  }
  
  @GetMapping("/blogs/{id}/topic")
  public ResponseEntity<?> readTopic(@PathVariable String id) {
    return ok(topicService.getResourceByBlog(id));
  }
  
  @PutMapping("/blogs/{id}/topic")
  @SuperUserPermission
  public ResponseEntity<?> updateTopic(@PathVariable String id,
      @RequestBody StringPrimitive topicId) {
    if (topicService.existsById(topicId.getValue()) 
        && service.existsById(id)) {
      return ok(
          service.updateResourceWithTopic(id, topicService.getById(topicId.getValue())));
    } else {
      throw new BadParamsException("Page or Topic with given ID do not exist!");
    }
  }

  /**
   * Adds the image.
   *
   * @param id the blog id
   * @param images the images
   * @return the response entity
   */
  @PostMapping("/blogs/{id}/images")
  @OwnBlogOrSuperuserPermission
  public ResponseEntity<?> addImage(@PathVariable String id,
      @RequestBody List<ImageEntity> images) {
    validateImages(images);
    try {
      List<ImageEntity> saved = service.addImages(id, imageService.addAll(images));
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
   * @param id the blog id
   * @param imageIds the image ids
   * @return the response entity
   */
  @DeleteMapping("/blogs/{id}/images")
  @OwnBlogOrSuperuserPermission
  public ResponseEntity<?> deleteImages(@PathVariable String id,
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
   * @param id the blog id
   * @return the response entity
   */
  @GetMapping("/blogs/{id}/blogger")
  public ResponseEntity<?> readBlogger(@PathVariable String id) {
    return ok(bloggerService.getByBlog(id));
  }
  
  /**
   * Read translations.
   *
   * @param id
   *          the blog id
   * @return the response entity
   */
  @GetMapping("/blogs/{id}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String id) {
    try {
      return ok(translationService.getAllTranslations(service.getById(id)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Increase like.
   *
   * @param id the blog id
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @PutMapping("/blogs/{id}/like")
  public ResponseEntity<?> increaseLike(
      @PathVariable String id,
      @RequestBody(required = false) StringPrimitive subscriptionId) {
    try {
      service.increaseLike(id);
      if (subscriptionId != null && !subscriptionId.getValue().isEmpty()) {
        subscriptionService.addLikedBlog(
            subscriptionId.getValue(), 
            service.getById(id));
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Blog does not exist");
    }
  }
  
  @GetMapping("/blogs/visitors")
  public ResponseEntity<List<VisitableEntity<?>>> calculateOverviewVisitors() throws Throwable {
    return ok(visitableService.getVisitablesForOverview(this));
  }
  
  @GetMapping("/blogs/{id}/visitors")
  public ResponseEntity<List<VisitableEntity<?>>> calculateVisitors(
      @PathVariable String id) throws Throwable {
    return ok(visitableService.getVisitablesForEntity(service.getById(id)));
  }
  
  @GetMapping("/blogs/{id}/titleimage")
  public ResponseEntity<ImageEntity> readTitleImage(@PathVariable String id) {
    return ok(service.getTitleImage(id));
  }

  @PostMapping("/blogs/{id}/titleimage")
  public ResponseEntity<?> addTitleImage(@PathVariable String id,
      @RequestBody ImageEntity titleImage) {
    try {
      if (titleImage == null) {
        try {
          imageService.delete(service.getTitleImage(id).getId()); 
        } catch (NotFoundException e) {}
        return noContent().build();
      } else {
        return ok(service.addTitleImage(id, imageService.add(titleImage))); 
      }
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Blog does not exist");
    } catch (IOException e) {
      throw new BadParamsException("Image Upload not possible");
    }
  }
}
