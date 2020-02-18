package de.codeschluss.wooportal.server.components.push;

import com.google.firebase.messaging.FirebaseMessagingException;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.activity.translations.ActivityTranslatablesEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleService;
import de.codeschluss.wooportal.server.core.i18n.TranslationsConfiguration;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * The Class PushService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class PushService {
  
  /** The pushConfig. */
  private final PushConfig pushConfig;
  
  private final TranslationsConfiguration translationConfig;

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
  
  private final SimpleDateFormat dateFormatter;
  
  /**
   * Instantiates a new push service.
   *
   * @param firebasePushService the firebase push service
   * @param subscriptionService the subscription service
   * @param languageService the language service
   */
  public PushService(
      PushConfig pushConfig,
      TranslationsConfiguration translationConfig,
      FirebasePushService firebasePushService,
      SubscriptionService subscriptionService,
      LanguageService languageService,
      TranslationService translationService,
      ScheduleService scheduleService) {
    this.pushConfig = pushConfig;
    this.translationConfig = translationConfig;
    this.firebasePushService = firebasePushService;
    this.subscriptionService = subscriptionService;
    this.languageService = languageService;
    this.translationService = translationService;
    this.scheduleService = scheduleService;
    
    this.dateFormatter = new SimpleDateFormat("HH:mm");
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
      Map<String, Map<String, String>> translatedMessages = new HashMap<>();
      for (SubscriptionEntity subscription : subscriptions) {
        translateAndPush(subscription, message, null, translatedMessages);
      }
    }
  }
  
  /**
   * Push content.
   *
   * @param message the message
   * @param link the link
   */
  public void pushSingleContent(MessageDto message, String link) {
    List<SubscriptionEntity> subscriptions = subscriptionService.getBySingleContentSub();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      Map<String, Map<String, String>> translatedMessages = new HashMap<>();
      for (SubscriptionEntity subscription : subscriptions) {
        Map<String, String> data = new HashMap<>();
        data.put("link", link);
        translateAndPush(subscription, message, data, translatedMessages);
      }
    }
  }
  
  private void translateAndPush(
      SubscriptionEntity subscription, 
      MessageDto message,
      Map<String, String> additionalData,
      Map<String, Map<String, String>> translatedMessages) {
    String messageLang = languageService.getCurrentRequestLocales().get(0);
    if (!subscription.getLanguage().equalsIgnoreCase(messageLang)) {
      translateMessage(subscription, messageLang, message, translatedMessages);
    }
    firebasePushService.sendPush(subscription, message, additionalData);
  }

  private void translateMessage(
      SubscriptionEntity subscription, 
      String messageLang,
      MessageDto message,
      Map<String, Map<String, String>> translatedMessages) {
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
    List<SubscriptionEntity> subscriptions = subscriptionService.getByActivityTypeSub();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      for (SubscriptionEntity subscription : subscriptions) {
        Map<String, String> data = new HashMap<>();
        
        MessageDto message = new MessageDto();
        message.setTitle(pushConfig.getTypeActivityTitle());
        String messageContent = null;
        for (ActivityEntity activity : subscription.getActivitySubscriptions()) {
          List<ScheduleEntity> schedules = scheduleService.getNextByActivityId(activity.getId());
          if (schedules != null && !schedules.isEmpty()) {
            if (messageContent == null) {
              messageContent = "";
              data.put("link", activity.selfLink().toString());
            } else {
              messageContent = ", " + messageContent;  
            }
            
            String activityTitle = getActivityTitle(activity, subscription.getLanguage());
            String time = dateFormatter.format(schedules.get(0));
            messageContent = messageContent + activityTitle + ": " + time;
          }
        }
        
        if (messageContent != null && !messageContent.isEmpty()) {
          message.setContent(messageContent);
          firebasePushService.sendPush(subscription, message, data); 
        }
      }
    }
  }

  private String getActivityTitle(ActivityEntity activity, String language) {
    Optional<ActivityTranslatablesEntity> translatable = 
        getOptionalTranslatable(activity, language);
    
    if (translatable.isPresent()) {
      if (language.equalsIgnoreCase(translationConfig.getDefaultLocale())) {
        return translatable.get().getName();
      }
      return translatable.get().getName().replaceFirst("\\(.*?\\)","");
    } else {
      translatable = getOptionalTranslatable(activity, translationConfig.getDefaultLocale());
      if (translatable.isPresent()) {
        return translatable.get().getName();
      }
    }
    return null;
  }

  private Optional<ActivityTranslatablesEntity> getOptionalTranslatable(
      ActivityEntity activity, String language) {
    return activity.getTranslatables().stream()
        .filter(t -> 
            t.getLanguage().getLocale().equalsIgnoreCase(language))
        .findFirst();
  }
}
