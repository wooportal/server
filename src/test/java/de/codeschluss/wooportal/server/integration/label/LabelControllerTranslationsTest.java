package de.codeschluss.wooportal.server.integration.label;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.label.LabelController;
import de.codeschluss.wooportal.server.components.label.translations.LabelTranslatablesEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LabelControllerTranslationsTest {

  @Autowired
  private LabelController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findTranslationsOk() {
    String labelId = "00000000-0000-0000-0028-100000000000";

    Resources<Resource<LabelTranslatablesEntity>> result = 
        (Resources<Resource<LabelTranslatablesEntity>>) controller
        .readTranslations(labelId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTranslationsNotFound() {
    String labelId = "00000000-0000-0000-0028-XX0000000000";

    controller.readTranslations(labelId);
  }
}
