package de.codeschluss.wooportal.server.components.category.translations;

import org.springframework.stereotype.Repository;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;

/**
 * The Interface OrganisationTranslatablesRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface CategoryTranslatablesRepository 
    extends TranslationRepository<CategoryTranslatablesEntity> {
}
