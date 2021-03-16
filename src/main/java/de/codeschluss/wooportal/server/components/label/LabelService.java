package de.codeschluss.wooportal.server.components.label;

import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

@Service
public class LabelService extends ResourceDataService<LabelEntity, LabelQueryBuilder> {

  public LabelService(
      LabelRepository repo, 
      LabelQueryBuilder entities,
      PagingAndSortingAssembler assembler) {
    super(repo, entities, assembler);
  }

  @Override
  public LabelEntity getExisting(LabelEntity newLabel) {
    return repo.findOne(entities.withTagId(newLabel.getTagId())).orElse(null);
  }
  
  @Override
  public boolean validCreateFieldConstraints(LabelEntity newLabel) {
    return newLabel.getContent() != null && !newLabel.getContent().isEmpty();
  }
  
  @Override
  public boolean validUpdateFieldConstraints(LabelEntity newLabel) {
    return newLabel.getContent() != null && !newLabel.getContent().isEmpty();
  }

  @Override
  public LabelEntity update(String id, LabelEntity newLabel) {
    return repo.findById(id).map(label -> {
      label.setContent(newLabel.getContent());
      return repo.save(label);
    }).orElseGet(() -> {
      newLabel.setId(id);
      return repo.save(newLabel);
    });
  }


}
