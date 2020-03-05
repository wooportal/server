package de.codeschluss.wooportal.server.components.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.google.firebase.messaging.WebpushNotification.Action;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * The Class FirebasePushService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class FirebasePushService {

  /** The subscription entity. */
  private final SubscriptionService subscriptionService;

  /**
   * Instantiates a new portal push service.
   *
   * @param subscriptionService the subscription service
   * @param config the config
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public FirebasePushService(
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
   * Send push.
   *
   * @param subscription the subscription
   * @param message the message
   */
  public void sendPush(
      SubscriptionEntity subscription, 
      MessageDto message,
      Map<String, String> additionalData) {
    try {
      Builder messageBuilder = Message.builder()
          .setToken(subscription.getAuthSecret())
          .setNotification(
              Notification.builder()
                .setTitle(message.getTitle())
                .setBody(message.getContent())
              .build());

      if (additionalData != null) {
        messageBuilder
          .setAndroidConfig(AndroidConfig.builder()
              .putAllData(additionalData)
              .build())
          .setApnsConfig(ApnsConfig.builder()
              .setAps(Aps.builder()
                  .putAllCustomData(new HashMap<String, Object>(additionalData))
                  .build())
              .build())
          .setWebpushConfig(WebpushConfig.builder()
              .setNotification(WebpushNotification.builder()
                  .setData(additionalData)
                  .build())
              .build());
      }
      
      FirebaseMessaging.getInstance()
          .sendAsync(messageBuilder.build())
          .get();

    } catch (InterruptedException | ExecutionException e) {
      subscriptionService.delete(subscription.getId());
    }
  }
}
