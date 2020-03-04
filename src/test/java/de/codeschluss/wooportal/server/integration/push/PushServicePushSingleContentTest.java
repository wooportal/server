package de.codeschluss.wooportal.server.integration.push;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import de.codeschluss.wooportal.server.components.push.FirebasePushService;
import de.codeschluss.wooportal.server.components.push.MessageDto;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
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
public class PushServicePushSingleContentTest {
  
  @Autowired
  private PushService pushService;

  @MockBean
  private FirebasePushService firebasePushService;
  
  @MockBean
  private TranslationService translationService;
  
  @Test
  public void pushSingleContentOk() throws Exception {
    String title = "test";
    String content = "test";
    String link = "url";
    MessageDto message = new MessageDto(title, content, link);
    String translation = "translation";
    String subscriptionId = "00000000-0000-0000-0020-400000000000";

    
    prepareMocks(translation);
    
    pushService.pushSingleContent(message);
    
    assertArguments(
        subscriptionId, 
        title, 
        content, 
        link);
  }
  
  @Test
  public void pushNewsWithTranslationOk() throws Exception {
    String title = "test";
    String content = "test";
    String link = "url";
    MessageDto message = new MessageDto(title, content, link);
    String translation = "translation";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    
    prepareMocks(translation);
    
    pushService.pushSingleContent(message);
    
    assertArguments(
        subscriptionId, 
        translation, 
        translation, 
        link);
  }

  private void prepareMocks(String translation) {
    willDoNothing().given(
        this.firebasePushService).sendPush(Mockito.any(), Mockito.any(), Mockito.any());
    given(this.translationService.translate(Mockito.any(), Mockito.any(), Mockito.any()))
        .willReturn(translation);
  }
  
  private void assertArguments(
      String subscriptionId, 
      String title, 
      String content, 
      String link) {
    then(this.firebasePushService)
        .should(new AtLeast(1))
        .sendPush(
            ArgumentMatchers.argThat(subscriptionParam -> 
                subscriptionParam.getId().equals(subscriptionId)),
            ArgumentMatchers.argThat(messageParam -> 
                messageParam.getTitle().equals(title)
                && messageParam.getContent().equals(content)),
            ArgumentMatchers.argThat(dataParam -> 
                dataParam.entrySet().stream().anyMatch(d -> 
                    d.getKey().equals("route") && d.getValue().equals(link))));
  }

}
