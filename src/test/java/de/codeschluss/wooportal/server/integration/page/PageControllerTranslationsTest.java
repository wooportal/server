package de.codeschluss.wooportal.server.integration.page;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.page.PageController;
import de.codeschluss.wooportal.server.components.page.translations.PageTranslatablesEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageControllerTranslationsTest {

  @Autowired
  private PageController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findTranslationsOk() {
    String pageId = "00000000-0000-0000-0016-100000000000";

    Resources<Resource<PageTranslatablesEntity>> result = 
        (Resources<Resource<PageTranslatablesEntity>>) controller
        .readTranslations(pageId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTranslationsNotFound() {
    String pageId = "00000000-0000-0000-0016-XX0000000000";

    controller.readTranslations(pageId);
  }
}
