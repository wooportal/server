package de.codeschluss.wooportal.server.components.blog.translations;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface BlogTranslatableRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface BlogTranslatableRepository
    extends TranslationRepository<BlogTranslatablesEntity> {
}
