package de.codeschluss.wooportal.server.integration.label;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.label.LabelController;
import de.codeschluss.wooportal.server.components.label.LabelEntity;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.DuplicateEntryException;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LabelControllerCreateTest {

  @Autowired
  private LabelController controller;

  @Test
  @WithUserDetails("super@user")
  public void createSuperUserOk() throws Exception {
    LabelEntity label = newLabel("createSuperUserOk", "createSuperUserOk");

    controller.create(label);

    assertContaining(label);
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void createNotvalidOk() throws Exception {
    LabelEntity label = newLabel("createNotvalidOk", null);

    controller.create(label);
  }

  @Test(expected = DuplicateEntryException.class)
  @WithUserDetails("super@user")
  public void createSuperUserDuplicated() throws Exception {
    LabelEntity label = newLabel("label1", "label1");

    controller.create(label);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("translator@user")
  public void createTranslatorDenied() throws Exception {
    LabelEntity label = newLabel("createTranslatorDenied", "createTranslatorDenied");

    controller.create(label);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void createNoUserDenied() throws Exception {
    LabelEntity label = newLabel("createNoUserDenied", "createNoUserDenied");

    controller.create(label);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(LabelEntity label) {
    Resources<Resource<LabelEntity>> result = (Resources<Resource<LabelEntity>>) controller
        .readAll(new FilterSortPaginate()).getBody();
    assertThat(result.getContent()).haveAtLeastOne(
        new Condition<>(p -> p.getContent().getTagId().equals(label.getTagId()), "label exists"));
  }
  
  private LabelEntity newLabel(String tagId, String content) {
    LabelEntity label = new LabelEntity();
    label.setTagId(tagId);
    label.setContent(content);
    return label;
  }
}
