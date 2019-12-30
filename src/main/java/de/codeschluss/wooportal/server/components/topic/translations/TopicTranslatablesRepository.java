package de.codeschluss.wooportal.server.components.topic.translations;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface SubscriptionTypeTranslatablesRepository.
 * 
 * @author Valmir Etemi
 *
 */
@Repository
public interface TopicTranslatablesRepository 
    extends TranslationRepository<TopicTranslatablesEntity> {
}
