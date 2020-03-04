package de.codeschluss.wooportal.server.components.push;

import static org.springframework.http.ResponseEntity.noContent;

import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class PushController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class PushController {

  private final PushService pushService;
  
  public PushController(PushService pushService) {
    this.pushService = pushService;
  }
  
  @PostMapping("/push")
//  @SuperUserPermission
  public ResponseEntity<?> push(@RequestBody(required = true) MessageDto message) {
    pushService.pushMessage(message);
    return noContent().build();
  }
 
}
