package de.codeschluss.wooportal.server.components.organisation;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
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
import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.address.AddressService;
import de.codeschluss.wooportal.server.components.organisation.visitors.OrganisationVisitorEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.user.UserService;
import de.codeschluss.wooportal.server.components.video.VideoEntity;
import de.codeschluss.wooportal.server.components.video.VideoService;
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
import de.codeschluss.wooportal.server.core.security.permissions.Authenticated;
import de.codeschluss.wooportal.server.core.security.permissions.OrgaAdminOrApprovedOrgaOrSuperuser;
import de.codeschluss.wooportal.server.core.security.permissions.OrgaAdminOrSuperUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import de.codeschluss.wooportal.server.core.security.services.AuthorizationService;

// TODO: Auto-generated Javadoc
/**
 * The Class OrganisationController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class OrganisationController
    extends CrudController<OrganisationEntity, OrganisationService> {

  /** The provider service. */
  private final ProviderService providerService;

  /** The user service. */
  private final UserService userService;

  /** The address service. */
  private final AddressService addressService;

  /** The activity service. */
  private final ActivityService activityService;
  
  /** The translation service. */
  private final TranslationService translationService;
  
  /** The image service. */
  private final ImageService imageService;
  
  /** The video service. */
  private final VideoService videoService;
  
  /** The authorization service. */
  private final AuthorizationService authService;
  
  /** The subscription service. */
  private final SubscriptionService subscriptionService;
  
  private final VisitableService<OrganisationVisitorEntity> visitableService;

  /**
   * Instantiates a new organisation controller.
   *
   * @param service
   *          the service
   * @param providerService
   *          the provider service
   * @param userService
   *          the user service
   * @param addressService
   *          the address service
   * @param activityService
   *          the activity service
   */
  public OrganisationController(OrganisationService service, ProviderService providerService,
      UserService userService, AddressService addressService, ActivityService activityService,
      TranslationService translationService, ImageService imageService, VideoService videoService,
      AuthorizationService authService, SubscriptionService subscriptionService,
      VisitableService<OrganisationVisitorEntity> visitableService) {
    super(service);
    this.providerService = providerService;
    this.userService = userService;
    this.addressService = addressService;
    this.activityService = activityService;
    this.translationService = translationService;
    this.imageService = imageService;
    this.videoService = videoService;
    this.authService = authService;
    this.subscriptionService = subscriptionService;
    this.visitableService = visitableService;
  }

  @GetMapping("/organisations")
  public ResponseEntity<?> readAll(OrganisationQueryParam params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/organisations/{id}")
  @OrgaAdminOrApprovedOrgaOrSuperuser
  public Resource<OrganisationEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/organisations")
  @Authenticated
  public ResponseEntity<?> create(@RequestBody OrganisationEntity newOrga) 
      throws Exception {
    validateCreate(newOrga);
    
    try {
      newOrga.setAddress(addressService.getById(newOrga.getAddressId()));
    } catch (NotFoundException e) {
      throw new BadParamsException("Need existing Address!");
    }
    var currentUser = authService.getCurrentUser();
    if (currentUser.isSuperuser()) {
      newOrga.setApproved(true);
    }
    Resource<OrganisationEntity> resource = service.addResource(newOrga);
    providerService.addAdminAndSendMail(resource.getContent(), currentUser);
    return created(new URI(resource.getId().expand().getHref())).body(resource);
  }  
  
  /**
   * Grant approval and if isApproved is false, it will delete all existing activities.
   *
   * @param id the organisation id
   * @param isApproved the is approved
   * @return the response entity
   */
  @PutMapping("/organisations/{id}/approve")
  @SuperUserPermission
  public ResponseEntity<?> grantApproval(
      @PathVariable String id,
      @RequestBody BooleanPrimitive isApproved) {
    try {
      service.setApproval(id, isApproved.getValue());
      if (!isApproved.getValue()) {
        service.delete(id);
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist!");
    }
  }

  @Override
  @PutMapping("/organisations/{id}")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> update(@RequestBody OrganisationEntity newOrga,
      @PathVariable String id) throws URISyntaxException {
    return super.update(newOrga, id);
  }

  
  @Override
  @DeleteMapping("/organisations/{id}")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }

  /**
   * Read address.
   *
   * @param id
   *          the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{id}/address")
  public ResponseEntity<?> readAddress(@PathVariable String id) {
    return ok(addressService.getResourcesByOrganisation(id));
  }

  /**
   * Update address.
   *
   * @param id
   *          the organisation id
   * @param addressId
   *          the address id
   * @return the response entity
   */
  @PutMapping("/organisations/{id}/address")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> updateAddress(@PathVariable String id,
      @RequestBody StringPrimitive addressId) {
    if (addressService.existsById(addressId.getValue())
        && service.existsById(id)) {
      service.updateAddress(id, addressService.getById(addressId.getValue()));
      return ok(readAddress(id));
    } else {
      throw new BadParamsException("Organisation or Address with given ID do not exist!");
    }
  }

  /**
   * Read activities.
   *
   * @param id
   *          the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{id}/activities")
  public ResponseEntity<?> readActivities(
      @PathVariable String id,
      BaseParams params) {
    List<ProviderEntity> providers = providerService.getProvidersByOrganisation(id);
    try {
      return ok(activityService.getResourcesByProviders(providers, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Delete activity.
   *
   * @param id
   *          the organisation id
   * @param activityId
   *          the activity id
   * @return the response entity
   */
  @DeleteMapping("/organisations/{id}/activities/{activityId}")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> deleteActivity(@PathVariable String id,
      @PathVariable String activityId) {
    if (activityService.isActivityForProvider(activityId,
        providerService.getProvidersByOrganisation(id))) {
      activityService.delete(activityId);
      return noContent().build();
    } else {
      throw new BadParamsException("Activity does not match given organisation!");
    }
  }

  /**
   * Read users.
   *
   * @param id
   *          the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{id}/users")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> readUsers(
      @PathVariable String id) {
    List<ProviderEntity> providers = providerService.getProvidersByOrganisation(id);
    return ok(userService.convertToResourcesEmbeddedProviders(providers));
  }

  /**
   * Approve or reject user.
   *
   * @param id
   *          the organisation id
   * @param userId
   *          the user id
   * @param isApproved
   *          the is approved
   * @return the response entity
   */
  @PutMapping("/organisations/{id}/users/{userId}/approve")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> approveOrRejectUser(@PathVariable String id,
      @PathVariable String userId, @RequestBody BooleanPrimitive isApproved) {
    try {
      if (isApproved.getValue()) {
        providerService.setApprovedByUserAndOrga(userId, id);
      } else {
        providerService.deleteForUserAndOrga(userId, id);
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("User with given ID does not exist in given Organisation!");
    }
  }

  /**
   * Grant admin right.
   *
   * @param id
   *          the organisation id
   * @param userId
   *          the user id
   * @param isAdmin
   *          the is admin
   * @return the response entity
   */
  @PutMapping("/organisations/{id}/users/{userId}/admin")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> grantAdminRight(@PathVariable String id,
      @PathVariable String userId, @RequestBody BooleanPrimitive isAdmin) {
    try {
      providerService.setAdminByUserAndOrga(userId, id, isAdmin.getValue());
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("User with given ID does not exist in given Organisation!");
    }
  }

  /**
   * Delete user.
   *
   * @param id
   *          the organisation id
   * @param userId
   *          the user id
   * @return the response entity
   */
  @DeleteMapping("/organisations/{id}/users/{userId}")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> deleteUser(@PathVariable String id,
      @PathVariable String userId) {
    try {
      providerService.deleteForUserAndOrga(userId, id);
      return noContent().build();
    } catch (NotFoundException e) {
      return noContent().build();
    }
  }
  
  /**
   * Read translations.
   *
   * @param id the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{id}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String id) {
    try {
      return ok(translationService.getAllTranslations(service.getById(id)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Read images.
   *
   * @param id
   *          the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{id}/images")
  public ResponseEntity<?> readImages(@PathVariable String id) {
    return ok(service.getImages(id));
  }
  
  /**
   * Adds the image.
   *
   * @param id the organisation id
   * @param images the image
   * @return the response entity
   */
  @PostMapping("/organisations/{id}/images")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> addImage(@PathVariable String id,
      @RequestBody List<ImageEntity> images) {
    validateImages(images);
    try {
      return ok(service.addImages(id, imageService.addAll(images)));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
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
   * @param id the organisation id
   * @param imageIds the image ids
   * @return the response entity
   */
  @DeleteMapping("/organisations/{id}/images")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> deleteImages(@PathVariable String id,
      @RequestParam(value = "imageIds", required = true) List<String> imageIds) {
    try {
      imageService.deleteAll(imageIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
      }
    }
    
  @GetMapping("/organisations/{id}/avatar")
  public ResponseEntity<ImageEntity> readAvatar(@PathVariable String id) {
    return ok(service.getAvatar(id));
  }
  
  @PostMapping("/organisations/{id}/avatar")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> addAvatar(@PathVariable String id, @RequestBody ImageEntity avatar) {
    try {
      if (avatar == null) {
        try {
          imageService.delete(service.getAvatar(id).getId()); 
        } catch (NotFoundException e) {}
        return noContent().build();
      } else {
        return ok(service.addAvatar(id, imageService.add(avatar))); 
      }
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
    } catch (IOException e) {
      throw new BadParamsException("Image Upload not possible");
    }
  }

  
  @GetMapping("/organisations/{id}/videos")
  public ResponseEntity<?> readVideos(@PathVariable String id) {
    return ok(videoService.getResourcesWithEmbeddables(id));
  }
  
  /**
   * Adds the videos.
   *
   * @param id the organisation id
   * @param videos the videos
   * @return the response entity
   */
  @PostMapping("/organisations/{id}/videos")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> addVideos(@PathVariable String id,
      @RequestBody List<VideoEntity> videos) {
    validateVideos(videos);
    try {
      return ok(videoService.addAll(videos, service.getById(id)));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
    }
  }
  
  private void validateVideos(List<VideoEntity> videos) {
    if (videos == null || videos.isEmpty()) {
      throw new BadParamsException("Video must not be null");
    }
    for (VideoEntity video : videos) {
      if (!videoService.validCreateFieldConstraints(video)) {
        throw new BadParamsException("Video is missing field");
      }
    }
  }
  
  /**
   * Delete videos.
   *
   * @param id the organisation id
   * @param videoIds the video ids
   * @return the response entity
   */
  @DeleteMapping("/organisations/{id}/videos")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> deleteVideos(@PathVariable String id,
      @RequestParam(value = "videoIds", required = true) List<String> videoIds) {
    try {
      if (videoService.belongsToOrga(id, videoIds)) {
        videoService.deleteAll(videoIds);
        return noContent().build();
      } else {
        throw new BadParamsException("Videos do not belong to Organisation");
      }
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
    }
  }
  
  /**
   * Increase like.
   *
   * @param id the organisation id
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @PutMapping("/organisations/{id}/like")
  public ResponseEntity<?> increaseLike(
      @PathVariable String id, 
      @RequestBody(required = false) StringPrimitive subscriptionId) { 
    try { 
      service.increaseLike(id); 
      if (subscriptionId != null && !subscriptionId.getValue().isEmpty()) { 
        subscriptionService.addLikedOrganisation(
            subscriptionId.getValue(),  
            service.getById(id)); 
      } 
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
    }
  }
  
  @GetMapping("/organisations/visitors")
  public ResponseEntity<List<VisitableEntity<?>>> calculateOverviewVisitors() throws Throwable {
    return ok(visitableService.getVisitablesForOverview(this));
  }
  
  @GetMapping("/organisations/{id}/visitors")
  public ResponseEntity<List<VisitableEntity<?>>> calculateVisitors(
      @PathVariable String id) throws Throwable {
    return ok(visitableService.getVisitablesForEntity(service.getById(id)));
  }
}
