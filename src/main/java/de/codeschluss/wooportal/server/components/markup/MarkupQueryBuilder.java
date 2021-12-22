package de.codeschluss.wooportal.server.components.markup;

import java.util.List;
import org.springframework.stereotype.Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

@Service
public class MarkupQueryBuilder extends QueryBuilder<QMarkupEntity> {

  private final LanguageService languageService;
  

  public MarkupQueryBuilder(LanguageService languageService) {
    super(QMarkupEntity.markupEntity, "tagId");
    this.languageService = languageService;
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
  
  public Predicate withLanguage(String languageId) {
    return query.translatables.any().language.id.eq(languageId);
  }

  public Predicate withLanguageLocale(List<String> locales) {
    return query.translatables.any().language.locale.in(locales);
  }
  
}
