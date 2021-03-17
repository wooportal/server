package de.codeschluss.wooportal.server.integration.label;

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
import de.codeschluss.wooportal.server.components.label.LabelController;
import de.codeschluss.wooportal.server.components.label.LabelEntity;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.DuplicateEntryException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LabelControllerUpdateTest {

  @Autowired
  private LabelController controller;

  @Test
  @WithUserDetails("super@user")
  public void updateSuperUserOk() throws URISyntaxException {
    LabelEntity label = newLabel("updateSuperUserOk", "updateSuperUserOk");
    String labelId = "00000000-0000-0000-0028-200000000000";

    controller.update(label, labelId);

    Resource<LabelEntity> result = (Resource<LabelEntity>) controller.readOne(labelId);
    assertThat(result.getContent().getContent()).isEqualTo(label.getContent());
  }
  
  @Test
  @WithUserDetails("translator@user")
  public void updateTranslatorOk() throws URISyntaxException {
    LabelEntity label = newLabel("updateTranslatorOk", "updateTranslatorOk");
    String labelId = "00000000-0000-0000-0028-200000000000";

    controller.update(label, labelId);

    Resource<LabelEntity> result = (Resource<LabelEntity>) controller.readOne(labelId);
    assertThat(result.getContent().getContent()).isEqualTo(label.getContent());
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void updateNotvalidOk() throws URISyntaxException {
    LabelEntity label = newLabel("updateNotvalidOk", null);
    String labelId = "00000000-0000-0000-0028-200000000000";

    controller.update(label, labelId);
  }

  @Test(expected = DuplicateEntryException.class)
  @WithUserDetails("super@user")
  public void updateSuperUserDuplicatedName() throws URISyntaxException {
    LabelEntity label = newLabel("label1", "label1");
    String labelId = "00000000-0000-0000-0028-200000000000";

    controller.update(label, labelId);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void updateProviderUserDenied() throws URISyntaxException {
    LabelEntity label = newLabel("updateProviderUserDenied", "updateProviderUserDenied");
    String labelId = "00000000-0000-0000-0028-200000000000";

    controller.update(label, labelId);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void updateNoUserDenied() throws URISyntaxException {
    LabelEntity label = newLabel("updateNoUserDenied", "updateNoUserDenied");
    String labelId = "00000000-0000-0000-0028-200000000000";

    controller.update(label, labelId);
  }
  
  private LabelEntity newLabel(String tagId, String content) {
    LabelEntity label = new LabelEntity();
    label.setTagId(tagId);
    label.setContent(content);
    return label;
  }

}
