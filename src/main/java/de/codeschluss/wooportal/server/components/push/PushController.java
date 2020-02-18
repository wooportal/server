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
  
  @PostMapping("/push/news")
  @SuperUserPermission
  public ResponseEntity<?> pushNews(@RequestBody(required = true) MessageDto message) {
    pushService.pushNews(message);
    return noContent().build();
  }
  
  @PostMapping("/push/content")
  @SuperUserPermission
  public ResponseEntity<?> pushContent(
      @RequestBody(required = true) MessageDto message,
      @RequestBody(required = true) String link) {
    pushService.pushSingleContent(message, link);
    return noContent().build();
  }
}
