package de.codeschluss.wooportal.server.components.subscription;

import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.push.subscriptiontype.SubscriptionTypeEntity;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.util.List;
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

  public SubscriptionService(
      SubscriptionRepository repo,
      SubscriptionQueryBuilder entities,
      PagingAndSortingAssembler assembler) {
    super(repo, entities, assembler);
  }

  @Override
  public SubscriptionEntity getExisting(SubscriptionEntity newSubscription) {
    return repo.findOne(entities.withAllSet(newSubscription)).orElse(null);
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
    return newSubscription.getAuthSecret() != null && !newSubscription.getAuthSecret().isEmpty()
        && newSubscription.getPublicKey() != null && !newSubscription.getPublicKey().isEmpty()
        && newSubscription.getEndpoint() != null && !newSubscription.getEndpoint().isEmpty();
  }

  @Override
  public SubscriptionEntity update(String id, SubscriptionEntity newSubscription) {
    return repo.findById(id).map(subscription -> {
      subscription.setAuthSecret(newSubscription.getAuthSecret());
      subscription.setEndpoint(newSubscription.getEndpoint());
      subscription.setPublicKey(newSubscription.getPublicKey());
      return repo.save(subscription);
    }).orElseGet(() -> {
      newSubscription.setId(id);
      return repo.save(newSubscription);
    });
  }

  public List<SubscriptionEntity> getAll() {
    return repo.findAll();
  }

  public List<SubscriptionTypeEntity> getSubsribedTypes(String subscriptionId) {
    return getById(subscriptionId).getSubscribedTypes();
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
    
    if (subscription.getSubscribedTypes() == null || subscription.getSubscribedTypes().isEmpty()) {
      repo.delete(subscription);
    } else {
      repo.save(subscription); 
    }
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
}
