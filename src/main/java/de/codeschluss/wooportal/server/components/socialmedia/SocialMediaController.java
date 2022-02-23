package de.codeschluss.wooportal.server.components.socialmedia;

import static org.springframework.http.ResponseEntity.status;
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
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;

/**
 * The Class ConfigurationController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class SocialMediaController extends CrudController<SocialMediaEntity, SocialMediaService> {

  public SocialMediaController(SocialMediaService service) {
    super(service);
  }

  @Override
  @GetMapping("/socialmedia")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/socialmedia/{socialmediaId}")
  public Resource<SocialMediaEntity> readOne(@PathVariable String socialmediaId) {
    return super.readOne(socialmediaId);
  }

  @Override
  @PostMapping("/socialmedia")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody SocialMediaEntity newSocialMedia) throws Exception {
    super.create(newSocialMedia);
    return status(HttpStatus.METHOD_NOT_ALLOWED).build();
  }

  @Override
  @PutMapping("/socialmedia/{socialmediaId}")
  @SuperUserPermission
  public ResponseEntity<?> update(@RequestBody SocialMediaEntity newSocialMedia,
      @PathVariable String socialmediaId) throws URISyntaxException {
    return super.update(newSocialMedia, socialmediaId);
  }

  @Override
  @DeleteMapping("/socialmedia/{socialmediaId}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String socialmediaId) {
    return super.delete(socialmediaId);
  }
}
