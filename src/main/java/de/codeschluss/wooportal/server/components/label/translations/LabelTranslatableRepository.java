package de.codeschluss.wooportal.server.components.label.translations;

import de.codeschluss.wooportal.server.core.i18n.translation.TranslationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelTranslatableRepository
    extends TranslationRepository<LabelTranslatablesEntity> {
}
