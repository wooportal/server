package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.DuplicateEntryException;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
public class SubscriptionControllerUpdateTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  @WithUserDetails("super@user")
  public void updateOk() throws URISyntaxException {
    SubscriptionEntity subscription = newSubscription("updateOk");
    String subscriptionId = "00000000-0000-0000-0020-200000000000";

    controller.update(subscription, subscriptionId);

    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>) controller
        .readOne(subscriptionId);
    assertThat(result.getContent().getAuthSecret()).isEqualTo(subscription.getAuthSecret());
  }
  
  @Test(expected = BadParamsException.class)
  public void updateNotValidEmpty() throws URISyntaxException {
    SubscriptionEntity subscription = newSubscription("");
    String subscriptionId = "00000000-0000-0000-0020-200000000000";

    controller.update(subscription, subscriptionId);
  }

  @Test(expected = BadParamsException.class)
  public void updateNotValidNull() throws URISyntaxException {
    SubscriptionEntity subscription = newSubscription(null);
    String subscriptionId = "00000000-0000-0000-0020-200000000000";

    controller.update(subscription, subscriptionId);
  }

  @Test(expected = DuplicateEntryException.class)
  public void updateDuplicated() throws URISyntaxException {
    SubscriptionEntity subscription = newSubscription("authSecret1");
    String subscriptionId = "00000000-0000-0000-0020-200000000000";

    controller.update(subscription, subscriptionId);
  }

  private SubscriptionEntity newSubscription(String authSecret) {
    SubscriptionEntity subscription = new SubscriptionEntity();
    subscription.setAuthSecret(authSecret);
    return subscription;
  }

}
