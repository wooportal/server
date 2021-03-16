package de.codeschluss.wooportal.server.core.security.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * The Annotation ProviderOrSuperUserPermission.
 * 
 * @author Valmir Etemi
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@authorizationService.isTranslator(authentication) "
    + "or @authorizationService.isSuperUser(authentication)")
public @interface TranslatorOrSuperUserPermission {

}
