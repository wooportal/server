package de.codeschluss.wooportal.server.integration.subscriptiontype;

import static org.junit.Assert.assertEquals;

import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeController;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionTypeControllerDeleteTest {

  @Autowired
  private SubscriptionTypeController controller;

  
  @Test
  public void createSuperUserOk() throws Exception {
    String subscriptionTypeId = "00000000-0000-0000-0021-100000000000s";

    assertEquals(
        controller.delete(subscriptionTypeId).getStatusCodeValue(), 
        HttpStatus.SC_METHOD_NOT_ALLOWED);
    
  }

}
