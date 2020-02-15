package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.components.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionControllerCreateTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  @SuppressWarnings("unchecked")
  @WithUserDetails("super@user")
  public void createOk() throws Exception {
    SubscriptionEntity subscription = newSubscription("authSecret");

    controller.create(subscription);

    Resources<Resource<SubscriptionEntity>> result = (Resources<Resource<SubscriptionEntity>>) 
        controller.readAll(new FilterSortPaginate()).getBody();
    assertThat(result.getContent()).haveAtLeastOne(new Condition<>(p -> 
        p.getContent().getAuthSecret().equals(subscription.getAuthSecret()), 
        "subscription exists"));
  }

  @Test(expected = BadParamsException.class)
  public void createNotValidEmpty() throws Exception {
    SubscriptionEntity subscription = newSubscription("");

    controller.create(subscription);
  }

  @Test(expected = BadParamsException.class)
  public void createNotValidNull() throws Exception {
    SubscriptionEntity subscription = newSubscription(null);

    controller.create(subscription);
  }

  private SubscriptionEntity newSubscription(String authSecret) {
    SubscriptionEntity subscription = new SubscriptionEntity();
    subscription.setAuthSecret(authSecret);
    return subscription;
  }
}
