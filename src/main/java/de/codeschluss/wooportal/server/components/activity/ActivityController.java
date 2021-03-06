package de.codeschluss.wooportal.server.components.activity;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.components.address.AddressService;
import de.codeschluss.wooportal.server.components.blog.BlogService;
import de.codeschluss.wooportal.server.components.category.CategoryService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderService;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleService;
import de.codeschluss.wooportal.server.components.tag.TagEntity;
import de.codeschluss.wooportal.server.components.tag.TagService;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupService;
import de.codeschluss.wooportal.server.components.user.UserService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.security.permissions.OwnActivityPermission;
import de.codeschluss.wooportal.server.core.security.permissions.OwnOrOrgaActivityOrSuperUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.ProviderPermission;
import de.codeschluss.wooportal.server.core.security.services.AuthorizationService;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class SubscriptionTypeController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class ActivityController extends CrudController<ActivityEntity, ActivityService> {

  /** The address service. */
  private final AddressService addressService;
  
  /** The image service. */
  private final ImageService imageService;

  /** The category service. */
  private final CategoryService categoryService;

  /** The provider service. */
  private final ProviderService providerService;

  /** The tag service. */
  private final TagService tagService;

  /** The target group service. */
  private final TargetGroupService targetGroupService;

  /** The schedule service. */
  private final ScheduleService scheduleService;

  /** The organisation service. */
  private final OrganisationService organisationService;
  
  /** The blog service. */
  private final BlogService blogService;

  /** The translation service. */
  private final TranslationService translationService;

  /** The auth service. */
  private final AuthorizationService authService;
  
  /** The push service. */
  private final PushService pushService;
  
  /** The subscription service. */
  private final SubscriptionService subscriptionService;

  /**
   * Instantiates a new activity controller.
   *
   * @param service
   *          the service
   * @param addressService
   *          the address service
   * @param categoryService
   *          the category service
   * @param providerService
   *          the provider service
   * @param userService
   *          the user service
   * @param tagService
   *          the tag service
   * @param targetGroupService
   *          the target group service
   * @param scheduleService
   *          the schedule service
   * @param organisationService
   *          the organisation service
   * @param translationService
   *          the translation service
   * @param authService
   *          the auth service
   */
  public ActivityController(ActivityService service, AddressService addressService,
      CategoryService categoryService, ProviderService providerService, UserService userService,
      TagService tagService, TargetGroupService targetGroupService, ScheduleService scheduleService,
      OrganisationService organisationService, BlogService blogService, 
      TranslationService translationService, AuthorizationService authService,
      ImageService imageService, PushService pushService, SubscriptionService subscriptionService) {
    super(service);
    this.addressService = addressService;
    this.categoryService = categoryService;
    this.providerService = providerService;
    this.tagService = tagService;
    this.targetGroupService = targetGroupService;
    this.scheduleService = scheduleService;
    this.organisationService = organisationService;
    this.blogService = blogService;
    this.translationService = translationService;
    this.authService = authService;
    this.imageService = imageService;
    this.pushService = pushService;
    this.subscriptionService = subscriptionService;
  }

  /**
   * Read all.
   *
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/activities")
  public ResponseEntity<?> readAll(ActivityQueryParam params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/activities/{activityId}")
  public Resource<ActivityEntity> readOne(@PathVariable String activityId) {
    return super.readOne(activityId);
  }

  @Override
  @PostMapping("/activities")
  @ProviderPermission
  public ResponseEntity<?> create(@RequestBody ActivityEntity newActivity)
      throws Exception {
    validateCreate(newActivity);

    try {
      newActivity.setProvider(getProvider(newActivity.getOrganisationId()));
      newActivity.setCategory(categoryService.getById(newActivity.getCategoryId()));
      newActivity.setAddress(addressService.getById(newActivity.getAddressId()));
      // TODO: Check if target groups are nullable!
    } catch (NotFoundException e) {
      throw new BadParamsException("Need existing Provider, Category or Address");
    }

    Resource<ActivityEntity> resource = service.addResource(newActivity);
    pushService.pushNewActivity(newActivity);
    return created(new URI(resource.getId().expand().getHref())).body(resource);
  }

  @Override
  @PutMapping("/activities/{activityId}")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> update(@RequestBody ActivityEntity newActivity,
      @PathVariable String activityId) throws URISyntaxException {
    return super.update(newActivity, activityId);
  }

  @Override
  @DeleteMapping("/activities/{activityId}")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String activityId) {
    return super.delete(activityId);
  }

  /**
   * Read address.
   *
   * @param activityId
   *          the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/address")
  public ResponseEntity<?> readAddress(@PathVariable String activityId) {
    return ok(addressService.getResourcesByActivity(activityId));
  }

  /**
   * Update address.
   *
   * @param activityId the activity id
   * @param addressId the address id
   * @return the response entity
   */
  @PutMapping("/activities/{activityId}/address")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> updateAddress(@PathVariable String activityId,
      @RequestBody StringPrimitive addressId) {
    if (addressService.existsById(addressId.getValue()) 
        && service.existsById(activityId)) {
      service.updateAddress(activityId, addressService.getById(addressId.getValue()));
      return readAddress(activityId);
    } else {
      throw new BadParamsException("Activity or Address with given ID do not exist!");
    }
  }

  /**
   * Read category.
   *
   * @param activityId
   *          the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/category")
  public ResponseEntity<?> readCategory(@PathVariable String activityId) {
    return ok(categoryService.getResourceByActivity(activityId));
  }

  /**
   * Update category.
   *
   * @param activityId
   *          the activity id
   * @param categoryId
   *          the category id
   * @return the response entity
   */
  @PutMapping("/activities/{activityId}/category")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> updateCategory(@PathVariable String activityId,
      @RequestBody StringPrimitive categoryId) {
    if (service.existsById(activityId) && categoryService.existsById(categoryId.getValue())) {
      service.updateCategory(activityId, categoryService.getById(categoryId.getValue()));
      return readCategory(activityId);
    } else {
      throw new BadParamsException("Activity or Category with given ID do not exist!");
    }
  }

  /**
   * Read organisation.
   *
   * @param activityId
   *          the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/organisation")
  public ResponseEntity<?> readOrganisation(@PathVariable String activityId) {
    ProviderEntity provider = providerService.getProviderByActivity(activityId);
    return ok(organisationService.getResourceByProvider(provider));
  }

  /**
   * Update organisation.
   *
   * @param activityId
   *          the activity id
   * @param organisationId
   *          the organisation id
   * @return the response entity
   */
  @PutMapping("/activities/{activityId}/organisation")
  @OwnActivityPermission
  public ResponseEntity<?> updateOrganisation(@PathVariable String activityId,
      @RequestBody StringPrimitive organisationId) {
    try {
      service.updateProvider(activityId, getProvider(organisationId.getValue()));
      return readOrganisation(activityId);
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity, Organisation or Provider do not exist!");
    }
  }

  /**
   * Read tags.
   *
   * @param activityId          the activity id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/tags")
  public ResponseEntity<?> readTags(
      @PathVariable String activityId,
      BaseParams params) {
    try {
      return ok(tagService.getResourcesByActivity(activityId, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the tags.
   *
   * @param activityId
   *          the activity id
   * @param tags
   *          the tags
   * @return the response entity
   */
  @PostMapping("/activities/{activityId}/tags")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> addTags(@PathVariable String activityId,
      @RequestBody List<TagEntity> tags) {
    try {
      validateTags(tags);
      return ok(service.addTags(activityId, tagService.addAll(tags)));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    }
  }

  /**
   * Validate tags.
   *
   * @param tags the tags
   */
  private void validateTags(List<TagEntity> tags) {
    for (TagEntity tag : tags) {
      if (!tagService.validCreateFieldConstraints(tag)) {
        throw new BadParamsException("Tags must have a name");
      }
    }
  }

  /**
   * Delete tags.
   *
   * @param activityId the activity id
   * @param tagIds the tag ids
   * @return the response entity
   */
  @DeleteMapping("/activities/{activityId}/tags")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> deleteTags(@PathVariable String activityId,
      @RequestParam(value = "tagIds", required = true) List<String> tagIds) {
    try {
      service.deleteTags(activityId, tagIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    }
  }

  /**
   * Read target groups.
   *
   * @param activityId          the activity id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/targetgroups")
  public ResponseEntity<?> readTargetGroups(
      @PathVariable String activityId,
      BaseParams params) {
    try {
      return ok(targetGroupService.getResourceByActivity(activityId, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the target groups.
   *
   * @param activityId
   *          the activity id
   * @param targetGroupIds
   *          the target group ids
   * @return the response entity
   */
  @PostMapping("/activities/{activityId}/targetgroups")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> addTargetGroups(@PathVariable String activityId,
      @RequestBody List<String> targetGroupIds) {
    try {
      List<String> distinctTargetGroups = targetGroupIds.stream().distinct()
          .collect(Collectors.toList());
      return ok(
          service.addTargetGroups(activityId, targetGroupService.getByIds(distinctTargetGroups)));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Target Group or Activity do not exist");
    }
  }

  /**
   * Delete target groups.
   *
   * @param activityId the activity id
   * @param targetGroupIds the target group ids
   * @return the response entity
   */
  @DeleteMapping("/activities/{activityId}/targetgroups")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> deleteTargetGroups(@PathVariable String activityId,
      @RequestParam(value = "targetGroupIds", required = true) List<String> targetGroupIds) {
    try {
      service.deleteTargetGroup(activityId, targetGroupIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    }
  }

  /**
   * Find schedules.
   *
   * @param activityId          the activity id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/schedules")
  public ResponseEntity<?> readSchedules(@PathVariable String activityId, BaseParams params) {
    try {
      return ok(scheduleService.getResourceByActivity(activityId, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the schedules.
   *
   * @param activityId
   *          the activity id
   * @param schedules
   *          the schedules
   * @return the response entity
   */
  @PostMapping("/activities/{activityId}/schedules")
  @OwnOrOrgaActivityOrSuperUserPermission
  public Resources<?> addSchedules(@PathVariable String activityId,
      @RequestBody List<ScheduleEntity> schedules) {
    validateSchedules(schedules);
    try {
      return scheduleService.addAllResourcesWithActivity(
          schedules, service.getById(activityId));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Validate schedules.
   *
   * @param schedules the schedules
   */
  private void validateSchedules(List<ScheduleEntity> schedules) {
    for (ScheduleEntity schedule : schedules) {
      if (!scheduleService.validCreateFieldConstraints(schedule)) {
        throw new BadParamsException("Schedules need Start and End date");
      }
    }
  }

  /**
   * Delete schedules.
   *
   * @param activityId the activity id
   * @param scheduleIds the schedule ids
   * @return the response entity
   */
  @DeleteMapping("/activities/{activityId}/schedules")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> deleteSchedules(@PathVariable String activityId,
      @RequestParam(value = "scheduleIds", required = true) List<String> scheduleIds) {
    try {
      scheduleService.deleteAll(scheduleIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    }
  }

  /**
   * Gets the provider.
   *
   * @param organisationId
   *          the organisation id
   * @return the provider
   */
  private ProviderEntity getProvider(String organisationId) {
    return providerService.getProviderByUserAndOrganisation(authService.getCurrentUser().getId(),
        organisationId);
  }
  
  /**
   * Read blogs.
   *
   * @param activityId the activity id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/blogs")
  public ResponseEntity<?> readBlogs(@PathVariable String activityId, BaseParams params) {
    try {
      return ok(blogService.getResourceByActivity(activityId, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Read images.
   *
   * @param activityId
   *          the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/images")
  public ResponseEntity<?> readImages(@PathVariable String activityId) {
    return ok(service.getImages(activityId));
  }
  
  /**
   * Adds the image.
   *
   * @param activityId the activity id
   * @param images the image
   * @return the response entity
   */
  @PostMapping("/activities/{activityId}/images")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> addImage(@PathVariable String activityId,
      @RequestBody List<ImageEntity> images) {
    validateImages(images);
    try {
      List<ImageEntity> saved = service.addImages(activityId, imageService.addAll(images));
      return ok(saved);
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
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
   * @param activityId the activity id
   * @param imageIds the image ids
   * @return the response entity
   */
  @DeleteMapping("/activities/{activityId}/images")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> deleteImages(@PathVariable String activityId,
      @RequestParam(value = "imageIds", required = true) List<String> imageIds) {
    try {
      imageService.deleteAll(imageIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    }
  }

  /**
   * Read translations.
   *
   * @param activityId
   *          the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{activityId}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String activityId) {
    try {
      return ok(translationService.getAllTranslations(service.getById(activityId)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Increase like.
   *
   * @param activityId the activity id
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @PutMapping("/activities/{activityId}/like")
  public ResponseEntity<?> increaseLike(
      @PathVariable String activityId,
      @RequestBody(required = false) StringPrimitive subscriptionId) {
    try {
      service.increaseLike(activityId);
      if (subscriptionId != null && !subscriptionId.getValue().isEmpty()) {
        subscriptionService.addLikedActivity(
            subscriptionId.getValue(), 
            service.getById(activityId));
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    }
  }
}
