package de.codeschluss.wooportal.server.components.tag.translations;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;
import org.springframework.stereotype.Repository;

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
