package de.codeschluss.wooportal.server.integration.tag;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.tag.TagController;
import de.codeschluss.wooportal.server.components.tag.translations.TagTranslatablesEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagControllerTranslationsTest {

  @Autowired
  private TagController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findTranslationsOk() {
    String tagId = "00000000-0000-0000-0002-100000000000";

    Resources<Resource<TagTranslatablesEntity>> result = 
        (Resources<Resource<TagTranslatablesEntity>>) controller
        .readTranslations(tagId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTranslationsNotFound() {
    String tagId = "00000000-0000-0000-0002-XX0000000000";

    controller.readTranslations(tagId);
  }
}
