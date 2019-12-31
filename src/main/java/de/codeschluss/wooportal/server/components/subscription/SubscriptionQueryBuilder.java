package de.codeschluss.wooportal.server.components.subscription;

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
    return query.authSecret.in(params.getFilter())
        .or(query.publicKey.in(params.getFilter()))
        .or(query.endpoint.in(params.getFilter()));
  }

  /**
   * With all set.
   *
   * @param subscription the subscription
   * @return the predicate
   */
  public Predicate withAllSet(SubscriptionEntity subscription) {
    return query.authSecret.eq(subscription.getAuthSecret())
        .and(query.publicKey.eq(subscription.getPublicKey()))
        .and(query.endpoint.eq(subscription.getEndpoint()));
  }

}
