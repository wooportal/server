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
public class SubscriptionControllerDeleteActivitySubscriptionsTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  @WithUserDetails("super@user")
  public void deleteActivitySubscriptions() throws URISyntaxException {
    List<String> activityId = new ArrayList<>();
    activityId.add("00000000-0000-0000-0010-200000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    assertContaining(subscriptionId, activityId);

    controller.deleteActivitySubscriptions(subscriptionId, activityId);

    assertNotContaining(subscriptionId, activityId);
  }
  
  @Test(expected = BadParamsException.class)
  public void deleteActivitySubscriptionsNotFound() throws URISyntaxException {
    List<String> activityId = new ArrayList<>();
    activityId.add("00000000-0000-0000-0010-100000000000");
    String subscriptionId = "00000000-0000-0000-0020-xx0000000000";

    controller.deleteActivitySubscriptions(subscriptionId, activityId);
  }

  private void assertContaining(String subscriptionId, List<String> activityIds) {
    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>)
        controller.readOne(subscriptionId);
    assertThat(result.getContent().getActivitySubscriptions()).haveAtLeastOne(
        new Condition<>(t -> activityIds.contains(t.getId()), "activity exists"));
  }

  private void assertNotContaining(String subscriptionId, List<String> activityIds) {
    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>)
        controller.readOne(subscriptionId);
    assertThat(result.getContent().getActivitySubscriptions())
        .noneMatch(t -> activityIds.contains(t.getId()));
  }
}
