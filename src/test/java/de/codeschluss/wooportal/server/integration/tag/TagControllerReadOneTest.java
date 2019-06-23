package de.codeschluss.wooportal.server.integration.tag;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.tag.TagController;
import de.codeschluss.wooportal.server.components.tag.TagEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagControllerReadOneTest {

  @Autowired
  private TagController controller;

  @Test
  public void findOneOk() {
    String tagId = "00000000-0000-0000-0002-100000000000";

    Resource<TagEntity> result = (Resource<TagEntity>) controller.readOne(tagId);

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findTagNotFound() {
    String tagId = "00000000-0000-0000-0002-XX0000000000";

    controller.readOne(tagId);
  }
}
