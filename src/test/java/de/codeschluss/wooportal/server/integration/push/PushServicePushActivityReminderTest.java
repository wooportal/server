package de.codeschluss.wooportal.server.integration.push;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.activity.translations.ActivityTranslatableRepository;
import de.codeschluss.wooportal.server.components.activity.translations.ActivityTranslatablesEntity;
import de.codeschluss.wooportal.server.components.address.AddressService;
import de.codeschluss.wooportal.server.components.category.CategoryService;
import de.codeschluss.wooportal.server.components.provider.ProviderService;
import de.codeschluss.wooportal.server.components.push.FirebasePushService;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleService;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import javax.naming.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.AtLeast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushServicePushActivityReminderTest {
  
  @Autowired
  private PushService pushService;
  
  @Autowired
  private ScheduleService scheduleService;
  
  @Autowired
  private ActivityService activityService;
  
  @Autowired 
  private SubscriptionService subscriptionService;

  @Autowired
  private ProviderService providerService;
  
  @Autowired
  private AddressService addressService;
  
  @Autowired
  private CategoryService categoryService;
  
  @Autowired
  private LanguageService languageService;
  
  @Autowired
  private ActivityTranslatableRepository translatableRepo;

  @MockBean
  private FirebasePushService firebasePushService;
  
  @MockBean
  private TranslationService translationService;
  
  private final String translationString = "translation";
  
  /**
   * Sets the up.
   *
   * @throws Throwable the throwable
   */
  @Before
  public void setUp() throws Throwable {
    willDoNothing().given(
        firebasePushService).sendPush(Mockito.any(), Mockito.any(), Mockito.any());
    given(translationService.translate(Mockito.any(), Mockito.any(), Mockito.any()))
        .willReturn(translationString);
  }

  @Test
  public void pushActivityReminderSingleOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-400000000000";
    ActivityEntity activity = createActivity(
        "pushActivityReminderSingleOk",
        "00000000-0000-0000-0013-400000000000");
    
    subscriptionService.addActivitySubscription(subscriptionId, activity);
    SubscriptionEntity sub =  subscriptionService.getById(subscriptionId);
    
    CompletableFuture<String> result = pushService.pushActivityReminders();
    result.get();
    activityService.delete(activity.getId());
    
    assertArugments(
        subscriptionId, 
        PushService.messageTitleActivityReminder, 
        buildContentSingle(activity, sub.getLanguage()));
  }
  
  private String buildContentSingle(
      ActivityEntity activity,
      String lang) {
    String time = getTime(activity.getSchedules().get(0).getStartDate());
    
    String name = activity.getTranslatables().stream()
        .filter(t -> 
            t.getLanguage().getLocale().equalsIgnoreCase(lang))
        .findFirst().get()
        .getName();
    
    return name + ": " + time;
  }
  
  @Test
  public void pushActivityReminderSingleWithTranslationsOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-500000000000";
    String name = "pushActivityReminderSingleWithTranslationsOk";
    String translatableName = "( machine translated ) " + name;
    ActivityEntity activity = createActivity(
        translatableName,
        "00000000-0000-0000-0013-100000000000");
    
    subscriptionService.addActivitySubscription(subscriptionId, activity);
    
    CompletableFuture<String> result = pushService.pushActivityReminders();
    result.get();
    activityService.delete(activity.getId());
    
    assertArugments(
        subscriptionId, 
        translationString, 
        name + ": " + getTime(activity.getSchedules().get(0).getStartDate()));
  }
  
  private void assertArugments(
      String subscriptionId, 
      String title,
      String content) {
    then(this.firebasePushService)
        .should(new AtLeast(1))
        .sendPush(
            ArgumentMatchers.argThat(subscriptionParam -> 
                subscriptionParam.getId().equals(subscriptionId)),
            ArgumentMatchers.argThat(messageParam ->
                messageParam.getTitle().equals(title)
                && messageParam.getContent().equals(content)),
            Mockito.any());
  }
  
  @Test
  public void pushActivityReminderMultipleOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-400000000000";
    String deLanguageId = "00000000-0000-0000-0013-400000000000";
    ActivityEntity activity1 = createActivity(
        "activity1",
        deLanguageId);
    
    ActivityEntity activity2 = createActivity(
        "activity2",
        deLanguageId);
    
    subscriptionService.addActivitySubscription(subscriptionId, activity1);
    subscriptionService.addActivitySubscription(subscriptionId, activity2);
    
    SubscriptionEntity sub =  subscriptionService.getById(subscriptionId);
    
    CompletableFuture<String> result = pushService.pushActivityReminders();
    result.get();
    activityService.delete(activity1.getId());
    
    assertArugmentsMultiple(
        subscriptionId, 
        PushService.messageTitleActivityReminder, 
        activity1, 
        activity2, 
        sub.getLanguage());
  }

  private String buildContentMultiple(ActivityEntity activity1, ActivityEntity activity2,
      String language) {
    String contentActivity1 = buildContentSingle(activity1, language);
    String contentActivity2 = buildContentSingle(activity2, language);
    
    return contentActivity1 + ", " + contentActivity2;
  }
  
  private void assertArugmentsMultiple(
      String subscriptionId, 
      String title,
      ActivityEntity activity1,
      ActivityEntity activity2,
      String language) {
    then(this.firebasePushService)
        .should(new AtLeast(1))
        .sendPush(
            ArgumentMatchers.argThat(subscriptionParam -> 
                subscriptionParam.getId().equals(subscriptionId)),
            ArgumentMatchers.argThat(messageParam ->
                messageParam.getTitle().equals(title)
                && (
                    messageParam.getContent().equals(buildContentMultiple(
                        activity1, activity2, language))
                    || messageParam.getContent().equals(buildContentMultiple(
                        activity2, activity1, language))
                    )),
            Mockito.any());
  }

  private ActivityEntity createActivity(
      String name,
      String lang) throws ServiceUnavailableException {
    ActivityEntity activity = new ActivityEntity();
        
    activity.setMail("createActivity");
    activity.setPhone("123456789");
    activity.setProvider(providerService.getById("00000000-0000-0000-0009-100000000000"));
    activity.setAddress(addressService.getById("00000000-0000-0000-0006-100000000000"));
    activity.setCategory(categoryService.getById("00000000-0000-0000-0007-100000000000"));
    activity = activityService.add(activity);
    
    ScheduleEntity schedule = new ScheduleEntity();
    schedule.setActivity(activity);
    schedule.setStartDate(nowPlusHours(2));
    schedule.setStartDate(nowPlusHours(3));
    List<ScheduleEntity> schedules = new ArrayList<>();
    schedules.add(schedule);
    scheduleService.addAll(schedules);
    activity.setSchedules(schedules);
    
    ActivityTranslatablesEntity translatable = new ActivityTranslatablesEntity();
    translatable.setName(name);
    translatable.setDescription("description");
    translatable.setLanguage(languageService.getById(lang));
    translatable.setParent(activity);
    Set<ActivityTranslatablesEntity> translatables = new HashSet<>();
    translatables.add(translatable);
    translatableRepo.saveAll(translatables);
    activity.setTranslatables(translatables);
    
    return activity;
  }
  
  private Date nowPlusHours(int hours) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.HOUR, hours);
    return cal.getTime();
  }

  private String getTime(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    
    int hours = cal.get(Calendar.HOUR_OF_DAY);
    int minutes = cal.get(Calendar.MINUTE);
    
    return hours + ":" + minutes;
  }

}
