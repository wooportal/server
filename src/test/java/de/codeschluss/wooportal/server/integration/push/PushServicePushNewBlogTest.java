package de.codeschluss.wooportal.server.integration.push;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerService;
import de.codeschluss.wooportal.server.components.push.FirebasePushService;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
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
public class PushServicePushNewBlogTest {
  
  @Autowired
  private PushService pushService;
  
  @Autowired
  private BloggerService bloggerService;

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
  public void pushNewBlogOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-400000000000";
    String bloggerId = "00000000-0000-0000-0015-300000000000";
    BloggerEntity blogger = bloggerService.getById(bloggerId);
    BlogEntity newBlog = 
        createBlog(blogger);
    
    pushService.pushNewBlog(newBlog);
    
    assertArugments(
        subscriptionId,
        blogger.getUser().getName(),
        PushService.messageContentNewBlog,
        newBlog.selfLink().getHref());
  }
  
  @Test
  public void pushNewBlogWithTranslationOk() throws Exception {
    String subscriptionId = "00000000-0000-0000-0020-100000000000";
    String bloggerId = "00000000-0000-0000-0015-100000000000";
    BloggerEntity blogger = bloggerService.getById(bloggerId);
    BlogEntity newBlog = 
        createBlog(blogger);
    
    pushService.pushNewBlog(newBlog);
    
    assertArugments(
        subscriptionId,
        blogger.getUser().getName(),
        translationString,
        newBlog.selfLink().getHref());
  }

  private BlogEntity createBlog(
      BloggerEntity blogger) {
    BlogEntity blog = new BlogEntity();
    
    blog.setAuthor("test");
    blog.setBlogger(blogger);
    return blog;
  }
  
  private void assertArugments(
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
                d.getKey().equals("link") && d.getValue().equals(
                    link))));
  }

}
