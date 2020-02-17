package de.codeschluss.wooportal.server.components.push.subscription;

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
public class SubscriptionQueryBuilder extends QueryBuilder<QSubscriptionEntity> {

  public SubscriptionQueryBuilder() {
    super(QSubscriptionEntity.subscriptionEntity);
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    return query.authSecret.in(params.getFilter());
  }

  /**
   * With all set.
   *
   * @param authSecret the subscription
   * @return the predicate
   */
  public Predicate withAuthSecret(String authSecret) {
    return query.authSecret.eq(authSecret);
  }

  public Predicate withSubscribedType(String type) {
    return query.subscribedTypes.any().configType.equalsIgnoreCase(type);
  }

}
