package de.codeschluss.wooportal.server.core.push.subscriptiontype;

import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;
import org.springframework.stereotype.Service;

/**
 * The Class SubscriptionTypeQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class SubscriptionTypeQueryBuilder extends QueryBuilder<QSubscriptionTypeEntity> {

  public SubscriptionTypeQueryBuilder() {
    super(QSubscriptionTypeEntity.subscriptionTypeEntity);
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    // TODO Auto-generated method stub
    return null;
  }

  public Predicate withAllSet(SubscriptionTypeEntity newSubscriptionType) {
    // TODO Auto-generated method stub
    return null;
  }

}
