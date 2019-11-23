package de.codeschluss.wooportal.server.integration.page;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.page.PageController;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageControllerIncreaseLike {
  
  @Autowired
  private PageController controller;

  @Test
  public void increaseLikeOk() {
    String pageId = "00000000-0000-0000-0010-100000000000";
    int likesCount = controller.readOne(pageId).getContent().getLikes();
    
    controller.increaseLike(pageId);
    
    assertThat(controller.readOne(pageId).getContent().getLikes()).isEqualTo(likesCount + 1); 
  }

  @Test(expected = BadParamsException.class)
  public void increaseLikeNotFound() {
    String pageId = "00000000-0000-0000-0010-XX0000000000";

    controller.increaseLike(pageId);
  }

}
