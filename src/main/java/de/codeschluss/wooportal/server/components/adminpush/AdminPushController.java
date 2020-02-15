package de.codeschluss.wooportal.server.components.adminpush;

import static org.springframework.http.ResponseEntity.noContent;

import com.google.firebase.messaging.FirebaseMessagingException;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class AdminPushController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class AdminPushController {

  private final AdminPushService adminPushService;
  
  public AdminPushController(AdminPushService adminPushService) {
    this.adminPushService = adminPushService;
  }
  
  @PostMapping("/push/news")
//  @SuperUserPermission
  public ResponseEntity<?> pushNews(@RequestBody News news) throws FirebaseMessagingException {
    adminPushService.pushNews(news);
    return noContent().build();
  }
  
  @PostMapping("/push/activity")
//  @SuperUserPermission
  public ResponseEntity<?> pushActivity(@RequestBody News news) throws FirebaseMessagingException {
    adminPushService.pushNews(news);
    return noContent().build();
  }
}
