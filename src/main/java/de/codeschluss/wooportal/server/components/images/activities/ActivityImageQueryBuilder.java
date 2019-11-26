package de.codeschluss.wooportal.server.components.images.activities;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;
import java.util.List;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class ActivityImageQueryBuilder.
 *
 * @author Valmir Etemi
 */
@Service
public class ActivityImageQueryBuilder extends QueryBuilder<QActivityImageEntity> {
  
  private final LanguageService languageService;
  
  
  /**
   * Instantiates a new activity image query builder.
   *
   * @param languageService the language service
   */
  public ActivityImageQueryBuilder(
      LanguageService languageService) {
    super(QActivityImageEntity.activityImageEntity, "caption");
    
    this.languageService = languageService;
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    String filter = prepareFilter(params.getFilter());
    List<String> locales = languageService.getCurrentReadLocales();
    
    return query.activity.translatables.any().name.likeIgnoreCase(filter)
        .and(query.activity.translatables.any().language.locale.in(locales));
  }

  /**
   * For activity id.
   *
   * @param activityId the activity id
   * @return the boolean expression
   */
  public BooleanExpression forActivityId(String activityId) {
    return query.activity.id.eq(activityId);
  }

}
