package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
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
public class OrganisationControllerIncreaseLike {
  
  @Autowired
  private OrganisationController controller;
  
  @Autowired
  private SubscriptionService subscriptionService;
  
  @Autowired
  private OrganisationService orgaService;

  @Test
  public void increaseLikeOk() {
    String organisationId = "00000001-0000-0000-0008-000000000000";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";
    int likesCount = controller.readOne(organisationId).getContent().getLikes();
    
    controller.increaseLike(organisationId, new StringPrimitive(subscriptionId));
    
    assertThat(
        controller.readOne(organisationId).getContent().getLikes()).isEqualTo(likesCount + 1);
    assertThat(subscriptionService.getById(subscriptionId).getOrganisationLikes())
        .haveAtLeastOne(new Condition<>(
            p -> p.getName().equals(orgaService.getById(organisationId).getName()), "orga exists"));
  }

  @Test(expected = BadParamsException.class)
  public void increaseLikeNotFound() {
    String organisationId = "00000000-0000-0000-0008-XX0000000000";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.increaseLike(organisationId, new StringPrimitive(subscriptionId));
  }

}
