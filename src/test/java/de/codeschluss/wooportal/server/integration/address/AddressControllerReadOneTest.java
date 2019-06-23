package de.codeschluss.wooportal.server.integration.address;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.address.AddressController;
import de.codeschluss.wooportal.server.components.address.AddressEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressControllerReadOneTest {

  @Autowired
  private AddressController controller;

  @Test
  public void findOneOk() {
    String addressId = "00000000-0000-0000-0006-100000000000";

    Resource<AddressEntity> result = (Resource<AddressEntity>) controller.readOne(addressId);

    assertThat(result.getContent().getId()).isEqualTo(addressId);
  }

  @Test(expected = NotFoundException.class)
  public void findOneNotFound() {
    String addressId = "00000000-0000-0000-0006-XX0000000000";

    controller.readOne(addressId);
  }

}
