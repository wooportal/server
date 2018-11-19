package de.codeschluss.portal.integration.activity;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.portal.components.activity.ActivityController;
import de.codeschluss.portal.components.address.AddressEntity;
import de.codeschluss.portal.core.exception.NotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityControllerFindAddressTest {

  @Autowired
  private ActivityController controller;

  @SuppressWarnings("unchecked")
  @Test
  public void findAddressOk() {
    String activityId = "00000000-0000-0000-0010-100000000000";

    Resource<AddressEntity> result = (Resource<AddressEntity>) controller.findAddress(activityId)
        .getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findAddressNotFound() {
    String activityId = "00000000-0000-0000-0010-XX0000000000";

    controller.findOne(activityId);
  }
}
