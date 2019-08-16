package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganisationControllerReadAddressTest {

  @Autowired
  private OrganisationController controller;

  @Test
  public void findAddressByOrganisationOk() {
    String orgaId = "00000000-0000-0000-0008-200000000000";

    Resource<?> result = (Resource<?>) controller.readAddress(orgaId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findAddressByOrganisationNotFound() {
    String orgaId = "00000000-0000-0000-0008-XX0000000000";

    Resource<?> result = (Resource<?>) controller.readAddress(orgaId).getBody();

    assertThat(result.getContent()).isNotNull();
  }
}