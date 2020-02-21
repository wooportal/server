package de.codeschluss.wooportal.server.integration.push;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import de.codeschluss.wooportal.server.components.page.PageEntity;
import de.codeschluss.wooportal.server.components.push.FirebasePushService;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.components.topic.TopicService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import java.util.concurrent.CompletableFuture;
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
public class PushServicePushNewPageTest {
  
  @Autowired
  private PushService pushService;
  
  @Autowired
  private TopicService topicService;

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
  public void pushNewPageOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-400000000000";
    String topicId = "00000000-0000-0000-0014-400000000000";
    TopicEntity topic = topicService.getById(topicId);
    PageEntity newPage = 
        createPage(topic);
    
    CompletableFuture<String> result = pushService.pushNewPage(newPage);
    result.get();
    
    assertArugments(
        subscriptionId, 
        getTopicTitle(topic, "de"),
        PushService.messageContentNewPage);
  }
  
  @Test
  public void pushNewPageWithDefaultTranslationOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-100000000000";
    String topicId = "00000000-0000-0000-0014-100000000000";
    TopicEntity topic = topicService.getById(topicId);
    PageEntity newPage = 
        createPage(topic);
    
    CompletableFuture<String> result = pushService.pushNewPage(newPage);
    result.get();
    
    assertArugments(
        subscriptionId, 
        getTopicTitle(topic, "de"),
        translationString);
  }
  
  private String getTopicTitle(TopicEntity topic, String lang) {
    return topic.getTranslatables().stream()
        .filter(t -> 
            t.getLanguage().getLocale().equalsIgnoreCase(lang))
        .findFirst().get()
        .getName();
    
  }
  
  @Test
  public void pushNewPageWithTranslationOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-500000000000";
    String topicId = "00000000-0000-0000-0014-500000000000";
    TopicEntity topic = topicService.getById(topicId);
    PageEntity newPage = 
        createPage(topic);
    
    pushService.pushNewPage(newPage);
    
    CompletableFuture<String> result = pushService.pushNewPage(newPage);
    result.get();
    
    assertArugments(
        subscriptionId, 
        "pagePushTest",
        translationString);
  }

  private PageEntity createPage(
      TopicEntity topic) {
    PageEntity page = new PageEntity();
    
    page.setTopic(topic);
    return page;
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

}
