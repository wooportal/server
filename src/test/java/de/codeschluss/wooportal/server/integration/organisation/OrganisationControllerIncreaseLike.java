package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
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

  @Test
  public void increaseLikeOk() {
    String organisationId = "00000001-0000-0000-0008-000000000000";
    int likesCount = controller.readOne(organisationId).getContent().getLikes();
    
    controller.increaseLike(organisationId, null);
    
    assertThat(
        controller.readOne(organisationId).getContent().getLikes()).isEqualTo(likesCount + 1); 
  }

  @Test(expected = BadParamsException.class)
  public void increaseLikeNotFound() {
    String organisationId = "00000000-0000-0000-0008-XX0000000000";

    controller.increaseLike(organisationId, null);
  }

}
