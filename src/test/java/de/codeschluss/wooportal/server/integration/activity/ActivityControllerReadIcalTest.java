package de.codeschluss.wooportal.server.integration.activity;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.TimeZone;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.activity.ActivityController;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ActivityControllerReadIcalTest {
  
  @Autowired
  private ActivityController controller;

  @Test
  public void IcalNotNull() throws IOException {

    String activityId = "00000000-0000-0000-0010-100000000000";
    ResponseEntity<?> result = controller.generateAllIcal(activityId);
    assertThat(result).isNotNull();
  }

  @Test
  public void IcalRightFormat() throws IOException, ParserException {
    
    String activityId = "00000000-0000-0000-0010-100000000000";
    ActivityEntity activity = controller.readOne(activityId).getContent();
    ResponseEntity<String> result = controller.generateAllIcal(activityId);

    Calendar c = new CalendarBuilder().build(new StringReader(result.getBody()));
    Component vevent = c.getComponent("VEVENT").get();
    assertThat(vevent.getProperty("summary").get().getValue()).isEqualTo(activity.getName());

    ScheduleEntity schedule = activity.getSchedules().get(0);

    assertThat(vevent.getProperty("DTSTART").get().getValue())
        .isEqualTo(new DtStart<>(LocalDateTime.ofInstant(schedule.getStartDate().toInstant(),
            TimeZone.getDefault().toZoneId())).getValue());
    assertThat(vevent.getProperty("DTEND").get().getValue())
        .isEqualTo(new DtEnd<>(LocalDateTime.ofInstant(schedule.getEndDate().toInstant(),
            TimeZone.getDefault().toZoneId())).getValue());

    assertThat(vevent.getProperty("Organizer").get().getValue())
        .isEqualTo("MAILTO:" + activity.getMail());
    assertThat(vevent.getProperty("Organizer").get().getParameter("CN").get())
        .isEqualTo(new Cn(activity.getContactName()));

    assertThat(vevent.getProperty("Description").get().getValue())
        .isEqualTo(activity.getDescription());

    assertThat(vevent.getProperty("Location").get().getValue())
        .isEqualTo(activity.getAddress().getStreet() + " " + activity.getAddress().getHouseNumber()
            + " " + activity.getAddress().getPostalCode() + " " + activity.getAddress().getPlace());

    assertThat(vevent.getProperty("Categories").get().getValue())
        .isEqualTo(activity.getCategory().getName());

    assertThat(vevent.getProperty("Geo").get().getValue()).isEqualTo(
        activity.getAddress().getLatitude() + ";" + activity.getAddress().getLongitude());

    assertThat(vevent.getProperty("UID").get().getValue()).isNotBlank();
  }

  @Test(expected = NotFoundException.class)
  public void TestScheduleIdExceptionThrown() throws IOException, ParserException {

    String scheduleId = "";
    String activityId = "00000000-0000-0000-0010-100000000000";

    controller.generateIcal(activityId, scheduleId);
  }

  @Test(expected = NotFoundException.class)
  public void TestActivityIdExceptionThrown() throws IOException, ParserException {

    String scheduleId = "00000000-0000-0000-0011-100000000000   ";
    String activityId = "";

    controller.generateIcal(activityId, scheduleId);
  }
}

