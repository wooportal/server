package de.codeschluss.wooportal.server.integration.markup;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.label.LabelController;
import de.codeschluss.wooportal.server.components.label.LabelEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkupControllerReadOneTest {

  @Autowired
  private LabelController controller;

  @Test
  public void findOneOk() {
    String labelId = "00000000-0000-0000-0028-100000000000";

    Resource<LabelEntity> result = (Resource<LabelEntity>) controller.readOne(labelId);

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findLabelNotFound() {
    String labelId = "00000000-0000-0000-0028-XX0000000000";

    controller.readOne(labelId);
  }
}
