package de.codeschluss.wooportal.server.core.security.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * The Annotation OwnBlogOrSuperuserPermission.
 * 
 * @author vetemi
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@authorizationService.isOwnBlog(authentication, #id) "
    + "or @authorizationService.isSuperUser(authentication)")
public @interface OwnBlogOrSuperuserPermission {

}
