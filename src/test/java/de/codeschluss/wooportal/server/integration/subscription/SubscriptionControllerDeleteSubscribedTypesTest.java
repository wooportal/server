package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.components.subscription.SubscriptionEntity;
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
public class SubscriptionControllerDeleteSubscribedTypesTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  @WithUserDetails("super@user")
  public void deleteSubscriptionTypes() throws URISyntaxException {
    List<String> subscribtionTypeId = new ArrayList<>();
    subscribtionTypeId.add("00000000-0000-0000-0021-200000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    assertContaining(subscriptionId, subscribtionTypeId);

    controller.deleteSubscriptionTypes(subscriptionId, subscribtionTypeId);

    assertNotContaining(subscriptionId, subscribtionTypeId);
  }
  
  @Test(expected = BadParamsException.class)
  public void deleteSubscriptionTypesNotFound() throws URISyntaxException {
    List<String> subscribtionTypeId = new ArrayList<>();
    subscribtionTypeId.add("00000000-0000-0000-0021-100000000000");
    String subscriptionId = "00000000-0000-0000-0020-xx0000000000";

    controller.deleteSubscriptionTypes(subscriptionId, subscribtionTypeId);
  }

  private void assertContaining(String subscriptionId, List<String> subscribtionTypeIds) {
    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>)
        controller.readOne(subscriptionId);
    assertThat(result.getContent().getSubscribedTypes()).haveAtLeastOne(
        new Condition<>(t -> subscribtionTypeIds.contains(t.getId()), "subscribtionType exists"));
  }

  private void assertNotContaining(String subscriptionId, List<String> subscribtionTypeIds) {
    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>)
        controller.readOne(subscriptionId);
    assertThat(result.getContent().getSubscribedTypes())
        .noneMatch(t -> subscribtionTypeIds.contains(t.getId()));
  }
}
