package de.codeschluss.wooportal.server.components.activity.translations;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface OrganisationTranslatablesRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface ActivityTranslatableRepository
    extends TranslationRepository<ActivityTranslatablesEntity> {
}
