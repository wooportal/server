package de.codeschluss.wooportal.server.core.push.subscriptiontype;

import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import org.springframework.stereotype.Service;

/**
 * The Class SubscriptionTypeService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class SubscriptionTypeService 
    extends ResourceDataService<SubscriptionTypeEntity, SubscriptionTypeQueryBuilder> {

  public SubscriptionTypeService(
      SubscriptionTypeRepository repo,
      SubscriptionTypeQueryBuilder entities,
      PagingAndSortingAssembler assembler) {
    super(repo, entities, assembler);
  }

  @Override
  public SubscriptionTypeEntity getExisting(SubscriptionTypeEntity newSubscriptionType) {
    return repo.findById(newSubscriptionType.getId()).orElse(null);
  }

  @Override
  public boolean validCreateFieldConstraints(SubscriptionTypeEntity newSubscriptionType) {
    return validFields(newSubscriptionType);
  }
  
  @Override
  public boolean validUpdateFieldConstraints(SubscriptionTypeEntity newSubscriptionType) {
    return validFields(newSubscriptionType);
  }

  private boolean validFields(SubscriptionTypeEntity newSubscription) {
    return newSubscription.getName() != null && !newSubscription.getName().isEmpty()
        && newSubscription.getDescription() != null && !newSubscription.getDescription().isEmpty();
  }

  @Override
  public SubscriptionTypeEntity update(String id, SubscriptionTypeEntity newSubscriptionType) {
    return repo.findById(id).map(subscriptionType -> {
      subscriptionType.setName(newSubscriptionType.getName());
      subscriptionType.setDescription(newSubscriptionType.getDescription());
      return repo.save(subscriptionType);
    }).orElseGet(() -> {
      newSubscriptionType.setId(id);
      return repo.save(newSubscriptionType);
    });
  }
}
