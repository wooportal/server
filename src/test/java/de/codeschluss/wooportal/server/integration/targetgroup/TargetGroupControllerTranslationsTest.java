package de.codeschluss.wooportal.server.integration.targetgroup;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupController;
import de.codeschluss.wooportal.server.components.targetgroup.translations.TargetGroupTranslatablesEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TargetGroupControllerTranslationsTest {

  @Autowired
  private TargetGroupController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findTranslationsOk() {
    String targetGroupId = "00000000-0000-0000-0003-100000000000";

    Resources<Resource<TargetGroupTranslatablesEntity>> result = 
        (Resources<Resource<TargetGroupTranslatablesEntity>>) controller
        .readTranslations(targetGroupId).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTranslationsNotFound() {
    String targetGroupId = "00000000-0000-0000-0003-XX0000000000";

    controller.readTranslations(targetGroupId);
  }
}
