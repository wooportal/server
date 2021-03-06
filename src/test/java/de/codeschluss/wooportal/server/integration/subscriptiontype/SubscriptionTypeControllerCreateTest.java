package de.codeschluss.wooportal.server.integration.subscriptiontype;

import static org.junit.Assert.assertEquals;

import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeController;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeEntity;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionTypeControllerCreateTest {

  @Autowired
  private SubscriptionTypeController controller;

  
  @Test
  public void createSuperUserOk() throws Exception {
    SubscriptionTypeEntity subscriptionType = new SubscriptionTypeEntity();

    assertEquals(
        controller.create(subscriptionType).getStatusCodeValue(), 
        HttpStatus.SC_METHOD_NOT_ALLOWED);
    
  }

}
