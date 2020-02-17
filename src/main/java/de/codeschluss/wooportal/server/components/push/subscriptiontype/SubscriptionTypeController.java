package de.codeschluss.wooportal.server.components.push.subscriptiontype;

import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class SubscriptionTypeController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class SubscriptionTypeController 
    extends CrudController<SubscriptionTypeEntity, SubscriptionTypeService> {
  
  private final TranslationService translationService;

  /**
   * Instantiates a new subscription type controller.
   *
   * @param service the service
   * @param translationService the translation service
   */
  public SubscriptionTypeController(
      SubscriptionTypeService service,
      TranslationService translationService) {
    super(service);
    
    this.translationService = translationService;
  }

  @Override
  @GetMapping("/subscriptiontypes")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/subscriptiontypes/{subscriptionId}")
  public Resource<SubscriptionTypeEntity> readOne(@PathVariable String subscriptionId) {
    return super.readOne(subscriptionId);
  }
  
  @Override
  @PostMapping("/subscriptiontypes")
  public ResponseEntity<?> create(@RequestBody SubscriptionTypeEntity newSubscriptionType) 
      throws Exception {
    return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
  }

  @Override
  @PutMapping("/subscriptiontypes/{subscriptionTypeId}")
  public ResponseEntity<?> update(@RequestBody SubscriptionTypeEntity newSubscriptionType,
      @PathVariable String subscriptionTypeId) throws URISyntaxException {
    return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
  }
  
  @Override
  @DeleteMapping("/subscriptiontypes/{subscriptionTypeId}")
  public ResponseEntity<?> delete(@PathVariable String subscriptionTypeId) {
    return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
  }

  /**
   * Read translations.
   *
   * @param organisationId the organisation id
   * @return the response entity
   */
  @GetMapping("/subscriptiontypes/{subscriptionTypeId}/translations")
  public ResponseEntity<?> readTranslations(@PathVariable String organisationId) {
    try {
      return ok(translationService.getAllTranslations(service.getById(organisationId)));
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
}
