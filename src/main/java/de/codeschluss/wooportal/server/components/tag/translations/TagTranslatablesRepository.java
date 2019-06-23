package de.codeschluss.wooportal.server.components.tag.translations;

import org.springframework.stereotype.Repository;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;

/**
 * The Interface TagTranslatablesRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface TagTranslatablesRepository 
    extends TranslationRepository<TagTranslatablesEntity> {
}
