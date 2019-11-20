package de.codeschluss.wooportal.server.integration.activity;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.activity.ActivityController;
import de.codeschluss.wooportal.server.components.address.AddressEntity;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityControllerUpdateAddressTest {

  @Autowired
  private ActivityController controller;

  @Test
  @WithUserDetails("super@user")
  public void updateAddressSuperUserOk() throws URISyntaxException {
    StringPrimitive addressId = new StringPrimitive("00000000-0000-0000-0006-400000000000");
    String activityId = "00000000-0000-0000-0010-100000000000";

    controller.updateAddress(activityId, addressId);

    assertContaining(activityId, addressId);
  }

  @Test
  @WithUserDetails("provider1@user")
  public void updateProviderOk() throws URISyntaxException {
    StringPrimitive addressId = new StringPrimitive("00000000-0000-0000-0006-200000000000");
    String activityId = "00000000-0000-0000-0010-200000000000";

    controller.updateAddress(activityId, addressId);

    assertContaining(activityId, addressId);
  }

  @Test
  @WithUserDetails("admin@user")
  public void updateAdminOk() throws URISyntaxException {
    StringPrimitive addressId = new StringPrimitive("00000000-0000-0000-0006-300000000000");
    String activityId = "00000000-0000-0000-0010-200000000000";

    controller.updateAddress(activityId, addressId);

    assertContaining(activityId, addressId);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void updateSuperActivityBadParam() throws URISyntaxException {
    StringPrimitive addressId = new StringPrimitive("00000000-0000-0000-0006-300000000000");
    String activityId = "00000000-0000-0000-0010-XX0000000000";

    controller.updateAddress(activityId, addressId);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("provider1@user")
  public void updateProviderAddressBadParam() throws URISyntaxException {
    StringPrimitive addressId = new StringPrimitive("00000000-0000-0000-0006-XX0000000000");
    String activityId = "00000000-0000-0000-0010-200000000000";

    controller.updateAddress(activityId, addressId);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void updateOtherProviderDenied() throws URISyntaxException {
    StringPrimitive addressId = new StringPrimitive("00000000-0000-0000-0006-300000000000");
    String activityId = "00000000-0000-0000-0010-300000000000";

    controller.updateAddress(activityId, addressId);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void updateNoUserDenied() throws URISyntaxException {
    StringPrimitive addressId = new StringPrimitive("00000000-0000-0000-0006-300000000000");
    String activityId = "00000000-0000-0000-0010-200000000000";

    controller.updateAddress(activityId, addressId);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(String activityId, StringPrimitive addressId) {
    Resource<AddressEntity> result = (Resource<AddressEntity>) controller.readAddress(activityId)
        .getBody();
    assertThat(result.getContent().getId()).isEqualTo(addressId.getValue());
  }
}
