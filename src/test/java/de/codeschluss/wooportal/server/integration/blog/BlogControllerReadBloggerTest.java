package de.codeschluss.wooportal.server.integration.blog;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.blog.BlogController;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogControllerReadBloggerTest {

  @Autowired
  private BlogController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findBloggerOk() {
    String blogId = "00000000-0000-0000-0016-100000000000";

    Resource<BloggerEntity> result = (Resource<BloggerEntity>) 
        controller.readBlogger(blogId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findBloggerNotFound() {
    String blogId = "00000000-0000-0000-0016-XX0000000000";

    controller.readOne(blogId);
  }
}
