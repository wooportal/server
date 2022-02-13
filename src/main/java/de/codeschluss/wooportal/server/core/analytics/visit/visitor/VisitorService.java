package de.codeschluss.wooportal.server.core.analytics.visit.visitor;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.core.service.DataService;

@Service
public class VisitorService extends DataService<VisitorEntity, VisitorQueryBuilder> {

  public VisitorService(
      VisitorRepository repo, 
      VisitorQueryBuilder entities) {
    super(repo, entities);
  }

  @Override
  public VisitorEntity getExisting(VisitorEntity newEntity) {
    return repo.findOne(entities.withIpAddress(newEntity.getIpAddress())).orElse(null);
  }

  @Override
  public boolean validCreateFieldConstraints(VisitorEntity newEntity) {
    return validateFields(newEntity);
  }

  @Override
  public boolean validUpdateFieldConstraints(VisitorEntity newEntity) {
    return validateFields(newEntity);
  }
  
  private boolean validateFields(VisitorEntity newEntity) {
    return newEntity.getIpAddress() != null && !newEntity.getIpAddress().isBlank() 
        && newEntity.getUserAgent() != null && !newEntity.getUserAgent().isBlank();
  }

  @Override
  public VisitorEntity update(String id, VisitorEntity newEntity) {
    return repo.findById(id).map(entity -> {
      entity.setIpAddress(newEntity.getIpAddress());
      entity.setUserAgent(newEntity.getUserAgent());
      return repo.save(entity);
    }).orElseGet(() -> {
      newEntity.setId(id);
      return repo.save(newEntity);
    });
  }
  

  public void emptyIdAddresses() {
    var results = repo.findAll(entities.withIpAddressNotEmpty());
    if (results != null && !results.isEmpty()) {
      repo.saveAll(results.stream().map(visitor -> {
        visitor.setIpAddress(null);
        return visitor;
      }).collect(Collectors.toList()));
    }
  }

}
