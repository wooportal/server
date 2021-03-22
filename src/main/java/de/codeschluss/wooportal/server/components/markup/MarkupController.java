package de.codeschluss.wooportal.server.components.markup;

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
public class MarkupController extends CrudController<MarkupEntity, MarkupService> {
  
  private final TranslationService translationService;
  
  public MarkupController(
      MarkupService service,
      TranslationService translationService) {
    super(service);
    this.translationService = translationService;
  }

  @Override
  @GetMapping("/markups")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/markups/{markupId}")
  public Resource<MarkupEntity> readOne(@PathVariable String markupId) {
    return super.readOne(markupId);
  }

  @Override
  @PostMapping("/markups")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody MarkupEntity newmarkup) throws Exception {
    return super.create(newmarkup);
  }

  @Override
  @PutMapping("/markups/{markupId}")
  @TranslatorOrSuperUserPermission
  public ResponseEntity<?> update(@RequestBody MarkupEntity newmarkup, @PathVariable String markupId)
      throws URISyntaxException {
    return super.update(newmarkup, markupId);
  }

  @Override
  @DeleteMapping("/markups/{markupId}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String markupId) {
    return super.delete(markupId);
  }
  
  @GetMapping("/markups/{markupId}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String markupId) {
    try {
      return ok(translationService.getAllTranslations(service.getById(markupId)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @PostMapping("/markups/import")
  @SuperUserPermission
  public ResponseEntity<?> importMarkups(@RequestParam("file") MultipartFile file) throws IOException {
    service.importLables(
        new String(file.getBytes(), StandardCharsets.UTF_8),
        file.getOriginalFilename());
    return noContent().build();
  }
}
