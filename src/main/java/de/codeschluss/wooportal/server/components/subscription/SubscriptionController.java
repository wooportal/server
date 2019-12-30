package de.codeschluss.wooportal.server.components.subscription;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.push.subscriptiontype.SubscriptionTypeService;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
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
  
  private final SubscriptionTypeService subscriptionTypeService;

  /**
   * Instantiates a new subscription controller.
   *
   * @param service the service
   * @param subscriptionTypeService the subscription type service
   */
  public SubscriptionController(
      SubscriptionService service,
      SubscriptionTypeService subscriptionTypeService) {
    super(service);
    
    this.subscriptionTypeService = subscriptionTypeService;
  }

  @Override
  @GetMapping("/subscriptions")
  @SuperUserPermission
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/subscriptions/{subscriptionId}")
  @SuperUserPermission
  public Resource<SubscriptionEntity> readOne(@PathVariable String subscriptionId) {
    return super.readOne(subscriptionId);
  }
  
  @Override
  @PostMapping("/subscriptions")
  public ResponseEntity<?> create(@RequestBody SubscriptionEntity newSubscription) 
      throws Exception {
    return super.create(newSubscription);
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
   * Read subscribed types.
   *
   * @param subscriptionId the subscription id
   * @param params the params
   * @return the response entity
   */
  @GetMapping("/subscriptions/{subscriptionId}/subscribedtypes")
  public ResponseEntity<?> readSubscribedTypes(
      @PathVariable String subscriptionId,
      BaseParams params) {
    return ok(service.getSubsribedTypes(subscriptionId));
  }

  /**
   * Adds the target groups.
   *
   * @param subscriptionId
   *          the activity id
   * @param subscriptionTypeIds
   *          the target group ids
   * @return the response entity
   */
  @PostMapping("/subscriptions/{subscriptionId}/subscribedtypes")
  public ResponseEntity<?> addSubscriptionType(@PathVariable String subscriptionId,
      @RequestBody List<String> subscriptionTypeIds) {
    try {
      List<String> distinctTargetGroups = subscriptionTypeIds.stream().distinct()
          .collect(Collectors.toList());
      return ok(
          service.addSubscribedTypes(
              subscriptionId, 
              subscriptionTypeService.getByIds(distinctTargetGroups)));
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Target Group or Activity do not exist");
    }
  }

  /**
   * Delete subscription types.
   *
   * @param subscriptionId the subscription id
   * @param subscriptionTypeIds the subscription type ids
   * @return the response entity
   */
  @DeleteMapping("/subscriptions/{subscriptionId}/subscribedtypes")
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
  
  
}
