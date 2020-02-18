package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionControllerReadOneTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  @WithUserDetails("super@user")
  public void findOneOk() {
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>) controller
        .readOne(subscriptionId);

    assertThat(result.getContent()).isNotNull();
  }
  
  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void findOneDenied() {
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.readOne(subscriptionId);
  }

  @Test(expected = NotFoundException.class)
  @WithUserDetails("super@user")
  public void findSubscriptionNotFound() {
    String subscriptionId = "00000000-0000-0000-0020-xx0000000000";

    controller.readOne(subscriptionId);
  }
}
