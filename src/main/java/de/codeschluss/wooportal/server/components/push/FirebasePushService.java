package de.codeschluss.wooportal.server.components.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.Notification;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.core.error.ErrorService;
import java.io.IOException;
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
  
  /** The error service. */
  private final ErrorService errorService;

  /**
   * Instantiates a new portal push service.
   *
   * @param subscriptionService the subscription service
   * @param config the config
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public FirebasePushService(
      SubscriptionService subscriptionService, 
      PushConfig config,
      ErrorService errorService)
      throws IOException {
    this.subscriptionService = subscriptionService;
    this.errorService = errorService;
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
   * @throws FirebaseMessagingException the firebase messaging exception
   * @throws InterruptedException the interrupted exception
   * @throws ExecutionException the execution exception
   */
  public void sendPush(
      SubscriptionEntity subscription, 
      MessageDto message,
      Map<String, String> additionalData) {
    try {
      Builder messageBuilder = Message.builder()
          .setToken(subscription.getAuthSecret())
//          .setWebpushConfig(WebpushConfig.builder()
//              .setNotification(WebpushNotification.builder()
//                  .addAction(new Action("action", "action"))  
//                  .build())
//              .build())
//          .setAndroidConfig(AndroidConfig.builder()
//              .setNotification(AndroidNotification.builder()
//                  .setClickAction("Action")
//                  .build())
//              .build())
//          .setApnsConfig(ApnsConfig.builder()
//              .setAps(Aps.builder()
//                  .setCategory("Action")
//                  .build())
          .setNotification(
              Notification.builder()
                .setTitle(message.getTitle())
                .setBody(message.getContent())
                .build());

      if (additionalData != null) {
        messageBuilder.putAllData(additionalData);
      }
      
      String response = FirebaseMessaging.getInstance()
          .sendAsync(messageBuilder.build())
          .get();
      
      System.out.println(response);
    } catch (InterruptedException | ExecutionException e) {
      subscriptionService.delete(subscription.getId());
      errorService.sendErrorMail("Push error for subscription: " 
          + subscription.getAuthSecret()
          + " with error: " 
          + e.getStackTrace());
    }
  }
}
