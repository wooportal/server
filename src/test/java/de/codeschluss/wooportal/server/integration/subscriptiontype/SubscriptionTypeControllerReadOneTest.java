package de.codeschluss.wooportal.server.integration.subscriptiontype;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.push.subscriptiontype.SubscriptionTypeController;
import de.codeschluss.wooportal.server.core.push.subscriptiontype.SubscriptionTypeEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionTypeControllerReadOneTest {

  @Autowired
  private SubscriptionTypeController controller;

  @Test
  public void findOneOk() {
    String subscriptionTypeId = "00000000-0000-0000-0021-100000000000";

    Resource<SubscriptionTypeEntity> result = 
        (Resource<SubscriptionTypeEntity>) controller.readOne(subscriptionTypeId);

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findSubscriptionTypeNotFound() {
    String subscriptionTypeId = "00000000-0000-0000-0021-xx0000000000";

    controller.readOne(subscriptionTypeId);
  }
}
