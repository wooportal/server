package de.codeschluss.wooportal.server.components.push.subscription;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.blogger.BloggerService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeService;
import de.codeschluss.wooportal.server.components.topic.TopicService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class SubscriptionTypeController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class SubscriptionController 
    extends CrudController<SubscriptionEntity, SubscriptionService> {
  
  /** The subscription type service. */
  private final SubscriptionTypeService subscriptionTypeService;
  
  /** The activity service. */
  private final ActivityService activityService;
  
  /** The orga service. */
  private final OrganisationService orgaService;
  
  /** The blogger service. */
  private final BloggerService bloggerService;
  
  private final TopicService topicService;

  /**
   * Instantiates a new subscription controller.
   *
   * @param service the service
   * @param subscriptionTypeService the subscription type service
   */
  public SubscriptionController(
      SubscriptionService service,
      SubscriptionTypeService subscriptionTypeService,
      ActivityService activityService,
      OrganisationService orgaService,
      BloggerService bloggerService,
      TopicService topicService) {
    super(service);
    
    this.subscriptionTypeService = subscriptionTypeService;
    this.activityService = activityService;
    this.orgaService = orgaService;
    this.bloggerService = bloggerService;
    this.topicService = topicService;
  }

  @Override
  @GetMapping("/subscriptions")
  @SuperUserPermission
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/subscriptions/{subscriptionId}")
  public Resource<SubscriptionEntity> readOne(@PathVariable String subscriptionId) {
    return super.readOne(subscriptionId);
  }
  
  @Override
  @PostMapping("/subscriptions")
  public ResponseEntity<?> create(@RequestBody SubscriptionEntity newSubscription) 
      throws Exception {
    validateCreate(newSubscription);
    
    Resource<SubscriptionEntity> existing = service.getExistingResource(newSubscription);
    if (existing != null) {
      return ok(existing);
    }
    newSubscription.setSubscribedTypes(subscriptionTypeService.getAll());
    Resource<SubscriptionEntity> resource = service.addResource(newSubscription);
    return created(new URI(resource.getId().expand().getHref())).body(resource);
  }

  @Override
  @PutMapping("/subscriptions/{subscriptionId}")
  public ResponseEntity<?> update(@RequestBody SubscriptionEntity newSubscription,
      @PathVariable String subscriptionId) throws URISyntaxException {
    return super.update(newSubscription, subscriptionId);
  }
  
  @Override
  @DeleteMapping("/subscriptions/{subscriptionId}")
  public ResponseEntity<?> delete(@PathVariable String subscriptionId) {
    return super.delete(subscriptionId);
  }
  
  /**
   * Read subscribed activities.
   *
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @GetMapping("/subscriptions/{subscriptionId}/activities")
  public ResponseEntity<?> readActivitySubscriptions(
      @PathVariable String subscriptionId) {
    try {
      return ok(service.getSubscribedActivities(subscriptionId));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the activity subscription.
   *
   * @param subscriptionId the subscription id
   * @param activityId the activity id
   * @return the response entity
   */
  @PostMapping("/subscriptions/{subscriptionId}/activities")
  public ResponseEntity<?> addActivitySubscription(@PathVariable String subscriptionId,
      @RequestBody StringPrimitive activityId) {
    try {
      return ok(service.addActivitySubscription(
          subscriptionId, activityService.getById(activityId.getValue())));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription or Activity do not exist");
    }
  }

  /**
   * Delete activity subscriptions.
   *
   * @param subscriptionId the subscription id
   * @param activityIds the activity ids
   * @return the response entity
   */
  @DeleteMapping("/subscriptions/{subscriptionId}/activities")
  public ResponseEntity<?> deleteActivitySubscriptions(
      @PathVariable String subscriptionId,
      @RequestParam(value = "activityIds", required = true) 
        List<String> activityIds) {
    try {
      service.deleteActivitySubscriptions(subscriptionId, activityIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription does not exist");
    }
  }
  
  /**
   * Read blogger subscriptions.
   *
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @GetMapping("/subscriptions/{subscriptionId}/bloggers")
  public ResponseEntity<?> readBloggerSubscriptions(
      @PathVariable String subscriptionId) {
    try {
      return ok(service.getSubscribedBloggers(subscriptionId));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the blogger subscription.
   *
   * @param subscriptionId the subscription id
   * @param bloggerId the blogger id
   * @return the response entity
   */
  @PostMapping("/subscriptions/{subscriptionId}/bloggers")
  public ResponseEntity<?> addBloggerSubscription(@PathVariable String subscriptionId,
      @RequestBody StringPrimitive bloggerId) {
    try {
      return ok(service.addBloggerSubscription(
          subscriptionId, bloggerService.getById(bloggerId.getValue())));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription or Blogger do not exist");
    }
  }

  /**
   * Delete blogger subscriptions.
   *
   * @param subscriptionId the subscription id
   * @param bloggerIds the blogger ids
   * @return the response entity
   */
  @DeleteMapping("/subscriptions/{subscriptionId}/bloggers")
  public ResponseEntity<?> deleteBloggerSubscriptions(
      @PathVariable String subscriptionId,
      @RequestParam(value = "bloggerIds", required = true) 
        List<String> bloggerIds) {
    try {
      service.deleteBloggerSubscriptions(subscriptionId, bloggerIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription does not exist");
    }
  }
  
  /**
   * Read organisation subscriptions.
   *
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @GetMapping("/subscriptions/{subscriptionId}/organisations")
  public ResponseEntity<?> readOrganisationSubscriptions(
      @PathVariable String subscriptionId) {
    try {
      return ok(service.getSubscribedOrganisations(subscriptionId));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the organisation subscription.
   *
   * @param subscriptionId the subscription id
   * @param organisationId the organisation id
   * @return the response entity
   */
  @PostMapping("/subscriptions/{subscriptionId}/organisations")
  public ResponseEntity<?> addOrganisationSubscription(@PathVariable String subscriptionId,
      @RequestBody StringPrimitive organisationId) {
    try {
      return ok(service.addOrganisationSubscription(
          subscriptionId, orgaService.getById(organisationId.getValue())));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription or Organisation do not exist");
    }
  }

  /**
   * Delete organisation subscriptions.
   *
   * @param subscriptionId the subscription id
   * @param organisationIds the organisation ids
   * @return the response entity
   */
  @DeleteMapping("/subscriptions/{subscriptionId}/organisations")
  public ResponseEntity<?> deleteOrganisationSubscriptions(
      @PathVariable String subscriptionId,
      @RequestParam(value = "organisationIds", required = true) 
        List<String> organisationIds) {
    try {
      service.deleteOrganisationSubscriptions(subscriptionId, organisationIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription does not exist");
    }
  }
  
  /**
   * Read subscribed types.
   *
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @GetMapping("/subscriptions/{subscriptionId}/types")
  public ResponseEntity<?> readSubscribedTypes(
      @PathVariable String subscriptionId) {
    try {
      return ok(service.getSubscribedTypes(subscriptionId));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds subscribed types.
   *
   * @param subscriptionId
   *          the activity id
   * @param subscriptionTypeIds
   *          the target group ids
   * @return the response entity
   */
  @PostMapping("/subscriptions/{subscriptionId}/types")
  public ResponseEntity<?> addSubscriptionType(@PathVariable String subscriptionId,
      @RequestBody List<String> subscriptionTypeIds) {
    try {
      List<String> distinctSubscriptionTypes = subscriptionTypeIds.stream().distinct()
          .collect(Collectors.toList());
      return ok(
          service.addSubscribedTypes(
              subscriptionId, 
              subscriptionTypeService.getByIds(distinctSubscriptionTypes)));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription or Subscription Type do not exist");
    }
  }

  /**
   * Delete subscription types.
   *
   * @param subscriptionId the subscription id
   * @param subscriptionTypeIds the subscription type ids
   * @return the response entity
   */
  @DeleteMapping("/subscriptions/{subscriptionId}/types")
  public ResponseEntity<?> deleteSubscriptionTypes(
      @PathVariable String subscriptionId,
      @RequestParam(value = "subscriptionTypeIds", required = true) 
        List<String> subscriptionTypeIds) {
    try {
      service.deleteSubscriptionType(subscriptionId, subscriptionTypeIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription does not exist");
    }
  }
  
  /**
   * Read topic subscriptions.
   *
   * @param subscriptionId the subscription id
   * @return the response entity
   */
  @GetMapping("/subscriptions/{subscriptionId}/topics")
  public ResponseEntity<?> readTopicSubscriptions(
      @PathVariable String subscriptionId) {
    try {
      return ok(service.getSubscribedTopics(subscriptionId));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds the topic subscription.
   *
   * @param subscriptionId the subscription id
   * @param topicId the topic id
   * @return the response entity
   */
  @PostMapping("/subscriptions/{subscriptionId}/topics")
  public ResponseEntity<?> addTopicSubscription(@PathVariable String subscriptionId,
      @RequestBody StringPrimitive topicId) {
    try {
      return ok(service.addTopicSubscription(
          subscriptionId, topicService.getById(topicId.getValue())));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription or Blogger do not exist");
    }
  }

  /**
   * Delete topic subscriptions.
   *
   * @param subscriptionId the subscription id
   * @param topicIds the topic ids
   * @return the response entity
   */
  @DeleteMapping("/subscriptions/{subscriptionId}/topics")
  public ResponseEntity<?> deleteTopicSubscriptions(
      @PathVariable String subscriptionId,
      @RequestParam(value = "topicIds", required = true) 
        List<String> topicIds) {
    try {
      service.deleteTopicSubscriptions(subscriptionId, topicIds);
      return noContent().build();
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Subscription does not exist");
    }
  }
  
}
