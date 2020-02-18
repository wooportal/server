package de.codeschluss.wooportal.server.components.page;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import de.codeschluss.wooportal.server.components.page.QPageEntity;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

import java.util.List;

import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class PageQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class PageQueryBuilder extends QueryBuilder<QPageEntity> {
  
  /** The language service. */
  private final LanguageService languageService;
  
  /**
   * Instantiates a new page query builder.
   */
  public PageQueryBuilder(LanguageService languageService) {
    super(QPageEntity.pageEntity, "translatables.title");
    this.languageService = languageService;
  }
  
  @Override
  protected String prepareSort(String sortProp) {
    return sortProp.equals("title")
        ? "translatables." + sortProp
        : sortProp;
  }
  
  @Override
  public boolean localized() {
    return true;
  }
  
  @Override
  public Predicate search(FilterSortPaginate params) {
    List<String> locales = languageService.getCurrentRequestLocales();
    BooleanBuilder search = new BooleanBuilder(withLocalized(locales));
    return params.isEmptyQuery()
        ? search.getValue()
        : searchFiltered(search, params);
  }
  
  /**
   * With localized.
   *
   * @param locales the locales
   * @return the predicate
   */
  private Predicate withLocalized(List<String> locales) {
    String defaultLang = languageService.getDefaultLocale();
    if (!locales.contains(defaultLang)) {
      locales.add(defaultLang);
    }
    return query.translatables.any().language.locale.in(locales);
  }
  
  /**
   * Search filtered.
   *
   * @param search the search
   * @param filter the filter
   * @return the predicate
   */
  private Predicate searchFiltered(BooleanBuilder search, FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return search.and(likeContent(filter)
        .or(likeTitle(filter))
        .or(likeTopic(filter)));
  }

  /**
   * Like content.
   *
   * @param filter the filter
   * @return the boolean expression
   */
  private BooleanExpression likeContent(String filter) {
    return query.translatables.any().content.likeIgnoreCase(filter)
        .and(query.translatables.any().language.locale.in(
            languageService.getCurrentRequestLocales()));
  }
  
  /**
   * Title.
   *
   * @param filter the filter
   * @return the boolean expression
   */
  private BooleanExpression likeTitle(String filter) {
    return query.translatables.any().title.likeIgnoreCase(filter)
        .and(query.translatables.any().language.locale.in(
            languageService.getCurrentRequestLocales()));
  }
  
  /**
   * Like topic.
   *
   * @param filter the filter
   * @return the predicate
   */
  private Predicate likeTopic(String filter) {
    return query.topic.translatables.any().name.likeIgnoreCase(filter)
        .and(query.translatables.any().language.locale.in(
            languageService.getCurrentRequestLocales()));
  }

  /**
   * With title.
   *
   * @param title the title
   * @return the boolean expression
   */
  public BooleanExpression withTitle(String title) {
    return query.translatables.any().language.locale.in(languageService.getCurrentRequestLocales())
        .and(query.translatables.any().title.eq(title));
  }

  /**
   * With topic id.
   *
   * @param topicId the topic id
   * @return the boolean expression
   */
  public BooleanExpression withTopicId(String topicId) {
    return query.topic.id.eq(topicId);
  }
}
