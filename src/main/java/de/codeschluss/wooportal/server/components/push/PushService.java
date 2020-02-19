package de.codeschluss.wooportal.server.components.push;

import com.google.firebase.messaging.FirebaseMessagingException;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.activity.translations.ActivityTranslatablesEntity;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.page.PageEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleService;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.components.topic.translations.TopicTranslatablesEntity;
import de.codeschluss.wooportal.server.core.i18n.TranslationsConfiguration;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Async;
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
  
  /** The orga service. */
  private final OrganisationService orgaService;
  
  /** The date formatter. */
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
      ScheduleService scheduleService,
      OrganisationService orgaService) {
    this.pushConfig = pushConfig;
    this.translationConfig = translationConfig;
    this.firebasePushService = firebasePushService;
    this.subscriptionService = subscriptionService;
    this.languageService = languageService;
    this.translationService = translationService;
    this.scheduleService = scheduleService;
    this.orgaService = orgaService;
    
    this.dateFormatter = new SimpleDateFormat("HH:mm");
  }

  /**
   * Push message.
   *
   * @param message the message
   * @throws FirebaseMessagingException the firebase messaging exception
   */
  @Async
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
  @Async
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
    MessageDto messageToSend = message;
    if (!subscription.getLanguage().equalsIgnoreCase(messageLang)) {
      messageToSend = translateMultiple(subscription, messageLang, message, translatedMessages);
    }
    firebasePushService.sendPush(subscription, messageToSend, additionalData);
  }

  private MessageDto translateMultiple(
      SubscriptionEntity subscription, 
      String messageLang,
      MessageDto message,
      Map<String, Map<String, String>> translatedMessages) {
    Map<String, String> translations = translatedMessages.get(subscription.getLanguage());
    MessageDto translatedMessage = new MessageDto();
    if (translations != null) {
      translatedMessage.setTitle(translations.get("title"));
      translatedMessage.setTitle(translations.get("content"));
      return translatedMessage;
    }
    
    Map<String, String> currentTranslations = new HashMap<>();
    String titleTranslation = translationService.translate(
        subscription.getLanguage(), 
        messageLang, 
        message.getTitle());
    translatedMessage.setTitle(titleTranslation);
    currentTranslations.put("title", titleTranslation);
    
    String contentTranslation = translationService.translate(
        subscription.getLanguage(), 
        messageLang, 
        message.getContent());
    translatedMessage.setContent(contentTranslation);
    currentTranslations.put("content", contentTranslation);
    
    translatedMessages.put(subscription.getLanguage(), currentTranslations);
    return translatedMessage;
  }
  
  /**
   * Push activity reminders.
   */
  @Async
  public void pushActivityReminders() {
    List<SubscriptionEntity> subscriptions = subscriptionService.getByActivityReminderTypeSub();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      for (SubscriptionEntity subscription : subscriptions) {
        Map<String, String> data = new HashMap<>();
        
        MessageDto message = new MessageDto();
        message.setTitle(pushConfig.getTypeActivityReminderTitle());
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
        getOptionalActivityTranslatable(activity, language);
    
    if (translatable.isPresent()) {
      if (language.equalsIgnoreCase(translationConfig.getDefaultLocale())) {
        return translatable.get().getName();
      }
      return translatable.get().getName().replaceFirst("\\(.*?\\)","");
    } else {
      translatable = getOptionalActivityTranslatable(
          activity, translationConfig.getDefaultLocale());
      if (translatable.isPresent()) {
        return translatable.get().getName();
      }
    }
    return null;
  }

  private Optional<ActivityTranslatablesEntity> getOptionalActivityTranslatable(
      ActivityEntity activity, String language) {
    return activity.getTranslatables().stream()
        .filter(t -> 
            t.getLanguage().getLocale().equalsIgnoreCase(language))
        .findFirst();
  }

  /**
   * Push new activity.
   *
   * @param newActivity the new activity
   */
  @Async
  public void pushNewActivity(ActivityEntity newActivity) {
    List<SubscriptionEntity> orgaSubscriptions = 
        subscriptionService.getByNewContentAndOrgaSub(newActivity.getOrganisationId());
    
    List<SubscriptionEntity> activitySubscriptions = filterDuplicates(
        subscriptionService.getByNewContentAndActivitySub(), 
        orgaSubscriptions);
    
    pushNewActivityForOrgaSub(orgaSubscriptions, newActivity);
    pushNewActivityForSimilar(activitySubscriptions, newActivity);
  }

  private void pushNewActivityForOrgaSub(
      List<SubscriptionEntity> orgaSubscriptions,
      ActivityEntity newActivity) {
    if (orgaSubscriptions != null && !orgaSubscriptions.isEmpty()) {
      Map<String, String> translatedMessages = new HashMap<>();
      String messageContent = "Hat eine neue Veranstaltung erstellt!";
      for (SubscriptionEntity subscription : orgaSubscriptions) {
        String messageContentToSend = messageContent;
        if (!subscription.getLanguage().equalsIgnoreCase(translationConfig.getDefaultLocale())) {
          messageContentToSend = translateSingle(
              subscription, 
              translationConfig.getDefaultLocale(), 
              messageContent, 
              translatedMessages);
        }
        Map<String, String> data = new HashMap<>();
        data.put("link", newActivity.selfLink().toString());
        
        OrganisationEntity orga = orgaService.getById(newActivity.getOrganisationId());
        MessageDto message = new MessageDto(
            orga.getName(), messageContentToSend);
        
        firebasePushService.sendPush(subscription, message, data);
      }
    }
  }
  
  private void pushNewActivityForSimilar(
      List<SubscriptionEntity> activitySubscriptions,
      ActivityEntity newActivity) {
    if (activitySubscriptions != null && !activitySubscriptions.isEmpty()) {
      Map<String, String> translatedMessages = new HashMap<>();
      String messageContent = "Eine Veranstaltung könnte dir gefallen!";
      for (SubscriptionEntity subscription : activitySubscriptions) {
        List<ActivityEntity> subscribedActivities = subscription.getActivitySubscriptions();
        if (subscribedActivities != null && !subscribedActivities.isEmpty()) {
          for (ActivityEntity activity : subscribedActivities) {
            if (similar(activity, newActivity)) {
              String messageContentToSend = messageContent;
              if (!subscription.getLanguage().equalsIgnoreCase(
                  translationConfig.getDefaultLocale())) {
                messageContentToSend = translateSingle(
                    subscription, 
                    translationConfig.getDefaultLocale(), 
                    messageContent, 
                    translatedMessages);
              }
              Map<String, String> data = new HashMap<>();
              data.put("link", newActivity.selfLink().toString());
              
              OrganisationEntity orga = orgaService.getById(newActivity.getOrganisationId());
              MessageDto message = new MessageDto(
                  orga.getName(), messageContentToSend);
              
              firebasePushService.sendPush(subscription, message, data);
            }
          }
        }
      }
    }
  }

  private boolean similar(ActivityEntity activity, ActivityEntity newActivity) {
    return activity.getCategory().getId().equals(newActivity.getCategoryId());
  }

  private List<SubscriptionEntity> filterDuplicates(List<SubscriptionEntity> activitySubscriptions,
      List<SubscriptionEntity> orgaSubscriptions) {
    return activitySubscriptions.stream().filter(actSub -> 
    orgaSubscriptions.stream().noneMatch(orgaSub -> 
        orgaSub.getId().equals(actSub.getId()))).collect(Collectors.toList());
  }

  /**
   * Push new blog.
   *
   * @param newBlog the new blog
   * @param bloggerId the blogger id
   */
  @Async
  public void pushNewBlog(BlogEntity newBlog, String bloggerId) {
    List<SubscriptionEntity> subscriptions = 
        subscriptionService.getByNewContentAndBloggerSub(bloggerId);
    if (subscriptions != null && !subscriptions.isEmpty()) {
      Map<String, String> translatedMessages = new HashMap<>();
      String messageContent = "Hat einen neuen Blog geschrieben!";
      for (SubscriptionEntity subscription : subscriptions) {
        String messageContentToSend = messageContent;
        if (!subscription.getLanguage().equalsIgnoreCase(translationConfig.getDefaultLocale())) {
          messageContentToSend = translateSingle(
              subscription, 
              translationConfig.getDefaultLocale(), 
              messageContent, 
              translatedMessages);
        }
        Map<String, String> data = new HashMap<>();
        data.put("link", newBlog.selfLink().toString());
       
        MessageDto message = new MessageDto(
            newBlog.getAuthor(), messageContentToSend);
        
        firebasePushService.sendPush(subscription, message, data);
      }
    }
  }
  
  /**
   * Push new page.
   *
   * @param newPage the new page
   * @param topic the topic
   */
  @Async
  public void pushNewPage(PageEntity newPage, TopicEntity topic) {
    List<SubscriptionEntity> subscriptions = 
        subscriptionService.getByNewContentAndTopicSub(topic.getId());
    if (subscriptions != null && !subscriptions.isEmpty()) {
      Map<String, String> translatedMessages = new HashMap<>();
      String messageContent = "Es gibt einen neuen Beitrag zu diesem Thema";
      for (SubscriptionEntity subscription : subscriptions) {
        String messageContentToSend = messageContent;
        if (!subscription.getLanguage().equalsIgnoreCase(translationConfig.getDefaultLocale())) {
          messageContentToSend = translateSingle(
              subscription, 
              translationConfig.getDefaultLocale(), 
              messageContent, 
              translatedMessages);
        }
        Map<String, String> data = new HashMap<>();
        data.put("link", newPage.selfLink().toString());
       
        MessageDto message = new MessageDto(
            getTopicName(topic, subscription.getLanguage()), messageContentToSend);
        
        firebasePushService.sendPush(subscription, message, data);
      }
    }
  }

  private String getTopicName(TopicEntity topic, String language) {
    Optional<TopicTranslatablesEntity> translatable = 
        getOptionalTopicTranslatable(topic, language);
    
    if (translatable.isPresent()) {
      if (language.equalsIgnoreCase(translationConfig.getDefaultLocale())) {
        return translatable.get().getName();
      }
      return translatable.get().getName().replaceFirst("\\(.*?\\)","");
    } else {
      translatable = getOptionalTopicTranslatable(topic, translationConfig.getDefaultLocale());
      if (translatable.isPresent()) {
        return translatable.get().getName();
      }
    }
    return null;
  }

  private Optional<TopicTranslatablesEntity> getOptionalTopicTranslatable(TopicEntity topic,
      String language) {
    return topic.getTranslatables().stream()
        .filter(t -> 
            t.getLanguage().getLocale().equalsIgnoreCase(language))
        .findFirst();
  }

  private String translateSingle(SubscriptionEntity subscription, String messageLang,
      String messageContent, Map<String, String> translatedMessages) {
    String translation = translatedMessages.get(subscription.getLanguage());
    if (translation != null) {
      return translation;
    }
    
    String currentTranslation = translationService.translate(
        subscription.getLanguage(), 
        messageLang, 
        messageContent);    
    translatedMessages.put(subscription.getLanguage(), currentTranslation);
    return translation;
  }
}
