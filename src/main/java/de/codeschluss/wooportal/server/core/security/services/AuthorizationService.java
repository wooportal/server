package de.codeschluss.wooportal.server.core.security.services;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.blog.BlogService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.user.UserEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.security.jwt.JwtUserDetails;

// TODO: Auto-generated Javadoc
/**
 * The Class AuthorizationService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class AuthorizationService {
  
  /** The organisation service. */
  private final OrganisationService organisationService;
  
  private final BlogService blogService;
  
  @Autowired
  protected HttpServletRequest request;

  /**
   * Instantiates a new authorization service.
   *
   * @param actitivityService the actitivity service
   * @param organisationService the organisation service
   */
  public AuthorizationService(
      ActivityService actitivityService,
      OrganisationService organisationService,
      BlogService blogService) {
    this.organisationService = organisationService;
    this.blogService = blogService;
  }

  /**
   * Checks if is own user.
   *
   * @param authentication the authentication
   * @param id the user id
   * @return true, if is own user
   */
  public boolean isOwnUser(Authentication authentication, String id) {
    if (authentication.getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      return jwtUserDetails.getUser().getId().equals(id);
    }
    return false;

  }
  
  public boolean isSuperUser() {
    var principal = request.getUserPrincipal();
    if (principal != null) {
      if (principal instanceof Authentication) {
        return isSuperUser((Authentication) principal);
      }
      if (principal instanceof JwtUserDetails) {
        return ((JwtUserDetails) principal).isSuperUser();
      }
    }
    return false;
  }


  /**
   * Checks if is super user.
   *
   * @param authentication the authentication
   * @return true, if is super user
   */
  public boolean isSuperUser(Authentication authentication) {
    if (authentication.getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      return jwtUserDetails.isSuperUser();
    }
    return false;
  }
  /**
   * Checks if is orga admin.
   *
   * @param authentication the authentication
   * @param id the organisation id
   * @return true, if is orga admin
   */
  public boolean isOrgaAdmin(Authentication authentication, String id) {
    if (authentication.getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      return Arrays.asList(jwtUserDetails.getAdminOrgas()).contains(id);
    }
    return false;
  }

  /**
   * Checks if is provider user.
   *
   * @param authentication the authentication
   * @return true, if is provider user
   */
  public boolean isProviderUser(Authentication authentication) {
    if (authentication.getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      return jwtUserDetails.getApprovedOrganisations() != null
          && jwtUserDetails.getApprovedOrganisations().length > 0;
    }
    return false;
  }
  
  public boolean isApprovedBlog(String id) {
    try {
      return blogService.getById(id).getApproved();
    } catch (Throwable e) {
      return true;
    }
  }
  
  public boolean isApprovedOrganisation(String id) {
    try {
      return organisationService.getById(id).isApproved();
    } catch (Throwable e) {
      return true;
    }
  }

  /**
   * Checks if is own activity.
   *
   * @param authentication the authentication
   * @param id the activity id
   * @return true, if is own activity
   */
  public boolean isOwnActivity(Authentication authentication, String id) {
    if (authentication.getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      return Arrays.asList(jwtUserDetails.getCreatedActivities()).contains(id);
    }
    return false;
  }

  /**
   * Checks if is orga activity.
   *
   * @param authentication the authentication
   * @param id the activity id
   * @return true, if is orga activity
   */
  public boolean isOrgaActivity(Authentication authentication, String id) {
    try {
      if (authentication.getPrincipal() instanceof JwtUserDetails) {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        String orgaId = organisationService.getOrgaActivity(id).getId();
        return Arrays.asList(jwtUserDetails.getAdminOrgas()).contains(orgaId);
      }
      return false;
    } catch (NotFoundException e) {
      return false;
    }
  }

  /**
   * Gets the current user.
   *
   * @return the current user
   */
  public UserEntity getCurrentUser() {
    if (SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext()
          .getAuthentication().getPrincipal();
      validateUser(jwtUserDetails);
      return jwtUserDetails.getUser();
    }
    return null;
  }

  /**
   * Checks if is blogger.
   *
   * @param authentication the authentication
   * @return true, if is blogger
   */
  public boolean isBlogger(Authentication authentication) {
    if (authentication.getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      return jwtUserDetails.isBlogger();
    }
    return false;
  }
  
  /**
   * Checks if is own blog.
   *
   * @param authentication the authentication
   * @param id the blog id
   * @return true, if is own blog
   */
  public boolean isOwnBlog(Authentication authentication, String id) {
    if (authentication.getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      return Arrays.asList(jwtUserDetails.getCreatedBlogs()).contains(id);
    }
    return false;
  }
  
  public boolean isTranslator(Authentication authentication) {
    if (authentication.getPrincipal() instanceof JwtUserDetails) {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      return jwtUserDetails.isTranslator();
    }
    return false;
  }
  
  /**
   * Validate user.
   *
   * @param jwtUserDetails the jwt user details
   */
  private void validateUser(JwtUserDetails jwtUserDetails) {
    if (jwtUserDetails == null || jwtUserDetails.getUser() == null) {
      throw new AccessDeniedException("No authenticated user");
    }
  }
}
