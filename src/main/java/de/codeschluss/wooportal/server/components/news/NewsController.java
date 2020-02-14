package de.codeschluss.wooportal.server.components.news;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import com.google.firebase.messaging.FirebaseMessagingException;
import de.codeschluss.wooportal.server.components.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.push.PortalPushService;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import javax.naming.ServiceUnavailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class NewsController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class NewsController {

  /** The push service. */
  private final PortalPushService pushService;
  
  public NewsController(PortalPushService portalPushService) {
    this.pushService = portalPushService;
  }
  
  /**
   * Subscribe.
   *
   * @param newSubscription the new subscription
   * @return the response entity
   * @throws ServiceUnavailableException the service unavailable exception
   */
  @PostMapping("/news/subscribe")
  public ResponseEntity<?> subscribe(@RequestBody String token) 
      throws ServiceUnavailableException {
    SubscriptionEntity savedSubscription = pushService.subscribe(token);
    if (savedSubscription == null) {
      throw new BadParamsException("Subscription is not valid. All field must be set");
    }
    return ok(savedSubscription);
  }
  
  @DeleteMapping("/news/unsubscribe/{subscriptionId}")
  public ResponseEntity<?> unsubscribe(@PathVariable String subscriptionId) {
    pushService.unsubscribe(subscriptionId);
    return noContent().build();
  }
  
  @PostMapping("/news/push")
//  @SuperUserPermission
  public ResponseEntity<?> pushNews(@RequestBody News news) throws FirebaseMessagingException {
    pushService.push(news);
    return noContent().build();
  }
}
