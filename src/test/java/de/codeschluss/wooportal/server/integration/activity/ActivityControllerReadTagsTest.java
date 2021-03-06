package de.codeschluss.wooportal.server.integration.activity;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.activity.ActivityController;
import de.codeschluss.wooportal.server.components.tag.TagEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityControllerReadTagsTest {

  @Autowired
  private ActivityController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findTagsOk() {
    String activityId = "00000000-0000-0000-0010-100000000000";

    Resources<Resource<TagEntity>> result = (Resources<Resource<TagEntity>>) controller
        .readTags(activityId, null).getBody();

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTagNotFound() {
    String activityId = "00000000-0000-0000-0010-XX0000000000";

    controller.readTags(activityId, null);
  }
}
