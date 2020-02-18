package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import java.net.URISyntaxException;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SubscriptionControllerAddBloggerSubscriptionTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  public void addBloggerSubscriptionOk() throws URISyntaxException {
    StringPrimitive bloggerId = new StringPrimitive("00000000-0000-0000-0015-100000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addBloggerSubscription(subscriptionId, bloggerId);

    assertContaining(bloggerId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addBloggerNotFoundSubscription() throws URISyntaxException {
    StringPrimitive bloggerId = new StringPrimitive("00000000-0000-0000-0015-100000000000");
    String subscriptionId = "00000000-0000-0000-00xx-100000000000";

    controller.addBloggerSubscription(subscriptionId, bloggerId);

    assertContaining(bloggerId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addBloggerNotFoundBlogger() throws URISyntaxException {
    StringPrimitive bloggerId = new StringPrimitive("00000000-0000-0000-0015-xx0000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addBloggerSubscription(subscriptionId, bloggerId);

    assertContaining(bloggerId, subscriptionId);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(StringPrimitive bloggerId, String subscriptionId) {
    Resources<Resource<BloggerEntity>> result = 
        (Resources<Resource<BloggerEntity>>) controller
        .readBloggerSubscriptions(subscriptionId).getBody();
    
    assertThat(result.getContent()).haveAtLeastOne(new Condition<>(
        t -> bloggerId.getValue().equals(t.getContent().getId()), "blogger exists"));
  }
}
