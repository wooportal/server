package de.codeschluss.wooportal.server.components.label;

import java.io.IOException;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.i18n.xliff.Transunit;
import de.codeschluss.wooportal.server.core.i18n.xliff.Xliff;
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

  public void importLables(
      String xmlContent, 
      String filename) throws JsonParseException, JsonMappingException, IOException {
    for (Transunit unit : new XmlMapper().readValue(xmlContent, Xliff.class).getFile().getBody()) {
      LabelEntity label = repo.findOne(entities.withTagId(unit.getId())).orElse(new LabelEntity());
      label.setTagId(unit.getId());
      label.setContent(unit.getTarget());
      repo.save(label);
    }
  }

}
