package de.codeschluss.wooportal.server.integration.targetgroup;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupController;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TargetGroupControllerReadOneTest {

  @Autowired
  private TargetGroupController controller;

  @Test
  public void findOneOk() {
    String targetGroupId = "00000000-0000-0000-0003-100000000000";

    Resource<TargetGroupEntity> result = (Resource<TargetGroupEntity>) controller
        .readOne(targetGroupId);

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTargetGroupNotFound() {
    String targetGroupId = "00000000-0000-0000-0003-XX0000000000";

    controller.readOne(targetGroupId);
  }
}
