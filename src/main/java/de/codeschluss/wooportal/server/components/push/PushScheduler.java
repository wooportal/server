package de.codeschluss.wooportal.server.components.push;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PushScheduler {
  
  private final PushService pushService;
  
  public PushScheduler(PushService pushService) {
    this.pushService = pushService;
  }
  
  @Scheduled(cron = "0 30 9 * * ?")  
  public void pushMorning() {
    pushService.pushActivityReminders();
  }

}
