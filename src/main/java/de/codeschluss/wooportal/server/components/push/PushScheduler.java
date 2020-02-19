package de.codeschluss.wooportal.server.components.push;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PushScheduler {
  
  private final PushService pushService;
  
  public PushScheduler(PushService pushService) {
    this.pushService = pushService;
  }
  
  @Scheduled(cron = "0 0 12 * * ?")  
  public void pushNoon() {
    pushService.pushActivityReminders();
  }
  
  @Scheduled(cron = "0 0 16 * * ?")  
  public void pushAfternoon() {
    pushService.pushActivityReminders();
  }

}
