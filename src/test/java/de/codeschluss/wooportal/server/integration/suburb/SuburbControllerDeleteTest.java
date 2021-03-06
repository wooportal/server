package de.codeschluss.wooportal.server.integration.suburb;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.suburb.SuburbController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
public class SuburbControllerDeleteTest {

  @Autowired
  private SuburbController controller;

  @Test(expected = NotFoundException.class)
  @WithUserDetails("super@user")
  public void deleteSuperUserOk() throws URISyntaxException {
    String suburbId = "00000000-0000-0000-0005-30000000000";
    assertThat(controller.readOne(suburbId)).isNotNull();

    controller.delete(suburbId);

    controller.readOne(suburbId);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void deleteProviderUserDenied() throws URISyntaxException {
    String suburbId = "00000000-0000-0000-0005-10000000000";

    controller.delete(suburbId);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void deleteOtherNotRegisteredDenied() {
    String suburbId = "00000000-0000-0000-0005-10000000000";

    controller.delete(suburbId);
  }

}
