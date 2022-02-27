package de.codeschluss.wooportal.server.components.markup;

import java.io.IOException;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.i18n.xliff.Transunit;
import de.codeschluss.wooportal.server.core.i18n.xliff.Xliff;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
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
  public MarkupEntity update(String id, MarkupEntity newEntity) {
    return repo.findById(id).map(entity -> {
      entity.setContent(newEntity.getContent());
      entity.setTagId(newEntity.getTagId());
      entity.setTitle(newEntity.getTitle());
      return repo.save(entity);
    }).orElseGet(() -> {
      newEntity.setId(id);
      return repo.save(newEntity);
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
  
  public ImageEntity getTitleImage(String markupId) {
    var result = repo.findOne(entities.withId(markupId))
        .orElseThrow(() -> new NotFoundException(markupId));

    if (result.getTitleImage() == null) {
      throw new NotFoundException("image");
    }
    
    return result.getTitleImage();
  }
  
  public MarkupEntity addTitleImage(String markupId, ImageEntity image)
      throws IOException {
    if (markupId == null || markupId.isEmpty()) {
      throw new NotFoundException("No Markup exists");
    }
    var user = getById(markupId);
    user.setTitleImage(image);
    return repo.save(user);
  }

}
