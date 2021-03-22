package de.codeschluss.wooportal.server.integration.markup;

import static org.assertj.core.api.Assertions.assertThat;
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
import de.codeschluss.wooportal.server.components.markup.MarkupController;
import de.codeschluss.wooportal.server.components.markup.MarkupEntity;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.DuplicateEntryException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkupControllerCreateTest {

  @Autowired
  private MarkupController controller;

  @Test
  @WithUserDetails("super@user")
  public void createSuperUserOk() throws Exception {
    MarkupEntity markup = newMarkup("createSuperUserOk", "createSuperUserOk");

    controller.create(markup);

    assertContaining(markup);
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void createNotvalidOk() throws Exception {
    MarkupEntity markup = newMarkup("createNotvalidOk", null);

    controller.create(markup);
  }

  @Test(expected = DuplicateEntryException.class)
  @WithUserDetails("super@user")
  public void createSuperUserDuplicated() throws Exception {
    MarkupEntity markup = newMarkup("markup1", "markup1");

    controller.create(markup);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("translator@user")
  public void createTranslatorDenied() throws Exception {
    MarkupEntity markup = newMarkup("createTranslatorDenied", "createTranslatorDenied");

    controller.create(markup);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void createNoUserDenied() throws Exception {
    MarkupEntity markup = newMarkup("createNoUserDenied", "createNoUserDenied");

    controller.create(markup);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(MarkupEntity markup) {
    Resources<Resource<MarkupEntity>> result = (Resources<Resource<MarkupEntity>>) controller
        .readAll(new FilterSortPaginate()).getBody();
    assertThat(result.getContent()).haveAtLeastOne(
        new Condition<>(p -> p.getContent().getTagId().equals(markup.getTagId()), "markup exists"));
  }
  
  private MarkupEntity newMarkup(String tagId, String content) {
    MarkupEntity markup = new MarkupEntity();
    markup.setTagId(tagId);
    markup.setContent(content);
    return markup;
  }
}
