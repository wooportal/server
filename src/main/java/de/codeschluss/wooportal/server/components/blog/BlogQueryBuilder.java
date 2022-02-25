package de.codeschluss.wooportal.server.components.blog;

import java.util.List;
import org.springframework.stereotype.Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

/**
 * The Class BlogQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class BlogQueryBuilder extends QueryBuilder<QBlogEntity> {
  
  /** The language service. */
  private final LanguageService languageService;

  public BlogQueryBuilder(LanguageService languageService) {
    super(QBlogEntity.blogEntity, "translatables.title");
    this.languageService = languageService;
  }
  
  @Override
  public boolean localized() {
    return true;
  }
  
  @Override
  protected String prepareSort(String sortProp) {
    return sortProp.equals("title") || sortProp.equals("content")
        ? "translatables." + sortProp
        : sortProp;
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
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
  private Predicate searchFiltered(BooleanBuilder search, FilterSortPaginate p) {
    var params = validateParams(p);
    String filter = params.getFilter();
    
    return filter != null && !filter.isEmpty()
        ? fuzzyTextSearch(params, search)
        : advancedSearch(params, search);
  }
  
  public Predicate advancedSearch(BlogQueryParam params, BooleanBuilder search) {
    BooleanBuilder advancedSearch = new BooleanBuilder();
    if (params.getTopics() != null && !params.getTopics().isEmpty()) {
      advancedSearch.and(withAnyOfTopics(params.getTopics()));
    }
    
    return search.and(advancedSearch).getValue();
  }
  
  private Predicate withAnyOfTopics(List<String> topics) {
    return query.topic.id.in(topics);
  }

  public Predicate fuzzyTextSearch(
      BlogQueryParam params,
      BooleanBuilder search) {
    String filter = prepareFilter(params.getFilter());
    return search.and(likeTitle(filter)
        .or(likeContent(filter)));
  }
  
  private <P extends FilterSortPaginate> BlogQueryParam validateParams(P p) {
    if (p instanceof BlogQueryParam) {
      return (BlogQueryParam) p;
    }
    throw new RuntimeException(
        "Must be of type " + BlogQueryParam.class + " but is " + p.getClass());
  }

  /**
   * Like title.
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
   * With blog id and user id.
   *
   * @param id the blog id
   * @param userId the user id
   * @return the predicate
   */
  public Predicate withBlogIdAndUserId(String id, String userId) {
    return withUserId(userId).and(withId(id));
  }

  /**
   * With user id.
   *
   * @param userId the user id
   * @return the boolean expression
   */
  public BooleanExpression withUserId(String userId) {
    return query.blogger.user.id.eq(userId);
  }
  
  public BooleanExpression withTopicId(String topicId) {
    return query.topic.id.eq(topicId);
  }

}
