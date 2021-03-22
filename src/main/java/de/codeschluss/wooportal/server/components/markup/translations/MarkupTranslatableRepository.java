package de.codeschluss.wooportal.server.components.markup.translations;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkupTranslatableRepository
    extends TranslationRepository<MarkupTranslatablesEntity> {
}
