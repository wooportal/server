package de.codeschluss.wooportal.server.integration.markup;

import static org.assertj.core.api.Assertions.assertThat;
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
import de.codeschluss.wooportal.server.components.markup.MarkupController;
import de.codeschluss.wooportal.server.components.markup.MarkupEntity;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.DuplicateEntryException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkupControllerUpdateTest {

  @Autowired
  private MarkupController controller;

  @Test
  @WithUserDetails("super@user")
  public void updateSuperUserOk() throws URISyntaxException {
    MarkupEntity markup = newMarkup("updateSuperUserOk", "updateSuperUserOk");
    String markupId = "00000000-0000-0000-0029-200000000000";

    controller.update(markup, markupId);

    Resource<MarkupEntity> result = (Resource<MarkupEntity>) controller.readOne(markupId);
    assertThat(result.getContent().getContent()).isEqualTo(markup.getContent());
  }
  
  @Test
  @WithUserDetails("translator@user")
  public void updateTranslatorOk() throws URISyntaxException {
    MarkupEntity markup = newMarkup("updateTranslatorOk", "updateTranslatorOk");
    String markupId = "00000000-0000-0000-0029-200000000000";

    controller.update(markup, markupId);

    Resource<MarkupEntity> result = (Resource<MarkupEntity>) controller.readOne(markupId);
    assertThat(result.getContent().getContent()).isEqualTo(markup.getContent());
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void updateNotvalidOk() throws URISyntaxException {
    MarkupEntity markup = newMarkup("updateNotvalidOk", null);
    String markupId = "00000000-0000-0000-0029-200000000000";

    controller.update(markup, markupId);
  }

  @Test(expected = DuplicateEntryException.class)
  @WithUserDetails("super@user")
  public void updateSuperUserDuplicatedName() throws URISyntaxException {
    MarkupEntity markup = newMarkup("markup1", "markup1");
    String markupId = "00000000-0000-0000-0029-200000000000";

    controller.update(markup, markupId);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void updateProviderUserDenied() throws URISyntaxException {
    MarkupEntity markup = newMarkup("updateProviderUserDenied", "updateProviderUserDenied");
    String markupId = "00000000-0000-0000-0029-200000000000";

    controller.update(markup, markupId);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void updateNoUserDenied() throws URISyntaxException {
    MarkupEntity markup = newMarkup("updateNoUserDenied", "updateNoUserDenied");
    String markupId = "00000000-0000-0000-0029-200000000000";

    controller.update(markup, markupId);
  }
  
  private MarkupEntity newMarkup(String tagId, String content) {
    MarkupEntity markup = new MarkupEntity();
    markup.setTagId(tagId);
    markup.setContent(content);
    return markup;
  }

}
