package de.codeschluss.wooportal.server.core.security.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@authorizationService.isApprovedBlog(#id) "
    + "or @authorizationService.isSuperUser(authentication)")
public @interface ApprovedBlogOrSuperuser {

}
