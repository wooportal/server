package de.codeschluss.wooportal.server.components.push.subscription;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
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
  public BooleanExpression withAuthSecret(String authSecret) {
    return query.authSecret.eq(authSecret);
  }

  public BooleanExpression withNewContentTypeAndOrga(String typeNewContent, String organisationId) {
    return withSubscribedType(typeNewContent)
        .and(query.organisationSubscriptions.any().id.eq(organisationId));
  }

  public BooleanExpression withNewContentTypeAndBlogger(String typeNewContent, String bloggerId) {
    return withSubscribedType(typeNewContent)
        .and(query.bloggerSubscriptions.any().id.eq(bloggerId));
  }

  public BooleanExpression withNewContentTypeAndTopic(String typeNewContent, String topicId) {
    return withSubscribedType(typeNewContent)
        .and(query.topicSubscriptions.any().id.eq(topicId));
  }

  public BooleanExpression withNewContentTypeAndHasActivity(String typeNewContent) {
    return withSubscribedType(typeNewContent)
        .and(query.activitySubscriptions.isNotEmpty());
  }
  
  public BooleanExpression withSubscribedType(String type) {
    return query.subscribedTypes.any().configType.equalsIgnoreCase(type);
  }

}
