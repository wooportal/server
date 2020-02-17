package de.codeschluss.wooportal.server.components.push;

import com.google.firebase.messaging.FirebaseMessagingException;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleService;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * The Class PushService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class PushService {

  /** The push service. */
  private final FirebasePushService firebasePushService;
  
  /** The subscription service. */
  private final SubscriptionService subscriptionService;
  
  /** The language service. */
  private final LanguageService languageService;
  
  /** The translation service. */
  private final TranslationService translationService;
  
  /** The schedule service. */
  private final ScheduleService scheduleService;
  
  private Map<String, Map<String,String>> translatedMessages;
  
  /**
   * Instantiates a new push service.
   *
   * @param firebasePushService the firebase push service
   * @param subscriptionService the subscription service
   * @param languageService the language service
   */
  public PushService(
      FirebasePushService firebasePushService,
      SubscriptionService subscriptionService,
      LanguageService languageService,
      TranslationService translationService,
      ScheduleService scheduleService) {
    this.firebasePushService = firebasePushService;
    this.subscriptionService = subscriptionService;
    this.languageService = languageService;
    this.translationService = translationService;
    this.scheduleService = scheduleService;
    
    this.translatedMessages = new HashMap<>();
  }

  /**
   * Push message.
   *
   * @param message the message
   * @throws FirebaseMessagingException the firebase messaging exception
   */
  public void pushNews(MessageDto message) {
    List<SubscriptionEntity> subscriptions = subscriptionService.getByNewsSub();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      for (SubscriptionEntity subscription : subscriptions) {
        translateAndPush(subscription, message, null);
      }
    }
  }
  
  /**
   * Push content.
   *
   * @param message the message
   * @param link the link
   */
  public void pushContent(MessageDto message, String link) {
    List<SubscriptionEntity> subscriptions = subscriptionService.getBySingleContentSub();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      for (SubscriptionEntity subscription : subscriptions) {
        Map<String, String> data = new HashMap<>();
        data.put("link", link);
        translateAndPush(subscription, message, data);
      }
    }
  }
  
  private void translateAndPush(
      SubscriptionEntity subscription, 
      MessageDto message,
      Map<String, String> additionalData) {
    String messageLang = languageService.getCurrentRequestLocales().get(0);
    if (!subscription.getLanguage().equalsIgnoreCase(messageLang)) {
      translateMessage(subscription, messageLang, message);
    }
    firebasePushService.sendPush(subscription, message, additionalData);
  }

  private void translateMessage(SubscriptionEntity subscription, String messageLang,
      MessageDto message) {
    Map<String, String> translations = translatedMessages.get(subscription.getLanguage());
    if (translations != null) {
      message.setTitle(translations.get("title"));
      message.setTitle(translations.get("content"));
    } else {
      Map<String, String> currentTranslations = new HashMap<>();
      String titleTranslation = translationService.translate(
          subscription.getLanguage(), 
          messageLang, 
          message.getTitle());
      message.setTitle(titleTranslation);
      currentTranslations.put("title", titleTranslation);
      
      String contentTranslation = translationService.translate(
          subscription.getLanguage(), 
          messageLang, 
          message.getContent());
      message.setTitle(contentTranslation);
      currentTranslations.put("content", contentTranslation);
      
      translatedMessages.put(subscription.getLanguage(), currentTranslations);
    }
  }
  
  /**
   * Push activity reminders.
   */
  public void pushActivityReminders() {
    List<SubscriptionEntity> subscriptions = subscriptionService.getByActivityFollowSub();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      for (SubscriptionEntity subscription : subscriptions) {
        subscription.getActivityLikes().stream().forEach(activity -> {
          List<ScheduleEntity> schedules = scheduleService.getNextByActivityId(activity.getId());
          if (schedules != null && !schedules.isEmpty()) {
            //TODO
          }
        });
        translateAndPush(subscription, null, null);
      }
    }
  }
}
