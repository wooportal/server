package de.codeschluss.wooportal.server.components.push;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PushScheduler {
  
  private final PushService pushService;
  
  public PushScheduler(PushService pushService) {
    this.pushService = pushService;
  }
  
  
  @Scheduled(cron = "30 18 * * *")  
  public void pushOncePerDay() {
  }
  
  @Scheduled(cron = "0 */2 * * *")  
  public void pushEveryTwoHour() {
    pushService.pushActivityReminders();
  }

}
