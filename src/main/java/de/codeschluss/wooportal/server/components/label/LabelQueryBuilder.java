package de.codeschluss.wooportal.server.components.label;

import java.util.List;
import org.springframework.stereotype.Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class LabelQueryBuilder.
 */
@Service
public class LabelQueryBuilder extends QueryBuilder<QLabelEntity> {
  
  /** The language service. */
  private final LanguageService languageService;
  
  /**
   * Instantiates a new Label query builder.
   *
   * @param languageService the language service
   */
  public LabelQueryBuilder(LanguageService languageService) {
    super(QLabelEntity.labelEntity, "translatables.content");
    this.languageService = languageService;
  }

  public boolean localized() {
    return true;
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    List<String> locales = languageService.getCurrentRequestLocales();
    BooleanBuilder search = new BooleanBuilder(withLocalized(locales));
    return params.isEmptyQuery()
        ? search.getValue()
        : searchFiltered(search, params);
  }
  
  private Predicate withLocalized(List<String> locales) {
    String defaultLang = languageService.getDefaultLocale();
    if (!locales.contains(defaultLang)) {
      locales.add(defaultLang);
    }
    return query.translatables.any().language.locale.in(locales);
  }
  
  private Predicate searchFiltered(BooleanBuilder search, FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return query.translatables.any().content.likeIgnoreCase(filter)
        .and(query.translatables.any().language.locale.in(
            languageService.getCurrentRequestLocales()));
  }

  public Predicate withTagId(String tagId) {
    return query.tagId.eq(tagId);
  }
  
}
