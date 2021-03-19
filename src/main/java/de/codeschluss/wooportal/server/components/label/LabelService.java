package de.codeschluss.wooportal.server.components.label;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.codeschluss.wooportal.server.components.label.translations.LabelTranslatableQueryBuilder;
import de.codeschluss.wooportal.server.components.label.translations.LabelTranslatableRepository;
import de.codeschluss.wooportal.server.components.label.translations.LabelTranslatablesEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageEntity;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.i18n.xliff.Transunit;
import de.codeschluss.wooportal.server.core.i18n.xliff.Xliff;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

@Service
public class LabelService extends ResourceDataService<LabelEntity, LabelQueryBuilder> {

  private final LanguageService languageService;
  
  private final LabelTranslatableRepository translatableRepo;
  
  private final LabelTranslatableQueryBuilder translatableEntities;
  
  public LabelService(
      LabelRepository repo, 
      LabelQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      LanguageService languageService,
      LabelTranslatableRepository translatableRepo,
      LabelTranslatableQueryBuilder translatableEntities) {
    super(repo, entities, assembler);
    
    this.languageService = languageService;
    this.translatableRepo = translatableRepo;
    this.translatableEntities = translatableEntities;
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
//    LanguageEntity language = languageService.getCurrentWriteLanguage();
//    deleteExisting(language);
    for (Transunit unit : new XmlMapper().readValue(xmlContent, Xliff.class).getFile().getBody()) {
      LabelEntity label = repo.findOne(entities.withTagId(unit.getId())).orElse(new LabelEntity());
      label.setTagId(unit.getId());
      label.setContent(unit.getTarget());
      repo.save(label);
    }
  }

//  private void deleteExisting(LanguageEntity language) {
//    List<LabelTranslatablesEntity> result = translatableRepo.findAll(translatableEntities.withLanguage(language.getId()));
//    
//    if (result != null && !result.isEmpty()) {
//      translatableRepo.deleteAll(result);
//    }
//  }

}
