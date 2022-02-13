package de.codeschluss.wooportal.server.components.push.subscription;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.push.PushConfig;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeEntity;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeService;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.core.analytics.AnalyticsEntry;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

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
  
  private SubscriptionTypeService subscriptionTypeService;
  
  public SubscriptionService(
      SubscriptionRepository repo,
      SubscriptionQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      PushConfig config,
      SubscriptionTypeService subscriptionTypeService) {
    super(repo, entities, assembler);
    this.config = config;
    this.subscriptionTypeService = subscriptionTypeService;
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
      subscription.setLocale(newSubscription.getLocale());
      return repo.save(subscription);
    }).orElseGet(() -> {
      newSubscription.setId(id);
      return repo.save(newSubscription);
    });
  }

  public List<SubscriptionEntity> getAll() {
    return repo.findAll();
  }
  
  public List<SubscriptionEntity> getByActivityReminderTypeSub() {
    return getBySubscribedType(config.getTypeActivityReminder());
  }
  
  public List<SubscriptionEntity> getByNewsSub() {
    return getBySubscribedType(config.getTypeNews());
  }
  
  public List<SubscriptionEntity> getByNewContentSub() {
    return getBySubscribedType(config.getTypeNewContent());
  }
  
  public List<SubscriptionEntity> getByNewContentAndActivitySub() {
    return repo.findAll(
        entities.withNewContentTypeAndHasActivity(config.getTypeNewContent()));
  }
  
  public List<SubscriptionEntity> getByNewContentAndOrgaSub(String organisationId) {
    return repo.findAll(
        entities.withNewContentTypeAndOrga(config.getTypeNewContent(), organisationId));
  }
  
  public List<SubscriptionEntity> getByNewContentAndBloggerSub(String bloggerId) {
    return repo.findAll(
        entities.withNewContentTypeAndBlogger(config.getTypeNewContent(), bloggerId));
  }
  
  public List<SubscriptionEntity> getByNewContentAndTopicSub(String topicId) {
    return repo.findAll(
        entities.withNewContentTypeAndTopic(config.getTypeNewContent(), topicId));
  }
  
  public List<SubscriptionEntity> getBySingleContentSub() {
    return getBySubscribedType(config.getTypeSingleContent());
  }
  
  private List<SubscriptionEntity> getBySubscribedType(String type) {
    return repo.findAll(entities.withSubscribedType(type));
  }
  
  /**
   * Gets the subscribed activities.
   *
   * @param subscriptionId the subscription id
   * @return the subscribed activities
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws NotFoundException the not found exception
   */
  public Resources<?> getSubscribedActivities(String subscriptionId)
      throws JsonParseException, JsonMappingException, IOException, NotFoundException {
    List<ActivityEntity> subscribedActivities = getById(subscriptionId).getActivitySubscriptions();
    if (subscribedActivities != null && !subscribedActivities.isEmpty()) {
      return assembler.entitiesToResources(subscribedActivities, null);
    }
    throw new NotFoundException(subscriptionId);
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

  /**
   * Gets the subscribed bloggers.
   *
   * @param subscriptionId the subscription id
   * @return the subscribed bloggers
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws NotFoundException the not found exception
   */
  public Resources<?> getSubscribedBloggers(String subscriptionId)
      throws JsonParseException, JsonMappingException, IOException, NotFoundException {
    List<BloggerEntity> subscribedBloggers = getById(subscriptionId).getBloggerSubscriptions();
    if (subscribedBloggers != null && !subscribedBloggers.isEmpty()) {
      return assembler.entitiesToResources(subscribedBloggers, null);
    }
    throw new NotFoundException(subscriptionId);
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
  
  /**
   * Gets the subscribed organisations.
   *
   * @param subscriptionId the subscription id
   * @return the subscribed organisations
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws NotFoundException the not found exception
   */
  public Resources<?> getSubscribedOrganisations(String subscriptionId) 
      throws JsonParseException, JsonMappingException, IOException, NotFoundException {
    List<OrganisationEntity> subscribedOrgas = 
        getById(subscriptionId).getOrganisationSubscriptions();
    if (subscribedOrgas != null && !subscribedOrgas.isEmpty()) {
      return assembler.entitiesToResources(subscribedOrgas, null);
    }
    throw new NotFoundException(subscriptionId);
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
  
  /**
   * Gets the subscribed types.
   *
   * @param subscriptionId the subscription id
   * @return the subscribed types
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Resources<?> getSubscribedTypes(String subscriptionId) 
      throws JsonParseException, JsonMappingException, IOException {
    List<SubscriptionTypeEntity> subscribedTypes = getById(subscriptionId).getSubscribedTypes();
    if (subscribedTypes != null && !subscribedTypes.isEmpty()) {
      return assembler.entitiesToResources(subscribedTypes, null);
    }
    throw new NotFoundException(subscriptionId);
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

  /**
   * Gets the subscribed topics.
   *
   * @param subscriptionId the subscription id
   * @return the subscribed topics
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Resources<?> getSubscribedTopics(String subscriptionId) 
      throws JsonParseException, JsonMappingException, IOException {
    List<TopicEntity> subscribedTopics = getById(subscriptionId).getTopicSubscriptions();
    if (subscribedTopics != null && !subscribedTopics.isEmpty()) {
      return assembler.entitiesToResources(subscribedTopics, null);
    }
    throw new NotFoundException(subscriptionId);
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

  /**
   * Adds the liked activity.
   *
   * @param subscriptionId the subscription id
   * @param activityLikeToAdd the activity to add
   */
  public void addLikedActivity(String subscriptionId, ActivityEntity activityLikeToAdd) {
    try {
      SubscriptionEntity subscription = getById(subscriptionId);
      if (subscription.getActivityLikes().stream().noneMatch(activity -> 
          activity.getId().equals(activityLikeToAdd.getId()))) {
        subscription.getActivityLikes().add(activityLikeToAdd);
      }
      repo.save(subscription);
    } catch (NotFoundException e) {
      return;
    }
  }

  /**
   * Adds the liked blog.
   *
   * @param subscriptionId the subscription id
   * @param blogLikeToAdd the blog like to add
   */
  public void addLikedBlog(String subscriptionId, BlogEntity blogLikeToAdd) {
    try {
      SubscriptionEntity subscription = getById(subscriptionId);
      if (subscription.getBlogLikes().stream().noneMatch(blog -> 
          blog.getId().equals(blogLikeToAdd.getId()))) {
        subscription.getBlogLikes().add(blogLikeToAdd);
      }
      repo.save(subscription);
    } catch (NotFoundException e) {
      return;
    }
  }
  
  /**
   * Adds the liked organisation.
   *
   * @param subscriptionId the subscription id
   * @param organisationLikeToAdd the organisation like to add
   */
  public void addLikedOrganisation(
      String subscriptionId, 
      OrganisationEntity organisationLikeToAdd) {
    try {
      SubscriptionEntity subscription = getById(subscriptionId);
      if (subscription.getOrganisationLikes().stream().noneMatch(organisation -> 
          organisation.getId().equals(organisationLikeToAdd.getId()))) {
        subscription.getOrganisationLikes().add(organisationLikeToAdd);
      }
      repo.save(subscription);
    } catch (NotFoundException e) {
      return;
    }
  }
  
  public List<AnalyticsEntry> calculateSubscriptions() {
    List<SubscriptionEntity> subscriptions = getAll();
    var data = subscriptionTypeService.getAll().stream().collect(Collectors.toMap(c -> c, c -> 0.0));    
    if (subscriptions != null && !subscriptions.isEmpty()) {
      for (SubscriptionEntity subscription : subscriptions) {
        for (SubscriptionTypeEntity type : subscription.getSubscribedTypes()) {
          Double value = data.get(type);
          data.put(type, value + 1);
        }
      }
    }
    
    return data.entrySet()
        .stream()
        .map(entry -> 
          new AnalyticsEntry(entry.getKey().getName(), entry.getValue(), null))
        .sorted()
        .collect(Collectors.toList());
  }
  
}
