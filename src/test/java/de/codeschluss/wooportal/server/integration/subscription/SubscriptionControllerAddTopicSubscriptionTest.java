package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
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
public class SubscriptionControllerAddTopicSubscriptionTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  public void addTopicSubscriptionOk() throws URISyntaxException {
    StringPrimitive topicId = new StringPrimitive("00000000-0000-0000-0014-100000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addTopicSubscription(subscriptionId, topicId);

    assertContaining(topicId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addTopicNotFoundSubscription() throws URISyntaxException {
    StringPrimitive topicId = new StringPrimitive("00000000-0000-0000-0014-100000000000");
    String subscriptionId = "00000000-0000-0000-00xx-100000000000";

    controller.addTopicSubscription(subscriptionId, topicId);

    assertContaining(topicId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addTopicNotFoundTopic() throws URISyntaxException {
    StringPrimitive topicId = new StringPrimitive("00000000-0000-0000-0014-xx0000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addTopicSubscription(subscriptionId, topicId);

    assertContaining(topicId, subscriptionId);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(StringPrimitive topicId, String subscriptionId) {
    Resources<Resource<TopicEntity>> result = 
        (Resources<Resource<TopicEntity>>) controller
        .readTopicSubscriptions(subscriptionId).getBody();
    
    assertThat(result.getContent()).haveAtLeastOne(new Condition<>(
        t -> topicId.getValue().equals(t.getContent().getId()), "topic exists"));
  }
}
