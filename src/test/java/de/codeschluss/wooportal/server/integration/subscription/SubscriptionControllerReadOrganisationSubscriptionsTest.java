package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionControllerReadOrganisationSubscriptionsTest {
  
  @Autowired
  private SubscriptionController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findOrganisationSubscriptionsOk() 
      throws JsonParseException, JsonMappingException, IOException {
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    Resources<Resource<OrganisationEntity>> result = 
        (Resources<Resource<OrganisationEntity>>) controller
        .readSubscribedTypes(subscriptionId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findOrganisationSubscriptionsNotFound() 
      throws JsonParseException, JsonMappingException, IOException {
    String subscriptionId = "00000000-0000-0000-0020-XX0000000000";

    controller.readSubscribedTypes(subscriptionId);
  }

}
