package de.codeschluss.wooportal.server.components.user;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.mail.MessagingException;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.blog.BlogService;
import de.codeschluss.wooportal.server.components.blogger.BloggerService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
import de.codeschluss.wooportal.server.core.api.dto.BooleanPrimitive;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.security.permissions.Authenticated;
import de.codeschluss.wooportal.server.core.security.permissions.OwnUserOrSuperUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.OwnUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import de.codeschluss.wooportal.server.core.security.services.AuthorizationService;

// TODO: Auto-generated Javadoc
/**
 * The Class UserController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class UserController extends CrudController<UserEntity, UserService> {

  /** The provider service. */
  private final ProviderService providerService;
  
  /** The activity service. */
  private final ActivityService activityService;
  
  /** The organisation service. */
  private final OrganisationService organisationService;
  
  /** The blog service. */
  private final BlogService blogService;
  
  /** The blogger service. */
  private final BloggerService bloggerService;
  
  /** The auth service. */
  private final AuthorizationService authService;
  
  /** The image service. */
  private final ImageService imageService;

  /**
   * Instantiates a new user controller.
   *
   * @param userService the user service
   * @param providerService the provider service
   * @param activityService the activity service
   * @param organisationService the organisation service
   */
  public UserController(
      UserService userService, 
      ProviderService providerService,
      ActivityService activityService, 
      OrganisationService organisationService,
      BlogService blogService,
      BloggerService bloggerService,
      AuthorizationService authService, ImageService imageService) {
    super(userService);
    this.providerService = providerService;
    this.activityService = activityService;
    this.organisationService = organisationService;
    this.blogService = blogService;
    this.bloggerService = bloggerService;
    this.authService = authService;
    this.imageService = imageService;
    }

  @Override
  @GetMapping("/users")
  @SuperUserPermission
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/users/{id}")
  @OwnUserOrSuperUserPermission
  public Resource<UserEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/users")
  public ResponseEntity<?> create(@RequestBody UserEntity newUser) throws URISyntaxException {
    validateCreate(newUser);
    try {
      UserEntity createdUser = service.add(newUser);
      
      if (newUser.getOrganisationRegistrations() != null 
          && !newUser.getOrganisationRegistrations().isEmpty()) {
        providerService.createApplication(createdUser, newUser.getOrganisationRegistrations());
      }
      
      if (newUser.isApplyBlogger()) {
        bloggerService.createApplication(createdUser);
      }
      
      return created(new URI(createdUser.toResource().getId().expand().getHref()))
          .body(createdUser.toResource());
    } catch (NotFoundException | NullPointerException e) {
      throw new BadParamsException("User or Organisation are null or do not exist!");
    }
  }

  @Override
  @PutMapping("/users/{id}")
  @OwnUserPermission
  public ResponseEntity<?> update(@RequestBody UserEntity newUser, @PathVariable String id)
      throws URISyntaxException {
    return super.update(newUser, id);
  }

  @Override
  @DeleteMapping("/users/{id}")
  @OwnUserOrSuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }

  /**
   * Grant superuser right.
   *
   * @param id the user id
   * @param isSuperuser the is superuser
   * @return the response entity
   */
  @PutMapping("/users/{id}/superuser")
  @SuperUserPermission
  public ResponseEntity<?> grantSuperuserRight(@PathVariable String id,
      @RequestBody BooleanPrimitive isSuperuser) {
    try {
      service.grantSuperUser(id, isSuperuser.getValue());
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("User with given ID does not exist!");
    }
  }
  
  @PutMapping("/users/{id}/translator")
  @SuperUserPermission
  public ResponseEntity<?> grantTranslatorRight(@PathVariable String id,
      @RequestBody BooleanPrimitive isTranslator) {
    try {
      service.grantTranslator(id, isTranslator.getValue());
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("User with given ID does not exist!");
    }
  }

  /**
   * Read organisations.
   *
   * @param id the user id
   * @return the response entity
   */
  @GetMapping("/users/{id}/organisations")
  @OwnUserOrSuperUserPermission
  public ResponseEntity<?> readOrganisations(
      @PathVariable String id) {
    List<ProviderEntity> providers = providerService.getProvidersByUser(id);
    return ok(organisationService.convertToResourcesEmbeddedProviders(providers));
  }

  /**
   * Adds the organisation.
   *
   * @param id the user id
   * @param organisationParam the organisation param
   * @return the response entity
   */
  @PostMapping("/users/{id}/organisations")
  @OwnUserOrSuperUserPermission
  public ResponseEntity<?> addOrganisation(@PathVariable String id,
      @RequestBody List<String> organisationParam) {
    try {
      return ok(providerService.createApplication(
          service.getById(id), organisationParam));
    } catch (NotFoundException | NullPointerException e) {
      throw new BadParamsException("User or Organisation are null or do not exist!");
    }
  }

  /**
   * Delete organisation.
   *
   * @param id the user id
   * @param orgaId the orga id
   * @return the response entity
   */
  @DeleteMapping("/users/{id}/organisations/{orgaId}")
  @OwnUserOrSuperUserPermission
  public ResponseEntity<?> deleteOrganisation(@PathVariable String id,
      @PathVariable String orgaId) {
    try {
      providerService.deleteForUserAndOrga(id, orgaId);
      return noContent().build();
    } catch (NotFoundException e) {
      return noContent().build();
    }
  }

  /**
   * Read activities.
   *
   * @param id the user id
   * @return the response entity
   */
  @GetMapping("/users/{id}/activities")
  public ResponseEntity<?> readActivities(
      @PathVariable String id,
      BaseParams params) {
    List<ProviderEntity> providers = providerService.getProvidersByUser(id);
    try {
      return ok(activityService.getResourcesByProviders(providers, params));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Delete activity.
   *
   * @param id the user id
   * @param activityId the activity id
   * @return the response entity
   */
  @DeleteMapping("/users/{id}/activities/{activityId}")
  @OwnUserOrSuperUserPermission
  public ResponseEntity<?> deleteActivity(@PathVariable String id,
      @PathVariable String activityId) {
    if (activityService.isActivityForProvider(
        activityId, providerService.getProvidersByUser(id))) {
      activityService.delete(activityId);
      return noContent().build();
    } else {
      throw new BadParamsException("Activity does not match given user!");
    }
  }

  /**
   * Reset password.
   *
   * @param username the username
   * @return the response entity
   * @throws MessagingException the messaging exception
   */
  @PutMapping("/users/resetpassword")
  public ResponseEntity<?> resetPassword(@RequestBody StringPrimitive username) 
      throws MessagingException {
    if (service.resetPassword(username.getValue())) {
      return noContent().build();
    } else {
      throw new BadParamsException(
          "Password is not reset. User does not exist or Mail is mistyped");
    }
  }
  
  /**
   * Reset all passwords.
   *
   * @return the response entity
   */
  @PutMapping("/users/resetallpasswords")
  @SuperUserPermission
  public ResponseEntity<?> resetAllPasswords() {
    service.resetAllPasswords();
    return noContent().build();
  }
  
  /**
   * Read bloggers.
   *
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/users/bloggers")
  public ResponseEntity<?> readAllBloggers(FilterSortPaginate params) {
    validateRead(params);
    return params.getPage() == null && params.getSize() == null
        ? ok(bloggerService.getSortedListResources(params))
        : ok(bloggerService.getPagedResources(params));
  }
  
  /**
   * Read activities.
   *
   * @param id the user id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/users/{id}/blogs")
  public ResponseEntity<?> readBlogs(
      @PathVariable String id,
      BaseParams params) {
    try {
      return ok(blogService.getResourceByUser(id, params));
    } catch (IOException e) {
      throw new BadParamsException("Invalid params: " + params.toString());
    }
  }
  
  /**
   * Delete blog.
   *
   * @param id the user id
   * @param blogId the blog id
   * @return the response entity
   */
  @DeleteMapping("/users/{id}/blogs/{blogId}")
  @OwnUserOrSuperUserPermission
  public ResponseEntity<?> deleteBlog(@PathVariable String id,
      @PathVariable String blogId) {
    if (blogService.isBlogUser(blogId, id)) {
      blogService.delete(blogId);
      return noContent().build();
    } else {
      throw new BadParamsException("Blog does not match given user!");
    }
  }
  
  /**
   * Apply as blogger.
   *
   * @return the response entity
   */
  @PostMapping("/users/blogapply")
  @Authenticated
  public ResponseEntity<?> applyAsBlogger() {
    return ok(bloggerService.createApplication(authService.getCurrentUser()));
  }
  
  /**
   * Grant blogger right.
   *
   * @param id the user id
   * @param isBlogger the is blogger
   * @return the response entity
   */
  @PutMapping("/users/{id}/grantblogger")
  @SuperUserPermission
  public ResponseEntity<?> grantBloggerRight(@PathVariable String id,
      @RequestBody BooleanPrimitive isBlogger) {
    try {
      if (isBlogger.getValue()) {
        bloggerService.approveByUserId(id);
      } else {
        bloggerService.deleteByUser(id);
      }
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("User with given ID does not exist!");
    }
  }
  
  /**
   * Read blogger.
   *
   * @param id the user id
   * @return the response entity
   */
  @GetMapping("/users/{id}/blogger")
  @OwnUserOrSuperUserPermission
  public ResponseEntity<?> readBlogger(@PathVariable String id) {
    return ok(bloggerService.getResourceByUser(id));
  }
  
  /**
   * Delete blogger.
   *
   * @param id the user id
   * @return the response entity
   */
  @DeleteMapping("/users/{id}/blogger")
  @OwnUserOrSuperUserPermission
  public ResponseEntity<?> deleteBlogger(@PathVariable String id) {
    try {
      bloggerService.deleteByUser(id);
      return noContent().build();
    } catch (NotFoundException e) {
      return noContent().build();
    }
  }

  @GetMapping("/user/{id}/avatar")
  public ResponseEntity<ImageEntity> readAvatar(@PathVariable String id) {
    return ok(service.getAvatar(id));
  }

  @PostMapping("/user/{id}/avatar")
  @OwnUserPermission
  public ResponseEntity<?> addAvatar(@PathVariable String id,
      @RequestBody(required = false) ImageEntity avatar) {
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
      throw new BadParamsException("Given User does not exist");
    } catch (IOException e) {
      throw new BadParamsException("Image Upload not possible");
    }
  }
}


