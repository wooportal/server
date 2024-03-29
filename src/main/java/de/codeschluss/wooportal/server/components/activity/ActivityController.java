package de.codeschluss.wooportal.server.components.activity;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.codeschluss.wooportal.server.components.activity.visitors.ActivityVisitorEntity;
import de.codeschluss.wooportal.server.components.address.AddressService;
import de.codeschluss.wooportal.server.components.category.CategoryService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderService;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleService;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupService;
import de.codeschluss.wooportal.server.components.user.UserService;
import de.codeschluss.wooportal.server.core.analytics.AnalyticsEntry;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
import de.codeschluss.wooportal.server.core.api.dto.BooleanPrimitive;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.security.permissions.OrgaAdminOrSuperUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.OwnActivityPermission;
import de.codeschluss.wooportal.server.core.security.permissions.OwnOrOrgaActivityOrSuperUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.ProviderPermission;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import de.codeschluss.wooportal.server.core.security.services.AuthorizationService;

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

  /** The target group service. */
  private final TargetGroupService targetGroupService;

  /** The schedule service. */
  private final ScheduleService scheduleService;

  /** The organisation service. */
  private final OrganisationService organisationService;

  /** The translation service. */
  private final TranslationService translationService;

  /** The auth service. */
  private final AuthorizationService authService;

  /** The push service. */
  private final PushService pushService;

  /** The subscription service. */
  private final SubscriptionService subscriptionService;

  private final VisitableService<ActivityVisitorEntity> visitableService;

  /**
   * Instantiates a new activity controller.
   *
   * @param service the service
   * @param addressService the address service
   * @param categoryService the category service
   * @param providerService the provider service
   * @param userService the user service
   * @param tagService the tag service
   * @param targetGroupService the target group service
   * @param scheduleService the schedule service
   * @param organisationService the organisation service
   * @param translationService the translation service
   * @param authService the auth service
   */
  public ActivityController(ActivityService service, AddressService addressService,
      CategoryService categoryService, ProviderService providerService, UserService userService,
      TargetGroupService targetGroupService, ScheduleService scheduleService,
      OrganisationService organisationService, TranslationService translationService,
      AuthorizationService authService, ImageService imageService, PushService pushService,
      SubscriptionService subscriptionService,
      VisitableService<ActivityVisitorEntity> visitableService) {
    super(service);
    this.addressService = addressService;
    this.categoryService = categoryService;
    this.providerService = providerService;
    this.targetGroupService = targetGroupService;
    this.scheduleService = scheduleService;
    this.organisationService = organisationService;
    this.translationService = translationService;
    this.authService = authService;
    this.imageService = imageService;
    this.pushService = pushService;
    this.subscriptionService = subscriptionService;
    this.visitableService = visitableService;
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
  @GetMapping("/activities/{id}")
  public Resource<ActivityEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/activities")
  @ProviderPermission
  public ResponseEntity<?> create(@RequestBody ActivityEntity newActivity) throws Exception {
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
  @PutMapping("/activities/{id}")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> update(@RequestBody ActivityEntity newActivity,
      @PathVariable String id) throws URISyntaxException {
    return super.update(newActivity, id);
  }

  @Override
  @DeleteMapping("/activities/{id}")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }

  /**
   * Read address.
   *
   * @param id the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{id}/address")
  public ResponseEntity<?> readAddress(@PathVariable String id) {
    return ok(addressService.getResourcesByActivity(id));
  }

  /**
   * Update address.
   *
   * @param id the activity id
   * @param addressId the address id
   * @return the response entity
   */
  @PutMapping("/activities/{id}/address")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> updateAddress(@PathVariable String id,
      @RequestBody StringPrimitive addressId) {
    if (addressService.existsById(addressId.getValue()) && service.existsById(id)) {
      service.updateAddress(id, addressService.getById(addressId.getValue()));
      return readAddress(id);
    } else {
      throw new BadParamsException("Activity or Address with given ID do not exist!");
    }
  }

  /**
   * Read category.
   *
   * @param id the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{id}/category")
  public ResponseEntity<?> readCategory(@PathVariable String id) {
    return ok(categoryService.getResourceByActivity(id));
  }

  /**
   * Update category.
   *
   * @param id the activity id
   * @param categoryId the category id
   * @return the response entity
   */
  @PutMapping("/activities/{id}/category")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> updateCategory(@PathVariable String id,
      @RequestBody StringPrimitive categoryId) {
    if (service.existsById(id) && categoryService.existsById(categoryId.getValue())) {
      service.updateCategory(id, categoryService.getById(categoryId.getValue()));
      return readCategory(id);
    } else {
      throw new BadParamsException("Activity or Category with given ID do not exist!");
    }
  }

  /**
   * Read organisation.
   *
   * @param id the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{id}/organisation")
  public ResponseEntity<?> readOrganisation(@PathVariable String id) {
    ProviderEntity provider = providerService.getProviderByActivity(id);
    return ok(organisationService.getResourceByProvider(provider));
  }

  /**
   * Update organisation.
   *
   * @param id the activity id
   * @param organisationId the organisation id
   * @return the response entity
   */
  @PutMapping("/activities/{id}/organisation")
  @OwnActivityPermission
  public ResponseEntity<?> updateOrganisation(@PathVariable String id,
      @RequestBody StringPrimitive organisationId) {
    try {
      service.updateProvider(id, getProvider(organisationId.getValue()));
      return readOrganisation(id);
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity, Organisation or Provider do not exist!");
    }
  }

  /**
   * Read target groups.
   *
   * @param id the activity id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/activities/{id}/targetgroups")
  public ResponseEntity<?> readTargetGroups(@PathVariable String id, BaseParams params) {
    try {
      return ok(targetGroupService.getResourceByActivity(id, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the target groups.
   *
   * @param id the activity id
   * @param targetGroupIds the target group ids
   * @return the response entity
   */
  @PostMapping("/activities/{id}/targetgroups")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> addTargetGroups(@PathVariable String id,
      @RequestBody List<String> targetGroupIds) {
    try {
      List<String> distinctTargetGroups =
          targetGroupIds.stream().distinct().collect(Collectors.toList());
      return ok(
          service.addTargetGroups(id, targetGroupService.getByIds(distinctTargetGroups)));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Target Group or Activity do not exist");
    }
  }

  /**
   * Delete target groups.
   *
   * @param id the activity id
   * @param targetGroupIds the target group ids
   * @return the response entity
   */
  @DeleteMapping("/activities/{id}/targetgroups")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> deleteTargetGroups(@PathVariable String id,
      @RequestParam(value = "targetGroupIds", required = true) List<String> targetGroupIds) {
    try {
      service.deleteTargetGroup(id, targetGroupIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    }
  }

  /**
   * Find schedules.
   *
   * @param id the activity id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/activities/{id}/schedules")
  public ResponseEntity<?> readSchedules(@PathVariable String id, BaseParams params) {
    try {
      return ok(scheduleService.getResourceByActivity(id, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the schedules.
   *
   * @param id the activity id
   * @param schedules the schedules
   * @return the response entity
   */
  @PostMapping("/activities/{id}/schedules")
  @OwnOrOrgaActivityOrSuperUserPermission
  public Resources<?> addSchedules(@PathVariable String id,
      @RequestBody List<ScheduleEntity> schedules) {
    validateSchedules(schedules);
    try {
      return scheduleService.addAllResourcesWithActivity(schedules, service.getById(id));
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
   * @param id the activity id
   * @param scheduleIds the schedule ids
   * @return the response entity
   */
  @DeleteMapping("/activities/{id}/schedules")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> deleteSchedules(@PathVariable String id,
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
   * @param organisationId the organisation id
   * @return the provider
   */
  private ProviderEntity getProvider(String organisationId) {
    return providerService.getProviderByUserAndOrganisation(authService.getCurrentUser().getId(),
        organisationId);
  }

  /**
   * Read images.
   *
   * @param id the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{id}/images")
  public ResponseEntity<?> readImages(@PathVariable String id) {
    return ok(service.getImages(id));
  }

  /**
   * Adds the image.
   *
   * @param id the activity id
   * @param images the image
   * @return the response entity
   */
  @PostMapping("/activities/{id}/images")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> addImage(@PathVariable String id,
      @RequestBody List<ImageEntity> images) {
    validateImages(images);
    try {
      List<ImageEntity> saved = service.addImages(id, imageService.addAll(images));
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
   * @param id the activity id
   * @param imageIds the image ids
   * @return the response entity
   */
  @DeleteMapping("/activities/{id}/images")
  @OwnOrOrgaActivityOrSuperUserPermission
  public ResponseEntity<?> deleteImages(@PathVariable String id,
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
   * @param id the activity id
   * @return the response entity
   */
  @GetMapping("/activities/{id}/translations")
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
   * @param id the activity id
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @PutMapping("/activities/{id}/like")
  public ResponseEntity<?> increaseLike(@PathVariable String id,
      @RequestBody(required = false) StringPrimitive subscriptionId) {
    try {
      service.increaseLike(id);
      if (subscriptionId != null && !subscriptionId.getValue().isEmpty()) {
        subscriptionService.addLikedActivity(subscriptionId.getValue(),
            service.getById(id));
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Activity does not exist");
    }
  }

  @GetMapping("/activities/{id}/iCal")
  public ResponseEntity<String> generateAllIcal(@PathVariable String id) {

    return ResponseEntity.ok().headers(createIcalHeader())
        .contentType(MediaType.APPLICATION_OCTET_STREAM).body(service.generateAllIcal(id));
  }

  @GetMapping("/activities/{id}/{scheduleId}/iCal")
  public ResponseEntity<String> generateIcal(@PathVariable String id,
      @PathVariable String scheduleId) {

    return ResponseEntity.ok().headers(createIcalHeader())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(service.generateIcal(id, scheduleId));
  }

  public HttpHeaders createIcalHeader() {
    HttpHeaders header = new HttpHeaders();
    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mycalendar.ics");
    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
    header.add("Pragma", "no-cache");
    header.add("Expires", "0");
    return header;
  }

  @GetMapping("/activities/analytics/categories")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> calculateActivitiesPerCategory(
      BooleanPrimitive current) {
    return ok(service.calculateActivitiesPerCategory(current));
  }

  @GetMapping("/activities/analytics/suburbs")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> calculateActivitiesPerSuburbs(
      BooleanPrimitive current) {
    return ok(service.calculateActivitiesPerSuburb(current));
  }

  @GetMapping("/activities/analytics/targetgroups")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> calculateActivitiesPerTargetGroup(
      BooleanPrimitive current) {
    return ok(service.calculateActivitiesPerTargetGroup(current));
  }

  @GetMapping("/activities/visitors")
  public ResponseEntity<List<VisitableEntity<?>>> calculateOverviewVisits() throws Throwable {
    return ok(visitableService.getVisitablesForOverview(this));
  }

  @GetMapping("/activities/{id}/visitors")
  public ResponseEntity<List<VisitableEntity<?>>> calculateVisitors(@PathVariable String id)
      throws Throwable {
    return ok(visitableService.getVisitablesForEntity(service.getById(id)));
  }

  /**
   * Read the titleImage
   */
  @GetMapping("/activities/{id}/titleimage")
  public ResponseEntity<ImageEntity> readTitleImage(@PathVariable String id) {
    return ok(service.getTitleImage(id));
  }

  /**
   * Adds the titleImage
   */
  @PostMapping("/activities/{id}/titleimage")
  @OrgaAdminOrSuperUserPermission
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
      throw new BadParamsException("Given Activity does not exist");
    } catch (IOException e) {
      throw new BadParamsException("Image Upload not possible");
    }
  }
}
