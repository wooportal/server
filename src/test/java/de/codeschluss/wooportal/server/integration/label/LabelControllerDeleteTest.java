package de.codeschluss.wooportal.server.integration.label;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.label.LabelController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LabelControllerDeleteTest {

  @Autowired
  private LabelController controller;

  @Test(expected = NotFoundException.class)
  @WithUserDetails("super@user")
  public void deleteSuperUserOk() throws URISyntaxException {
    String labelId = "00000000-0000-0000-0028-300000000000";
    assertThat(controller.readOne(labelId)).isNotNull();

    controller.delete(labelId);

    controller.readOne(labelId);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("translator@user")
  public void deleteTranslatorDenied() throws URISyntaxException {
    String labelId = "00000000-0000-0000-0028-100000000000";

    controller.delete(labelId);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void deleteOtherNotRegisteredDenied() {
    String labelId = "00000000-0000-0000-0028-100000000000";

    controller.delete(labelId);
  }

}
