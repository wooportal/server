package de.codeschluss.wooportal.server.core.security.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * The Annotation OrgaAdminOrSuperUserPermission.
 * 
 * @author Valmir Etemi
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@authorizationService.isOrgaAdmin(authentication, #id) "
    + "or @authorizationService.isSuperUser(authentication)")
public @interface OrgaAdminOrSuperUserPermission {

}
