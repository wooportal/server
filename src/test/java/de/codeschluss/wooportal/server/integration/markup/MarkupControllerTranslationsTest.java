package de.codeschluss.wooportal.server.integration.markup;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.markup.MarkupController;
import de.codeschluss.wooportal.server.components.markup.translations.MarkupTranslatablesEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkupControllerTranslationsTest {

  @Autowired
  private MarkupController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findTranslationsOk() {
    String markupId = "00000000-0000-0000-0029-100000000000";

    Resources<Resource<MarkupTranslatablesEntity>> result = 
        (Resources<Resource<MarkupTranslatablesEntity>>) controller
        .readTranslations(markupId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTranslationsNotFound() {
    String markupId = "00000000-0000-0000-0029-XX0000000000";

    controller.readTranslations(markupId);
  }
}
