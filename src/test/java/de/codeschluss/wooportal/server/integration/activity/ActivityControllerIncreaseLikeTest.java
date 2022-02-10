package de.codeschluss.wooportal.server.integration.activity;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.activity.ActivityController;
import de.codeschluss.wooportal.server.components.activity.ActivityService;
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
public class ActivityControllerIncreaseLikeTest {
  
  @Autowired
  private ActivityController controller;
  
  @Autowired
  private SubscriptionService subscriptionService;
  
  @Autowired
  private ActivityService activityService;

  @Test
  public void increaseLikeOk() {
    String activityId = "00000000-0000-0000-0010-100000000000";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";
    int likesCount = controller.readOne(activityId).getContent().getLikes();
    
    controller.increaseLike(activityId, new StringPrimitive(subscriptionId));
    
    assertThat(controller.readOne(activityId).getContent().getLikes()).isEqualTo(likesCount + 1);
    assertThat(subscriptionService.getById(subscriptionId).getActivityLikes())
        .haveAtLeastOne(new Condition<>(
            p -> p.getName().equals(activityService.getById(activityId).getName()),
            "activity exists"));
  }

  @Test(expected = BadParamsException.class)
  public void increaseLikeNotFound() {
    String activityId = "00000000-0000-0000-0010-XX0000000000";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.increaseLike(activityId, new StringPrimitive(subscriptionId));
  }

}
