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
import de.codeschluss.wooportal.server.components.markup.visitors.MarkupVisitorEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.security.permissions.OwnUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import de.codeschluss.wooportal.server.core.security.permissions.TranslatorOrSuperUserPermission;

@RestController
public class MarkupController extends CrudController<MarkupEntity, MarkupService> {
  
  private final ImageService imageService;
  
  private final TranslationService translationService;
  
  private final VisitableService<MarkupVisitorEntity> visitableService;
  
  public MarkupController(
      MarkupService service,
      ImageService imageService,
      TranslationService translationService,
      VisitableService<MarkupVisitorEntity> visitableService) {
    super(service);
    this.imageService = imageService;
    this.translationService = translationService;
    this.visitableService = visitableService;
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
  
  @GetMapping("/markups/{markupId}/visitors")
  public ResponseEntity<Integer> calculateVisitors(
      @PathVariable String markupId) throws Throwable {
    return ok(visitableService.calculateVisitors(service.getById(markupId)));
  }
  
  @GetMapping("/markups/{markupId}/visits")
  public ResponseEntity<Integer> calculateVisits(
      @PathVariable String markupId) throws Throwable {
    return ok(visitableService.calculateVisits(service.getById(markupId)));
  }
  
  @GetMapping("/markups/{markupId}/titleimage")
  public ResponseEntity<ImageEntity> readTitleImage(@PathVariable String markupId) {
    return ok(service.getTitleImage(markupId));
  }
  
  @PostMapping("/markups/{markupId}/titleimage")
  @SuperUserPermission
  public ResponseEntity<?> addTitleImage(@PathVariable String markupId,
      @RequestBody(required = false) ImageEntity avatar) {
    try {
      if (avatar == null) {
        try {
          imageService.delete(service.getTitleImage(markupId).getId()); 
        } catch (NotFoundException e) {}
        return noContent().build();
      } else {
        return ok(service.addTitleImage(markupId, imageService.add(avatar))); 
      }
    } catch (NotFoundException e) {
      throw new BadParamsException("Given Markup does not exist");
    } catch (IOException e) {
      throw new BadParamsException("Image Upload not possible");
    }
  }

}
