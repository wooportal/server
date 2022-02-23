package de.codeschluss.wooportal.server.components.socialmedia;

import org.springframework.stereotype.Service;
import com.querydsl.core.types.dsl.BooleanExpression;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

/**
 * The Class ConfigurationQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class SocialMediaQueryBuilder extends QueryBuilder<QSocialMediaEntity> {
  
  public SocialMediaQueryBuilder() {
    super(QSocialMediaEntity.socialMediaEntity);
  }

  public BooleanExpression withItem(String name) {
    return query.name.eq(name);
  }

  @Override
  public BooleanExpression search(FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return query.name.likeIgnoreCase(filter);
  }
  
}
