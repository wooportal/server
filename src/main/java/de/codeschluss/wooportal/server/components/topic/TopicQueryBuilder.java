package de.codeschluss.wooportal.server.components.topic;

import java.util.List;
import org.springframework.stereotype.Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class TopicQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class TopicQueryBuilder extends QueryBuilder<QTopicEntity> {
  
  /** The language service. */
  private final LanguageService languageService;
  
  /**
   * Instantiates a new topic query builder.
   */
  public TopicQueryBuilder(LanguageService languageService) {
    super(QTopicEntity.topicEntity, "translatables.name");
    this.languageService = languageService;
  }
  
  @Override
  protected String prepareSort(String sortProp) {
    return sortProp.equals("name")
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
    return search.and(likeName(filter));
  }

  /**
   * With name.
   *
   * @param name the name
   * @return the boolean expression
   */
  public BooleanExpression withName(String name) {
    return query.translatables.any().language.locale.in(
        languageService.getCurrentRequestLocales())
        .and(query.translatables.any().name.eq(name));
  }

  /**
   * Name.
   *
   * @param filter the filter
   * @return the boolean expression
   */
  private BooleanExpression likeName(String filter) {
    return query.translatables.any().name.likeIgnoreCase(filter)
        .and(query.translatables.any().language.locale.in(
            languageService.getCurrentRequestLocales()));
  }

  /**
   * With any blog id.
   *
   * @param blogId the blog id
   * @return the predicate
   */
  public Predicate withAnyBlogId(String blogId) {
    return query.blogs.any().id.eq(blogId);
  }
}
