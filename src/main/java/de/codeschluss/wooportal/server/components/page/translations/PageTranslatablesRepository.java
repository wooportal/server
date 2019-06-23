package de.codeschluss.wooportal.server.components.page.translations;

import org.springframework.stereotype.Repository;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;

/**
 * The Interface PageTranslatablesRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface PageTranslatablesRepository 
    extends TranslationRepository<PageTranslatablesEntity> {
}
