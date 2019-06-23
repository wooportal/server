package de.codeschluss.wooportal.server.components.blog.translations;

import org.springframework.stereotype.Repository;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;

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
