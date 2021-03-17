package de.codeschluss.wooportal.server.components.label;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageEntity;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.i18n.xliff.Transunit;
import de.codeschluss.wooportal.server.core.i18n.xliff.Xliff;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

@Service
public class LabelService extends ResourceDataService<LabelEntity, LabelQueryBuilder> {

  private final LanguageService languageService;
  
  public LabelService(
      LabelRepository repo, 
      LabelQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      LanguageService languageService) {
    super(repo, entities, assembler);
    
    this.languageService = languageService;
  }

  @Override
  public LabelEntity getExisting(LabelEntity newLabel) {
    return repo.findOne(entities.withTagId(newLabel.getTagId())).orElse(null);
  }
  
  public <P extends FilterSortPaginate> List<LabelEntity> getSortedList(P params) {
    Sort sort = entities.createSort(params);
    List<LabelEntity> result = params.isEmptyQuery() && !entities.localized()
        ? repo.findAll(entities.withLanguageLocale(languageService.getCurrentRequestLocales()), sort)
        : repo.findAll(entities.search(params), sort);
    
    if (result == null || result.isEmpty()) {
      throw new NotFoundException(params.toString());
    }
    
    return result;
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
    LanguageEntity language = languageService.getCurrentWriteLanguage();
    deleteExisting(language);
    for (Transunit unit : new XmlMapper().readValue(xmlContent, Xliff.class).getFile().getBody()) {
      LabelEntity label = new LabelEntity();
      label.setTagId(unit.getId());
      label.setContent(unit.getTarget());
      repo.save(label);
    }
  }

  private void deleteExisting(LanguageEntity language) {
    List<LabelEntity> result = repo.findAll(entities.withLanguage(language.getId()));
    
    if (result != null && !result.isEmpty()) {
      repo.deleteAll(result);
    }
  }

}
