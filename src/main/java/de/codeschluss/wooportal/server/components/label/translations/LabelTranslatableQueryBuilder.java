package de.codeschluss.wooportal.server.components.label.translations;

import java.util.List;
import org.springframework.stereotype.Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

@Service
public class LabelTranslatableQueryBuilder extends QueryBuilder<QLabelTranslatablesEntity> {

  private final LanguageService languageService;
  

  public LabelTranslatableQueryBuilder(LanguageService languageService) {
    super(QLabelTranslatablesEntity.labelTranslatablesEntity, "content");
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
    return query.language.locale.in(locales);
  }
  
  private Predicate searchFiltered(BooleanBuilder search, FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return query.content.likeIgnoreCase(filter)
        .and(query.language.locale.in(
            languageService.getCurrentRequestLocales()));
  }
  
  public Predicate withLanguage(String languageId) {
    return query.language.id.eq(languageId);
  }
  
}
