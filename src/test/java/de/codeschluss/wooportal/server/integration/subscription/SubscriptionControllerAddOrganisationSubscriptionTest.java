package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
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
public class SubscriptionControllerAddOrganisationSubscriptionTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  public void addOrganisationSubscriptionOk() throws URISyntaxException {
    StringPrimitive organisationId = new StringPrimitive("00000000-0000-0000-0008-100000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addOrganisationSubscription(subscriptionId, organisationId);

    assertContaining(organisationId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addOrganisationNotFoundSubscription() throws URISyntaxException {
    StringPrimitive organisationId = new StringPrimitive("00000000-0000-0000-0008-100000000000");
    String subscriptionId = "00000000-0000-0000-00xx-100000000000";

    controller.addOrganisationSubscription(subscriptionId, organisationId);

    assertContaining(organisationId, subscriptionId);
  }
  
  @Test(expected = BadParamsException.class)
  public void addOrganisationNotFoundOrganisation() throws URISyntaxException {
    StringPrimitive organisationId = new StringPrimitive("00000000-0000-0000-0008-xx0000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.addOrganisationSubscription(subscriptionId, organisationId);

    assertContaining(organisationId, subscriptionId);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(StringPrimitive organisationId, String subscriptionId) {
    Resources<Resource<OrganisationEntity>> result = 
        (Resources<Resource<OrganisationEntity>>) controller
        .readOrganisationSubscriptions(subscriptionId).getBody();
    
    assertThat(result.getContent()).haveAtLeastOne(new Condition<>(
        t -> organisationId.getValue().equals(t.getContent().getId()), "organisation exists"));
  }
}
