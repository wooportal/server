package de.codeschluss.wooportal.server.integration.push;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.push.FirebasePushService;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.AtLeast;
import org.mockito.internal.verification.Times;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushServicePushNewActivityTest {
  
  @Autowired
  private PushService pushService;
  
  @Autowired
  private OrganisationService orgaService;

  @MockBean
  private FirebasePushService firebasePushService;
  
  @MockBean
  private TranslationService translationService;
  
  private final String translationString = "translation";
  
  /**
   * Sets the up.
   */
  @Before
  public void setUp() {
    willDoNothing().given(
        this.firebasePushService).sendPush(Mockito.any(), Mockito.any(), Mockito.any());
    given(this.translationService.translate(Mockito.any(), Mockito.any(), Mockito.any()))
        .willReturn(translationString);
  }

  @Test
  public void pushNewActivitySubOrgaOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-400000000000";
    String name = "pushNewActivitySubOrgaOk";
    String categoryId = "00000000-0000-0000-0007-300000000000";
    OrganisationEntity orga = orgaService.getById("00000000-0000-0000-0008-300000000000");
    ActivityEntity newActivity = 
        createActivity(name, categoryId, orga.getId());
    
    CompletableFuture<String> result = pushService.pushNewActivity(newActivity);
    result.get();
    
    assertArugments(
        new AtLeast(1),
        subscriptionId, 
        orga.getName(),
        PushService.messageContentNewActivityOrgaSub);
  }
  
  @Test
  public void pushNewActivitySubOrgaTranslationsOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-100000000000";
    String name = "pushNewActivitySubOrgaTranslationsOk";
    String categoryId = "00000000-0000-0000-0007-300000000000";
    OrganisationEntity orga = orgaService.getById("00000000-0000-0000-0008-100000000000");
    ActivityEntity newActivity = 
        createActivity(name, categoryId, orga.getId());
    
    CompletableFuture<String> result = pushService.pushNewActivity(newActivity);
    result.get();
    
    assertArugments(
        new AtLeast(1),
        subscriptionId, 
        orga.getName(),
        translationString);
  }
  
  @Test
  public void pushNewActivitySubActivityOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-400000000000";
    String name = "pushNewActivitySubActivityOk";
    String categoryId = "00000000-0000-0000-0007-200000000000";
    OrganisationEntity orga = orgaService.getById("00000000-0000-0000-0008-200000000000");
    ActivityEntity newActivity = 
        createActivity(name, categoryId, orga.getId());
    
    CompletableFuture<String> result = pushService.pushNewActivity(newActivity);
    result.get();
    
    assertArugments(
        new AtLeast(1),
        subscriptionId, 
        orga.getName(),
        PushService.messageContentNewActivitySimilar);
  }
  
  @Test
  public void pushNewActivityDuplicateCheckOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-400000000000";
    String name = "pushNewActivitySubActivityOk";
    String categoryId = "00000000-0000-0000-0007-200000000000";
    OrganisationEntity orga = orgaService.getById("00000000-0000-0000-0008-300000000000");
    ActivityEntity newActivity = 
        createActivity(name, categoryId, orga.getId());
    
    CompletableFuture<String> result = pushService.pushNewActivity(newActivity);
    result.get();
    
    assertArugments(
        new Times(1),
        subscriptionId, 
        orga.getName(),
        PushService.messageContentNewActivityOrgaSub);
  }

  private ActivityEntity createActivity(
      String name,
      String categoryId,
      String organisationId) {
    ActivityEntity activity = new ActivityEntity();
    
    String addressId = "00000000-0000-0000-0006-100000000000";
    
    activity.setName(name);
    activity.setMail("createActivity");
    activity.setPhone("123456789");
    activity.setAddressId(addressId);
    activity.setCategoryId(categoryId);
    activity.setOrganisationId(organisationId);
    
    return activity;
  }
  
  private void assertArugments(
      VerificationMode mode,
      String subscriptionId, 
      String title,
      String content) {
    then(this.firebasePushService)
        .should()
        .sendPush(
            ArgumentMatchers.argThat(subscriptionParam -> 
                subscriptionParam.getId().equals(subscriptionId)),
            ArgumentMatchers.argThat(messageParam ->
                messageParam.getTitle().equals(title)
                && messageParam.getContent().equals(content)),
            Mockito.any());
  }

}
