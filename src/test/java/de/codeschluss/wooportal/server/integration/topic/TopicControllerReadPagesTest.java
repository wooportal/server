package de.codeschluss.wooportal.server.integration.topic;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.topic.TopicController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicControllerReadPagesTest {

  @Autowired
  private TopicController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void readPagesOk() {
    String topicId = "00000000-0000-0000-0014-100000000000";

    Resources<Resource<BlogEntity>> result = (Resources<Resource<BlogEntity>>) controller
        .readBlogs(topicId, null).getBody();

    assertThat(result.getContent()).isNotEmpty();
  }

  @Test(expected = NotFoundException.class)
  public void readPagesNotFound() {
    String topicId = "00000000-0000-0000-0014-XX0000000000";

    controller.readBlogs(topicId, null);
  }
}
