package de.codeschluss.wooportal.server.components.push;

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
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
  
  public static final String messageContentNewActivityOrgaSub =
      "Hat eine neue Veranstaltung erstellt";
  
  public static final String messageContentNewActivitySimilar =
      "Eine Veranstaltung könnte dir gefallen";
  
  public static final String messageContentNewBlog =
      "Hat einen neuen Blog geschrieben";
  
  public static final String messageContentNewPage =
      "Es gibt einen neuen Beitrag zu diesem Thema";
  
  public static final String messageTitleActivityReminder =
      "Veranstaltungserinnerung";
  
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
      ScheduleService scheduleService,
      OrganisationService orgaService) {
    this.firebasePushService = firebasePushService;
    this.subscriptionService = subscriptionService;
    this.languageService = languageService;
    this.translationService = translationService;
    this.scheduleService = scheduleService;
    this.orgaService = orgaService;
    
    this.dateFormatter = new SimpleDateFormat("HH:mm");
  }

  /**
   * Push manual.
   *
   * @param message the message
   */
  public void pushMessage(MessageDto message) {
    if (message.getRoute() == null || message.getRoute().isEmpty()) {
      pushNews(message);
    } else {
      pushSingleContent(message);
    }
  }
  
  /**
   * Push news.
   *
   * @param message the message
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
   * Push single content.
   *
   * @param message the message
   */
  public void pushSingleContent(MessageDto message) {
    List<SubscriptionEntity> subscriptions = subscriptionService.getBySingleContentSub();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      Map<String, Map<String, String>> translatedMessages = new HashMap<>();
      for (SubscriptionEntity subscription : subscriptions) {
        Map<String, String> data = new HashMap<>();
        data.put("route", message.getRoute());
        translateAndPush(subscription, message, data, translatedMessages);
      }
    }
  }
  
  private void translateAndPush(
      SubscriptionEntity subscription, 
      MessageDto message,
      Map<String, String> additionalData,
      Map<String, Map<String, String>> translatedMessages) {
    String messageLocale = languageService.getCurrentRequestLocales().get(0);
    MessageDto messageToSend = message;
    if (needsTranslation(subscription, messageLocale)) {
      messageToSend = translateMultiple(subscription, messageLocale, message, translatedMessages);
    }
    firebasePushService.sendPush(subscription, messageToSend, additionalData);
  }
  
  private MessageDto translateMultiple(
      SubscriptionEntity subscription, 
      String messageLocale,
      MessageDto message,
      Map<String, Map<String, String>> translatedMessages) {
    Map<String, String> translations = translatedMessages.get(subscription.getLocale());
    MessageDto translatedMessage = new MessageDto();
    if (translations != null) {
      translatedMessage.setTitle(translations.get("title"));
      translatedMessage.setTitle(translations.get("content"));
      return translatedMessage;
    }
    
    Map<String, String> currentTranslations = new HashMap<>();
    String titleTranslation = translationService.translate(
        subscription.getLocale(), 
        messageLocale, 
        message.getTitle());
    translatedMessage.setTitle(titleTranslation);
    currentTranslations.put("title", titleTranslation);
    
    String contentTranslation = translationService.translate(
        subscription.getLocale(), 
        messageLocale, 
        message.getContent());
    translatedMessage.setContent(contentTranslation);
    currentTranslations.put("content", contentTranslation);
    
    translatedMessages.put(subscription.getLocale(), currentTranslations);
    return translatedMessage;
  }
  
  /**
   * Push activity reminders.
   */
  @Async
  public CompletableFuture<String> pushActivityReminders() {
    List<SubscriptionEntity> subscriptions = subscriptionService.getByActivityReminderTypeSub();
    if (subscriptions != null && !subscriptions.isEmpty()) {
      Map<String, String> translatedMessages = new HashMap<>();
      for (SubscriptionEntity subscription : subscriptions) {
        Map<String, String> data = new HashMap<>();
        
        MessageDto message = new MessageDto();
        String messageContent = null;
        for (ActivityEntity activity : subscription.getActivitySubscriptions()) {
          List<ScheduleEntity> schedules = scheduleService.getNextByActivityId(activity.getId());
          if (schedules != null && !schedules.isEmpty()) {
            if (messageContent == null) {
              messageContent = "";
              data.put("route", activity.selfLink().getHref());
            } else {
              messageContent = messageContent + ", ";  
            }
            
            String messageTitle = messageTitleActivityReminder;
            if (needsTranslation(subscription, languageService.getDefaultLocale())) {
              messageTitle = translateSingle(
                  subscription, 
                  languageService.getDefaultLocale(), 
                  messageTitle, 
                  translatedMessages);
            }
            message.setTitle(messageTitle);
            
            String activityTitle = getActivityTitle(activity, subscription.getLocale());
            String time = dateFormatter.format(schedules.get(0).getStartDate());
            messageContent = messageContent + activityTitle + ": " + time;
          }
        }
        
        if (messageContent != null && !messageContent.isEmpty()) {
          message.setContent(messageContent);
          firebasePushService.sendPush(subscription, message, data); 
        }
      }
    }
    
    return CompletableFuture.completedFuture("done");
  }

  private String getActivityTitle(ActivityEntity activity, String language) {
    Optional<ActivityTranslatablesEntity> translatable = 
        getOptionalActivityTranslatable(activity, language);
    
    if (translatable.isPresent()) {
      if (language.equalsIgnoreCase(languageService.getDefaultLocale())) {
        return translatable.get().getName();
      }
      return translatable.get().getName().replaceFirst("\\(.*?\\)","").trim();
    } else {
      translatable = getOptionalActivityTranslatable(
          activity, languageService.getDefaultLocale());
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
  public CompletableFuture<String> pushNewActivity(ActivityEntity newActivity) {
    List<SubscriptionEntity> orgaSubscriptions = 
        subscriptionService.getByNewContentAndOrgaSub(newActivity.getOrganisationId());
    
    List<SubscriptionEntity> activitySubscriptions = filterDuplicates(
        subscriptionService.getByNewContentAndActivitySub(), 
        orgaSubscriptions);
    
    pushNewActivityForOrgaSub(orgaSubscriptions, newActivity);
    pushNewActivityForSimilar(activitySubscriptions, newActivity);
    
    return CompletableFuture.completedFuture("done");
  }

  private void pushNewActivityForOrgaSub(
      List<SubscriptionEntity> orgaSubscriptions,
      ActivityEntity newActivity) {
    if (orgaSubscriptions != null && !orgaSubscriptions.isEmpty()) {
      Map<String, String> translatedMessages = new HashMap<>();
      for (SubscriptionEntity subscription : orgaSubscriptions) {
        String messageContentToSend = messageContentNewActivityOrgaSub;
        if (needsTranslation(subscription, languageService.getDefaultLocale())) {
          messageContentToSend = translateSingle(
              subscription, 
              languageService.getDefaultLocale(), 
              messageContentToSend, 
              translatedMessages);
        }
        Map<String, String> data = new HashMap<>();
        data.put("route", newActivity.selfLink().getHref());
        
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
      for (SubscriptionEntity subscription : activitySubscriptions) {
        List<ActivityEntity> subscribedActivities = subscription.getActivitySubscriptions();
        if (subscribedActivities != null && !subscribedActivities.isEmpty()) {
          for (ActivityEntity activity : subscribedActivities) {
            if (similar(activity, newActivity)) {
              String messageContentToSend = messageContentNewActivitySimilar;
              if (needsTranslation(subscription, languageService.getDefaultLocale())) {
                messageContentToSend = translateSingle(
                    subscription, 
                    languageService.getDefaultLocale(), 
                    messageContentToSend, 
                    translatedMessages);
              }
              Map<String, String> data = new HashMap<>();
              data.put("route", newActivity.selfLink().getHref());
              
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
   */
  @Async
  public CompletableFuture<String> pushNewBlog(BlogEntity newBlog) {
    List<SubscriptionEntity> subscriptions = 
        subscriptionService.getByNewContentAndBloggerSub(newBlog.getBlogger().getId());
    if (subscriptions != null && !subscriptions.isEmpty()) {
      Map<String, String> translatedMessages = new HashMap<>();
      for (SubscriptionEntity subscription : subscriptions) {
        String messageContentToSend = messageContentNewBlog;
        if (needsTranslation(subscription, languageService.getDefaultLocale())) {
          messageContentToSend = translateSingle(
              subscription, 
              languageService.getDefaultLocale(), 
              messageContentToSend, 
              translatedMessages);
        }
        Map<String, String> data = new HashMap<>();
        data.put("route", newBlog.selfLink().getHref());
       
        MessageDto message = new MessageDto(
            newBlog.getAuthor(), messageContentToSend);
        
        firebasePushService.sendPush(subscription, message, data);
      }
    }
    
    return CompletableFuture.completedFuture("done");
  }
  
  /**
   * Push new page.
   *
   * @param newPage the new page
   */
  @Async
  public CompletableFuture<String> pushNewPage(PageEntity newPage) {
    List<SubscriptionEntity> subscriptions = 
        subscriptionService.getByNewContentAndTopicSub(newPage.getTopic().getId());
    if (subscriptions != null && !subscriptions.isEmpty()) {
      Map<String, String> translatedMessages = new HashMap<>();
      for (SubscriptionEntity subscription : subscriptions) {
        String messageContentToSend = messageContentNewPage;
        if (needsTranslation(subscription, languageService.getDefaultLocale())) {
          messageContentToSend = translateSingle(
              subscription, 
              languageService.getDefaultLocale(), 
              messageContentToSend, 
              translatedMessages);
        }
        Map<String, String> data = new HashMap<>();
        data.put("route", newPage.selfLink().getHref());
       
        MessageDto message = new MessageDto(
            getTopicName(newPage.getTopic(), subscription.getLocale()), messageContentToSend);
        
        firebasePushService.sendPush(subscription, message, data);
      }
    }
    
    return CompletableFuture.completedFuture("done");
  }

  private String getTopicName(TopicEntity topic, String language) {
    Optional<TopicTranslatablesEntity> translatable = 
        getOptionalTopicTranslatable(topic, language);
    
    if (translatable.isPresent()) {
      if (language.equalsIgnoreCase(languageService.getDefaultLocale())) {
        return translatable.get().getName();
      }
      return translatable.get().getName().replaceFirst("\\(.*?\\)","").trim();
    } else {
      translatable = getOptionalTopicTranslatable(topic, languageService.getDefaultLocale());
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
  
  private boolean needsTranslation(SubscriptionEntity subscription, String messageLocale) {
    if (subscription.getLocale() == null || subscription.getLocale().isEmpty()) {
      subscription.setLocale(languageService.getDefaultLocale());
    }
    return subscription.getLocale().equalsIgnoreCase(messageLocale);
  }

  private String translateSingle(SubscriptionEntity subscription, String messageLocale,
      String messageContent, Map<String, String> translatedMessages) {
    String translation = translatedMessages.get(subscription.getLocale());
    if (translation != null) {
      return translation;
    }
    
    String currentTranslation = translationService.translate(
        subscription.getLocale(), 
        messageLocale, 
        messageContent);    
    translatedMessages.put(subscription.getLocale(), currentTranslation);
    return currentTranslation;
  }
}
