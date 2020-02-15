package de.codeschluss.wooportal.server.components.organisation;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.address.AddressService;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderService;
import de.codeschluss.wooportal.server.components.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.user.UserService;
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
import de.codeschluss.wooportal.server.core.security.permissions.OrgaAdminOrSuperUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import de.codeschluss.wooportal.server.core.security.services.AuthorizationService;
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
  
  /** The authorization service. */
  private final AuthorizationService authService;
  
  private final SubscriptionService subscriptionService;

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
      TranslationService translationService, ImageService imageService,
      AuthorizationService authService, SubscriptionService subscriptionService) {
    super(service);
    this.providerService = providerService;
    this.userService = userService;
    this.addressService = addressService;
    this.activityService = activityService;
    this.translationService = translationService;
    this.imageService = imageService;
    this.authService = authService;
    this.subscriptionService = subscriptionService;
  }

  @GetMapping("/organisations")
  public ResponseEntity<?> readAll(OrganisationQueryParam params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/organisations/{organisationId}")
  public Resource<OrganisationEntity> readOne(@PathVariable String organisationId) {
    return super.readOne(organisationId);
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
    
    Resource<OrganisationEntity> resource = service.addResource(newOrga);
    providerService.addAdminAndSendMail(resource.getContent(), authService.getCurrentUser());
    return created(new URI(resource.getId().expand().getHref())).body(resource);
  }
  
  /**
   * Grant approval and if isApproved is false, it will delete all existing activities.
   *
   * @param organisationId the organisation id
   * @param isApproved the is approved
   * @return the response entity
   */
  @PutMapping("/organisations/{organisationId}/approve")
  @SuperUserPermission
  public ResponseEntity<?> grantApproval(
      @PathVariable String organisationId,
      @RequestBody BooleanPrimitive isApproved) {
    try {
      service.setApproval(organisationId, isApproved.getValue());
      if (!isApproved.getValue()) {
        service.delete(organisationId);
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist!");
    }
  }

  @Override
  @PutMapping("/organisations/{organisationId}")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> update(@RequestBody OrganisationEntity newOrga,
      @PathVariable String organisationId) throws URISyntaxException {
    return super.update(newOrga, organisationId);
  }

  @Override
  @DeleteMapping("/organisations/{organisationId}")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String organisationId) {
    return super.delete(organisationId);
  }

  /**
   * Read address.
   *
   * @param organisationId
   *          the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{organisationId}/address")
  public ResponseEntity<?> readAddress(@PathVariable String organisationId) {
    return ok(addressService.getResourcesByOrganisation(organisationId));
  }

  /**
   * Update address.
   *
   * @param organisationId
   *          the organisation id
   * @param addressId
   *          the address id
   * @return the response entity
   */
  @PutMapping("/organisations/{organisationId}/address")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> updateAddress(@PathVariable String organisationId,
      @RequestBody StringPrimitive addressId) {
    if (addressService.existsById(addressId.getValue())
        && service.existsById(organisationId)) {
      service.updateAddress(organisationId, addressService.getById(addressId.getValue()));
      return ok(readAddress(organisationId));
    } else {
      throw new BadParamsException("Organisation or Address with given ID do not exist!");
    }
  }

  /**
   * Read activities.
   *
   * @param organisationId
   *          the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{organisationId}/activities")
  public ResponseEntity<?> readActivities(
      @PathVariable String organisationId,
      BaseParams params) {
    List<ProviderEntity> providers = providerService.getProvidersByOrganisation(organisationId);
    try {
      return ok(activityService.getResourcesByProviders(providers, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Delete activity.
   *
   * @param organisationId
   *          the organisation id
   * @param activityId
   *          the activity id
   * @return the response entity
   */
  @DeleteMapping("/organisations/{organisationId}/activities/{activityId}")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> deleteActivity(@PathVariable String organisationId,
      @PathVariable String activityId) {
    if (activityService.isActivityForProvider(activityId,
        providerService.getProvidersByOrganisation(organisationId))) {
      activityService.delete(activityId);
      return noContent().build();
    } else {
      throw new BadParamsException("Activity does not match given organisation!");
    }
  }

  /**
   * Read users.
   *
   * @param organisationId
   *          the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{organisationId}/users")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> readUsers(
      @PathVariable String organisationId) {
    List<ProviderEntity> providers = providerService.getProvidersByOrganisation(organisationId);
    return ok(userService.convertToResourcesEmbeddedProviders(providers));
  }

  /**
   * Approve or reject user.
   *
   * @param organisationId
   *          the organisation id
   * @param userId
   *          the user id
   * @param isApproved
   *          the is approved
   * @return the response entity
   */
  @PutMapping("/organisations/{organisationId}/users/{userId}/approve")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> approveOrRejectUser(@PathVariable String organisationId,
      @PathVariable String userId, @RequestBody BooleanPrimitive isApproved) {
    try {
      if (isApproved.getValue()) {
        providerService.setApprovedByUserAndOrga(userId, organisationId);
      } else {
        providerService.deleteForUserAndOrga(userId, organisationId);
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("User with given ID does not exist in given Organisation!");
    }
  }

  /**
   * Grant admin right.
   *
   * @param organisationId
   *          the organisation id
   * @param userId
   *          the user id
   * @param isAdmin
   *          the is admin
   * @return the response entity
   */
  @PutMapping("/organisations/{organisationId}/users/{userId}/admin")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> grantAdminRight(@PathVariable String organisationId,
      @PathVariable String userId, @RequestBody BooleanPrimitive isAdmin) {
    try {
      providerService.setAdminByUserAndOrga(userId, organisationId, isAdmin.getValue());
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("User with given ID does not exist in given Organisation!");
    }
  }

  /**
   * Delete user.
   *
   * @param organisationId
   *          the organisation id
   * @param userId
   *          the user id
   * @return the response entity
   */
  @DeleteMapping("/organisations/{organisationId}/users/{userId}")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> deleteUser(@PathVariable String organisationId,
      @PathVariable String userId) {
    try {
      providerService.deleteForUserAndOrga(userId, organisationId);
      return noContent().build();
    } catch (NotFoundException e) {
      return noContent().build();
    }
  }
  
  /**
   * Read translations.
   *
   * @param organisationId the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{organisationId}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String organisationId) {
    try {
      return ok(translationService.getAllTranslations(service.getById(organisationId)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Read images.
   *
   * @param organisationId
   *          the organisation id
   * @return the response entity
   */
  @GetMapping("/organisations/{organisationId}/images")
  public ResponseEntity<?> readImages(@PathVariable String organisationId) {
    List<ImageEntity> test = service.getImages(organisationId);
    return ok(test);
  }
  
  /**
   * Adds the image.
   *
   * @param organisationId the organisation id
   * @param images the image
   * @return the response entity
   */
  @PostMapping("/organisations/{organisationId}/images")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> addImage(@PathVariable String organisationId,
      @RequestBody List<ImageEntity> images) {
    validateImages(images);
    try {
      List<ImageEntity> saved = service.addImages(organisationId, imageService.addAll(images));
      return ok(saved);
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
   * @param organisationId the organisation id
   * @param imageIds the image ids
   * @return the response entity
   */
  @DeleteMapping("/organisations/{organisationId}/images")
  @OrgaAdminOrSuperUserPermission
  public ResponseEntity<?> deleteImages(@PathVariable String organisationId,
      @RequestParam(value = "imageIds", required = true) List<String> imageIds) {
    try {
      imageService.deleteAll(imageIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
    }
  }
  
  /**
   * Increase like.
   *
   * @param organisationId the organisation id
   * @return the response entity
   */
  @PutMapping("/organisations/{organisationId}/like")
  public ResponseEntity<?> increaseLike(
      @PathVariable String organisationId,
      @RequestBody(required = false) StringPrimitive subscriptionId) {
    try {
      service.increaseLike(organisationId);
      if (subscriptionId != null && !subscriptionId.getValue().isEmpty()) {
        subscriptionService.addLikedOrganisation(
            subscriptionId.getValue(), 
            service.getById(organisationId));
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Organisation does not exist");
    }
  }
}
