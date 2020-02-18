package de.codeschluss.wooportal.server.integration.subscriptiontype;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeController;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.translations.SubscriptionTypeTranslatablesEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionTypeControllerTranslationsTest {

  @Autowired
  private SubscriptionTypeController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findTranslationsOk() {
    String subscriptionTypeId = "00000000-0000-0000-0021-100000000000";

    Resources<Resource<SubscriptionTypeTranslatablesEntity>> result = 
        (Resources<Resource<SubscriptionTypeTranslatablesEntity>>) controller
        .readTranslations(subscriptionTypeId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTranslationsNotFound() {
    String subscriptionTypeId = "00000000-0000-0000-0021-xx0000000000";

    controller.readTranslations(subscriptionTypeId);
  }
}
