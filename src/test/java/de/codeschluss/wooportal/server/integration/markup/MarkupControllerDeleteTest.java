package de.codeschluss.wooportal.server.integration.markup;

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
import de.codeschluss.wooportal.server.components.markup.MarkupController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkupControllerDeleteTest {

  @Autowired
  private MarkupController controller;

  @Test(expected = NotFoundException.class)
  @WithUserDetails("super@user")
  public void deleteSuperUserOk() throws URISyntaxException {
    String markupId = "00000000-0000-0000-0029-300000000000";
    assertThat(controller.readOne(markupId)).isNotNull();

    controller.delete(markupId);

    controller.readOne(markupId);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("translator@user")
  public void deleteTranslatorDenied() throws URISyntaxException {
    String markupId = "00000000-0000-0000-0029-100000000000";

    controller.delete(markupId);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void deleteOtherNotRegisteredDenied() {
    String markupId = "00000000-0000-0000-0029-100000000000";

    controller.delete(markupId);
  }

}
