package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class SubscriptionControllerDeleteTopicSubscriptionsTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  @WithUserDetails("super@user")
  public void deleteTopicSubscriptions() throws URISyntaxException {
    List<String> topicId = new ArrayList<>();
    topicId.add("00000000-0000-0000-0014-200000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    assertContaining(subscriptionId, topicId);

    controller.deleteTopicSubscriptions(subscriptionId, topicId);

    assertNotContaining(subscriptionId, topicId);
  }
  
  @Test(expected = BadParamsException.class)
  public void deleteTopicSubscriptionsNotFound() throws URISyntaxException {
    List<String> topicId = new ArrayList<>();
    topicId.add("00000000-0000-0000-0014-100000000000");
    String subscriptionId = "00000000-0000-0000-0020-xx0000000000";

    controller.deleteTopicSubscriptions(subscriptionId, topicId);
  }

  private void assertContaining(String subscriptionId, List<String> topicIds) {
    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>)
        controller.readOne(subscriptionId);
    assertThat(result.getContent().getTopicSubscriptions()).haveAtLeastOne(
        new Condition<>(t -> topicIds.contains(t.getId()), "topic exists"));
  }

  private void assertNotContaining(String subscriptionId, List<String> topicIds) {
    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>)
        controller.readOne(subscriptionId);
    assertThat(result.getContent().getTopicSubscriptions())
        .noneMatch(t -> topicIds.contains(t.getId()));
  }
}
