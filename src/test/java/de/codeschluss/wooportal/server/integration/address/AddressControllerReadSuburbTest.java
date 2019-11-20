package de.codeschluss.wooportal.server.integration.address;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.address.AddressController;
import de.codeschluss.wooportal.server.components.suburb.SuburbEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressControllerReadSuburbTest {

  @Autowired
  private AddressController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findSuburbOk() {
    String addressId = "00000000-0000-0000-0006-100000000000";

    Resource<SuburbEntity> result = (Resource<SuburbEntity>) controller.readSuburb(addressId)
        .getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findSuburbNotFound() {
    String addressId = "00000000-0000-0000-0006-XX0000000000";

    controller.readSuburb(addressId);
  }
}
