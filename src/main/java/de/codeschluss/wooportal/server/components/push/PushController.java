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

  /** The push service. */
  private final PushService pushService;
  
  public PushController(PushService pushService) {
    this.pushService = pushService;
  }
  
  @PostMapping("/push/notifications")
  @SuperUserPermission
  public ResponseEntity<?> pushNotifications(@RequestBody(required = true) MessageDto message) {
    pushService.pushNotifications(message);
    return noContent().build();
  }
  
  @PostMapping("/push/mails")
  @SuperUserPermission
  public ResponseEntity<?> pushMails(@RequestBody(required = true) MessageDto message) {
    pushService.pushMails(message);
    return noContent().build();
  }
 
}
