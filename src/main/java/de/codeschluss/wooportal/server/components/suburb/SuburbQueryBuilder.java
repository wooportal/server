package de.codeschluss.wooportal.server.components.suburb;

import org.springframework.stereotype.Service;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

/**
 * The Class ContactQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class SuburbQueryBuilder extends QueryBuilder<QSuburbEntity> {
  
  public SuburbQueryBuilder() {
    super(QSuburbEntity.suburbEntity, "name");
  }
  
  public BooleanExpression withName(String name) {
    return query.name.eq(name);
  }

  public Predicate withAnyAddressId(String activityId) {
    return query.addresses.any().id.eq(activityId);
  }
  
  @Override
  public BooleanExpression search(FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return query.name.likeIgnoreCase(filter);
  }
}
