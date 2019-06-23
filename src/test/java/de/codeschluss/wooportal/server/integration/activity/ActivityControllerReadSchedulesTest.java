package de.codeschluss.wooportal.server.integration.activity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.activity.ActivityController;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityControllerReadSchedulesTest {

  @Autowired
  private ActivityController controller;

  @Test
  @SuppressWarnings("unchecked")
  public void findSchedulesOk() {
    String activityId = "00000000-0000-0000-0010-300000000000";

    Resources<Resource<ScheduleEntity>> result = (Resources<Resource<ScheduleEntity>>) controller
        .readSchedules(activityId, null).getBody();

    assertThat(result.getContent()).isNotNull();
    assertThat(result.getContent()).allMatch(
        schedule -> schedule.getContent().getStartDate().getTime() >= System.currentTimeMillis());
  }

  @Test(expected = NotFoundException.class)
  public void findScheduleNotFound() {
    String activityId = "00000000-0000-0000-0010-XX0000000000";

    controller.readSchedules(activityId, null);
  }
}
