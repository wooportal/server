package de.codeschluss.wooportal.server.integration.blog;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.blog.BlogController;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
public class BlogControllerUpdateTopicTest {

  @Autowired
  private BlogController controller;

  @Test
  @WithUserDetails("super@user")
  public void updateTopicSuperUserOk() throws URISyntaxException {
    StringPrimitive topicId = new StringPrimitive("00000000-0000-0000-0014-100000000000");
    String blogId = "00000000-0000-0000-0016-200000000000";

    controller.updateTopic(blogId, topicId);

    assertContaining(blogId, topicId);
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void updateTopicBadblog() throws URISyntaxException {
    StringPrimitive topicId = new StringPrimitive("00000000-0000-0000-0014-200000000000");
    String blogId = "00000000-0000-0000-0016-XX0000000000";

    controller.updateTopic(blogId, topicId);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void updateTopicBadTopic() throws URISyntaxException {
    StringPrimitive topicId = new StringPrimitive("00000000-0000-0000-0014-XX0000000000");
    String blogId = "00000000-0000-0000-0016-200000000000";

    controller.updateTopic(blogId, topicId);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void updateProviderUserDenied() throws URISyntaxException {
    StringPrimitive topicId = new StringPrimitive("00000000-0000-0000-0014-100000000000");
    String blogId = "00000000-0000-0000-0016-100000000000";

    controller.updateTopic(blogId, topicId);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void updateNoUserDenied() throws URISyntaxException {
    StringPrimitive topicId = new StringPrimitive("00000000-0000-0000-0014-100000000000");
    String blogId = "00000000-0000-0000-0016-100000000000";

    controller.updateTopic(blogId, topicId);
  }
  
  @SuppressWarnings("unchecked")
  private void assertContaining(String blogId, StringPrimitive topicId) {
    Resource<TopicEntity> result = (Resource<TopicEntity>) controller.readTopic(blogId)
        .getBody();
    assertThat(result.getContent().getId()).isEqualTo(topicId.getValue());
  }

}
