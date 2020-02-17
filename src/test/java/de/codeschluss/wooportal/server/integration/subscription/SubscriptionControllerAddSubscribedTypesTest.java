package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeEntity;
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
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SubscriptionControllerAddSubscribedTypesTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  public void addSubscriptionTypeOk() throws URISyntaxException {
    List<String> subscriptionTypeId = new ArrayList<>();
    subscriptionTypeId.add("00000000-0000-0000-0021-200000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addSubscriptionType(subscriptionId, subscriptionTypeId);

    assertContaining(subscriptionTypeId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addSubscriptionTypeNotFoundSubscription() throws URISyntaxException {
    List<String> subscriptionTypeId = new ArrayList<>();
    subscriptionTypeId.add("00000000-0000-0000-0023-300000000000");
    String subscriptionId = "00000000-0000-0000-00xx-100000000000";

    controller.addSubscriptionType(subscriptionId, subscriptionTypeId);

    assertContaining(subscriptionTypeId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addSubscriptionTypeNotFoundSubscriptionType() throws URISyntaxException {
    List<String> subscriptionTypeId = new ArrayList<>();
    subscriptionTypeId.add("00000000-0000-0000-0023-xx0000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addSubscriptionType(subscriptionId, subscriptionTypeId);

    assertContaining(subscriptionTypeId, subscriptionId);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(List<String> subscriptionTypeIds, String subscriptionId) {
    Resources<Resource<SubscriptionTypeEntity>> result = 
        (Resources<Resource<SubscriptionTypeEntity>>) controller
        .readSubscribedTypes(subscriptionId).getBody();
    
    assertThat(result.getContent()).haveAtLeastOne(new Condition<>(
        t -> subscriptionTypeIds.contains(t.getContent().getId()), "subscriptionType exists"));
  }
}
