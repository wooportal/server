package de.codeschluss.wooportal.server.components.socialmedia;

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
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;

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
  @GetMapping("/socialmedia/{id}")
  public Resource<SocialMediaEntity> readOne(@PathVariable String id) {
    return super.readOne(id);
  }

  @Override
  @PostMapping("/socialmedia")
  @SuperUserPermission
  public ResponseEntity<?> create(@RequestBody SocialMediaEntity newSocialMedia) throws Exception {
    return super.create(newSocialMedia);
  }

  @Override
  @PutMapping("/socialmedia/{id}")
  @SuperUserPermission
  public ResponseEntity<?> update(@RequestBody SocialMediaEntity newSocialMedia,
      @PathVariable String id) throws URISyntaxException {
    return super.update(newSocialMedia, id);
  }

  @Override
  @DeleteMapping("/socialmedia/{id}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String id) {
    return super.delete(id);
  }
}
