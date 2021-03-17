package de.codeschluss.wooportal.server.components.label;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
import org.springframework.web.multipart.MultipartFile;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.TranslatorOrSuperUserPermission;

@RestController
public class LabelController extends CrudController<LabelEntity, LabelService> {
  
  private final TranslationService translationService;
  
  public LabelController(
      LabelService service,
      TranslationService translationService) {
    super(service);
    this.translationService = translationService;
  }

  @Override
  @GetMapping("/labels")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/labels/{labelId}")
  public Resource<LabelEntity> readOne(@PathVariable String labelId) {
    return super.readOne(labelId);
  }

  @Override
  @PostMapping("/labels")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody LabelEntity newlabel) throws Exception {
    return super.create(newlabel);
  }

  @Override
  @PutMapping("/labels/{labelId}")
  @TranslatorOrSuperUserPermission
  public ResponseEntity<?> update(@RequestBody LabelEntity newlabel, @PathVariable String labelId)
      throws URISyntaxException {
    return super.update(newlabel, labelId);
  }

  @Override
  @DeleteMapping("/labels/{labelId}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String labelId) {
    return super.delete(labelId);
  }
  
  @GetMapping("/labels/{labelId}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String labelId) {
    try {
      return ok(translationService.getAllTranslations(service.getById(labelId)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @PostMapping("/labels/import")
  @SuperUserPermission
  public ResponseEntity<?> importLabels(@RequestParam("file") MultipartFile file) throws IOException {
    service.importLables(
        new String(file.getBytes(), StandardCharsets.UTF_8),
        file.getOriginalFilename());
    return noContent().build();
  }
}
