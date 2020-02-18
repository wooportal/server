package de.codeschluss.wooportal.server.components.push.subscription;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.push.PushConfig;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeEntity;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.io.IOException;
import java.util.List;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

/**
 * The Class SubscriptionTypeService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class SubscriptionService 
    extends ResourceDataService<SubscriptionEntity, SubscriptionQueryBuilder> {

  private final PushConfig config;
  
  public SubscriptionService(
      SubscriptionRepository repo,
      SubscriptionQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      PushConfig config) {
    super(repo, entities, assembler);
    this.config = config;
  }

  @Override
  public SubscriptionEntity getExisting(SubscriptionEntity newSubscription) {
    return repo.findOne(entities.withAuthSecret(newSubscription.getAuthSecret())).orElse(null);
  }

  @Override
  public boolean validCreateFieldConstraints(SubscriptionEntity newSubscription) {
    return validFields(newSubscription);
  }
  
  @Override
  public boolean validUpdateFieldConstraints(SubscriptionEntity newSubscription) {
    return validFields(newSubscription);
  }

  private boolean validFields(SubscriptionEntity newSubscription) {
    return newSubscription.getAuthSecret() != null && !newSubscription.getAuthSecret().isEmpty();
  }

  @Override
  public SubscriptionEntity update(String id, SubscriptionEntity newSubscription) {
    return repo.findById(id).map(subscription -> {
      subscription.setAuthSecret(newSubscription.getAuthSecret());
      subscription.setLanguage(newSubscription.getLanguage());
      return repo.save(subscription);
    }).orElseGet(() -> {
      newSubscription.setId(id);
      return repo.save(newSubscription);
    });
  }

  public List<SubscriptionEntity> getAll() {
    return repo.findAll();
  }
  
  public List<SubscriptionEntity> getByActivityTypeSub() {
    return getBySubscribedType(config.getTypeActivity());
  }
  
  public List<SubscriptionEntity> getByNewsSub() {
    return getBySubscribedType(config.getTypeNews());
  }
  
  public List<SubscriptionEntity> getByNewContentSub() {
    return getBySubscribedType(config.getTypeNewContent());
  }
  
  public List<SubscriptionEntity> getBySingleContentSub() {
    return getBySubscribedType(config.getTypeSingleContent());
  }
  
  private List<SubscriptionEntity> getBySubscribedType(String type) {
    return repo.findAll(entities.withSubscribedType(type));
  }
  
  public Resources<?> getSubscribedActivities(String subscriptionId)
      throws JsonParseException, JsonMappingException, IOException {
    return assembler.entitiesToResources(getById(subscriptionId).getActivitySubscriptions(), null);
  }

  /**
   * Adds the activity subscription.
   *
   * @param subscriptionId the subscription id
   * @param activityToSubscribe the activity to subscribe
   * @return the subscription entity
   */
  public List<ActivityEntity> addActivitySubscription(
      String subscriptionId, ActivityEntity activityToSubscribe) {
    SubscriptionEntity subscription = getById(subscriptionId);
    if (subscription.getActivitySubscriptions().stream().noneMatch(activity -> 
        activity.getId().equals(activityToSubscribe.getId()))) {
      subscription.getActivitySubscriptions().add(activityToSubscribe);
    }
    return repo.save(subscription).getActivitySubscriptions();
  }
  
  /**
   * Delete activity subscriptions.
   *
   * @param subscriptionId the subscription id
   * @param activityIds the activity ids
   */
  public void deleteActivitySubscriptions(String subscriptionId, List<String> activityIds) {
    SubscriptionEntity subscription = getById(subscriptionId);
    subscription.getActivitySubscriptions()   
        .removeIf(activitySubscription -> activityIds.contains(activitySubscription.getId()));
    
    repo.save(subscription); 
  }

  public Resources<?> getSubscribedBloggers(String subscriptionId)
      throws JsonParseException, JsonMappingException, IOException {
    return assembler.entitiesToResources(getById(subscriptionId).getBloggerSubscriptions(), null);
  }
  
  /**
   * Adds the blogger subscription.
   *
   * @param subscriptionId the subscription id
   * @param bloggerToSubscribe the blogger to subscribe
   * @return the subscription entity
   */
  public List<BloggerEntity> addBloggerSubscription(
      String subscriptionId, BloggerEntity bloggerToSubscribe) {
    SubscriptionEntity subscription = getById(subscriptionId);
    if (subscription.getBloggerSubscriptions().stream().noneMatch(blog -> 
        blog.getId().equals(bloggerToSubscribe.getId()))) {
      subscription.getBloggerSubscriptions().add(bloggerToSubscribe);
    }
    return repo.save(subscription).getBloggerSubscriptions();
  }
  
  /**
   * Delete blogger subscriptions.
   *
   * @param subscriptionId the subscription id
   * @param bloggerIds the blogger ids
   */
  public void deleteBloggerSubscriptions(String subscriptionId, List<String> bloggerIds) {
    SubscriptionEntity subscription = getById(subscriptionId);
    subscription.getBloggerSubscriptions()
        .removeIf(orgaSubscription -> bloggerIds.contains(orgaSubscription.getId()));
    
    repo.save(subscription); 
  }
  
  public Resources<?> getSubscribedOrganisations(String subscriptionId) 
      throws JsonParseException, JsonMappingException, IOException {
    return assembler.entitiesToResources(
        getById(subscriptionId).getOrganisationSubscriptions(), null);
  }

  /**
   * Adds the organisation subscription.
   *
   * @param subscriptionId the subscription id
   * @param organisationLikeToSubscribe the organisation like to subscribe
   */
  public List<OrganisationEntity> addOrganisationSubscription(
      String subscriptionId, OrganisationEntity organisationLikeToSubscribe) {
    SubscriptionEntity subscription = getById(subscriptionId);
    if (subscription.getOrganisationSubscriptions().stream().noneMatch(organisation -> 
        organisation.getId().equals(organisationLikeToSubscribe.getId()))) {
      subscription.getOrganisationSubscriptions().add(organisationLikeToSubscribe);
    }
    return repo.save(subscription).getOrganisationSubscriptions();
  }

  /**
   * Delete organisation subscriptions.
   *
   * @param subscriptionId the subscription id
   * @param orgaIds the orga ids
   */
  public void deleteOrganisationSubscriptions(String subscriptionId, List<String> orgaIds) {
    SubscriptionEntity subscription = getById(subscriptionId);
    subscription.getOrganisationSubscriptions()
        .removeIf(orgaSubscription -> orgaIds.contains(orgaSubscription.getId()));
    
    repo.save(subscription); 
  }
  
  public Resources<?> getSubscribedTypes(String subscriptionId) 
      throws JsonParseException, JsonMappingException, IOException {
    return assembler.entitiesToResources(getById(subscriptionId).getSubscribedTypes(), null);
  }

  /**
   * Adds the subscribed types.
   *
   * @param subscriptionId the subscription id
   * @param types the types
   * @return the list
   */
  public List<SubscriptionTypeEntity> addSubscribedTypes(
      String subscriptionId, List<SubscriptionTypeEntity> types) {
    SubscriptionEntity subscription = getById(subscriptionId);
    types.stream().forEach(subscriptionTypeToAdd -> {
      if (subscription.getSubscribedTypes().stream()
          .noneMatch(subscriptionType -> 
            subscriptionType.getId().equals(subscriptionTypeToAdd.getId()))) {
        subscription.getSubscribedTypes().add(subscriptionTypeToAdd);
      }
    });
    return repo.save(subscription).getSubscribedTypes();
  }

  /**
   * Delete subscription type.
   *
   * @param subscriptionId the subscription id
   * @param subscriptionTypeIds the subscription type ids
   */
  public void deleteSubscriptionType(String subscriptionId, List<String> subscriptionTypeIds) {
    SubscriptionEntity subscription = getById(subscriptionId);
    subscription.getSubscribedTypes()   
        .removeIf(subscriptionType -> subscriptionTypeIds.contains(subscriptionType.getId()));
    
    repo.save(subscription); 
  }

  public Resources<?> getSubscribedTopics(String subscriptionId) 
      throws JsonParseException, JsonMappingException, IOException {
    return assembler.entitiesToResources(getById(subscriptionId).getTopicSubscriptions(), null);
  }

  /**
   * Adds the topic subscription.
   *
   * @param subscriptionId the subscription id
   * @param topicToSubscribe the topic to subscribe
   * @return the list
   */
  public List<TopicEntity> addTopicSubscription(
      String subscriptionId, TopicEntity topicToSubscribe) {
    SubscriptionEntity subscription = getById(subscriptionId);
    if (subscription.getTopicSubscriptions().stream().noneMatch(topic -> 
        topic.getId().equals(topicToSubscribe.getId()))) {
      subscription.getTopicSubscriptions().add(topicToSubscribe);
    }
    return repo.save(subscription).getTopicSubscriptions();
  }

  /**
   * Delete topic subscriptions.
   *
   * @param subscriptionId the subscription id
   * @param topicIds the topic ids
   */
  public void deleteTopicSubscriptions(String subscriptionId, List<String> topicIds) {
    SubscriptionEntity subscription = getById(subscriptionId);
    subscription.getTopicSubscriptions()
        .removeIf(orgaSubscription -> topicIds.contains(orgaSubscription.getId()));
    
    repo.save(subscription); 
  }
}
