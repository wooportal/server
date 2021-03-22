package de.codeschluss.wooportal.server.components.markup;

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
public class MarkupService extends ResourceDataService<MarkupEntity, MarkupQueryBuilder> {
  
  public MarkupService(
      MarkupRepository repo, 
      MarkupQueryBuilder entities,
      PagingAndSortingAssembler assembler) {
    super(repo, entities, assembler);
  }

  @Override
  public MarkupEntity getExisting(MarkupEntity newLabel) {
    return repo.findOne(entities.withTagId(newLabel.getTagId())).orElse(null);
  }
  
  @Override
  public boolean validCreateFieldConstraints(MarkupEntity newLabel) {
    return newLabel.getContent() != null && !newLabel.getContent().isEmpty();
  }
  
  @Override
  public boolean validUpdateFieldConstraints(MarkupEntity newLabel) {
    return newLabel.getContent() != null && !newLabel.getContent().isEmpty();
  }

  @Override
  public MarkupEntity update(String id, MarkupEntity newLabel) {
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
      MarkupEntity label = repo.findOne(entities.withTagId(unit.getId())).orElse(new MarkupEntity());
      label.setTagId(unit.getId());
      label.setContent(unit.getTarget());
      repo.save(label);
    }
  }

}
