package de.codeschluss.wooportal.server.core.i18n.translation;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.entities.TranslatableEntity;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageEntity;
import de.codeschluss.wooportal.server.core.repository.DataRepository;

/**
 * The Interface TranslationRepository.
 * 
 * @author Valmir Etemi
 *
 */
@NoRepositoryBean
public interface TranslationRepository<T extends TranslatableEntity<?>> extends DataRepository<T> {

  <E extends BaseResource> T findByLanguageAndParent(LanguageEntity language, E parent);

  <E extends BaseResource> List<T> findByParent(E parent);
}
