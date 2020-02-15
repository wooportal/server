package de.codeschluss.wooportal.server.core.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FcmOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import de.codeschluss.wooportal.server.components.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.components.subscription.SubscriptionService;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * The Class PushService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class PushService {

  /** The subscription entity. */
  private final SubscriptionService subscriptionService;

  /**
   * Instantiates a new portal push service.
   *
   * @param subscriptionService the subscription service
   * @param config the config
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public PushService(
      SubscriptionService subscriptionService, 
      PushConfig config)
      throws IOException {
    this.subscriptionService = subscriptionService;
    initializePushService(config);
  }

  /**
   * Initialize push service.
   *
   * @param config the config
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void initializePushService(PushConfig config) throws IOException {
    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials
            .fromStream(new ClassPathResource(config.getFirebaseConfigFile()).getInputStream()))
        .build();
    if (FirebaseApp.getApps().isEmpty()) {
      FirebaseApp.initializeApp(options);
    }
  }

  /**
   * Push.
   *
   * @param object the object
   */
  public void push(Object object) throws FirebaseMessagingException {
    List<SubscriptionEntity> subscriptions = subscriptionService.getAll();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      for (SubscriptionEntity subscription : subscriptions) {
        try {
          sendPush(subscription, object);
        } catch (FirebaseMessagingException e) {
          subscriptionService.delete(subscription.getId());
        }
      }
    }
  }


  /**
   * Send push.
   *
   * @param subscription the subscription
   * @param object the object
   * @throws FirebaseMessagingException the firebase messaging exception
   */
  private void sendPush(SubscriptionEntity subscription, Object object) 
      throws FirebaseMessagingException {
    Message message = Message.builder()
        .setNotification(
            Notification.builder().setTitle("test").setBody("testbody").build())
        .setToken(subscription.getAuthSecret())
        .build();

    String response = FirebaseMessaging.getInstance().send(message);
    System.out.println("Successfully sent message: " + response);
  }
}
