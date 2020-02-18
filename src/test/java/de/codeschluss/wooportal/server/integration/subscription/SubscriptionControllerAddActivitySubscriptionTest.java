package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
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
public class SubscriptionControllerAddActivitySubscriptionTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  public void addActivitySubscriptionOk() throws URISyntaxException {
    StringPrimitive activityId = new StringPrimitive("00000000-0000-0000-0010-100000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addActivitySubscription(subscriptionId, activityId);

    assertContaining(activityId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addActivityNotFoundSubscription() throws URISyntaxException {
    StringPrimitive activityId = new StringPrimitive("00000000-0000-0000-0010-100000000000");
    String subscriptionId = "00000000-0000-0000-00xx-100000000000";

    controller.addActivitySubscription(subscriptionId, activityId);

    assertContaining(activityId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addActivityNotFoundActivity() throws URISyntaxException {
    StringPrimitive activityId = new StringPrimitive("00000000-0000-0000-0010-xx0000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addActivitySubscription(subscriptionId, activityId);

    assertContaining(activityId, subscriptionId);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(StringPrimitive activityId, String subscriptionId) {
    Resources<Resource<ActivityEntity>> result = 
        (Resources<Resource<ActivityEntity>>) controller
        .readActivitySubscriptions(subscriptionId).getBody();
    
    assertThat(result.getContent()).haveAtLeastOne(new Condition<>(
        t -> activityId.getValue().equals(t.getContent().getId()), "activity exists"));
  }
}
