package de.codeschluss.wooportal.server.integration.blog;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.blog.BlogController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogControllerReadActivityTest {

  @Autowired
  private BlogController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findActivityOk() {
    String blogId = "00000000-0000-0000-0016-100000000000";

    Resource<ActivityEntity> result = (Resource<ActivityEntity>) 
        controller.readActivity(blogId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findActivityNotFound() {
    String blogId = "00000000-0000-0000-0016-XX0000000000";

    controller.readOne(blogId);
  }
}
