package de.codeschluss.wooportal.server.components.adminpush;

import com.google.firebase.messaging.FirebaseMessagingException;
import de.codeschluss.wooportal.server.core.push.PushService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The Class AdminPushService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class AdminPushService {

  /** The push service. */
  private final PushService pushService;
  
  public AdminPushService(PushService pushService) {
    this.pushService = pushService;
  }
  
  public void pushNews(@RequestBody News news) throws FirebaseMessagingException {
    pushService.push(news);
  }
}
