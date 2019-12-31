package de.codeschluss.wooportal.server.integration.activity;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.activity.ActivityController;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityControllerIncreaseLike {
  
  @Autowired
  private ActivityController controller;

  @Test
  public void increaseLikeOk() {
    String activityId = "00000000-0000-0000-0010-100000000000";
    int likesCount = controller.readOne(activityId).getContent().getLikes();
    
    controller.increaseLike(activityId, null);
    
    assertThat(controller.readOne(activityId).getContent().getLikes()).isEqualTo(likesCount + 1); 
  }

  @Test(expected = BadParamsException.class)
  public void increaseLikeNotFound() {
    String activityId = "00000000-0000-0000-0010-XX0000000000";

    controller.increaseLike(activityId, null);
  }

}
