package de.codeschluss.wooportal.server.integration.blog;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.blog.BlogController;
import de.codeschluss.wooportal.server.components.blog.BlogService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogControllerIncreaseLike {
  
  @Autowired
  private BlogController controller;
  
  @Autowired
  private SubscriptionService subscriptionService;
  
  @Autowired
  private BlogService blogService;

  @Test
  public void findOneOk() {
    String blogId = "00000000-0000-0000-0016-100000000000";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";
    int likesCount = controller.readOne(blogId).getContent().getLikes();
    
    controller.increaseLike(blogId, new StringPrimitive(subscriptionId));
    
    assertThat(controller.readOne(blogId).getContent().getLikes()).isEqualTo(likesCount + 1);
    assertThat(subscriptionService.getById(subscriptionId).getBlogLikes())
        .haveAtLeastOne(new Condition<>(
            p -> p.getTitle().equals(blogService.getById(blogId).getTitle()), "blog exists"));
  }

  @Test(expected = BadParamsException.class)
  public void findOneNotFound() {
    String blogId = "00000000-0000-0000-0016-XX0000000000";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.increaseLike(blogId, new StringPrimitive(subscriptionId));
  }

}
