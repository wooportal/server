package de.codeschluss.wooportal.server.components.suburb;

import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import java.net.URISyntaxException;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class SuburbController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class SuburbController extends CrudController<SuburbEntity, SuburbService> {

  public SuburbController(SuburbService service) {
    super(service);
  }

  @Override
  @GetMapping("/suburbs")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/suburbs/{id}")
  public Resource<SuburbEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/suburbs")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody SuburbEntity newSuburb) throws Exception {
    return super.create(newSuburb);
  }

  @Override
  @PutMapping("/suburbs/{id}")
  @SuperUserPermission
  public ResponseEntity<?> update(@RequestBody SuburbEntity newSuburb,
      @PathVariable String id) throws URISyntaxException {
    return super.update(newSuburb, id);
  }

  @Override
  @DeleteMapping("/suburbs/{id}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }
}
