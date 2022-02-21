package de.codeschluss.wooportal.server.core.analytics.app;

import static org.springframework.http.ResponseEntity.ok;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.codeschluss.wooportal.server.core.analytics.AnalyticsEntry;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;

@RestController
public class AppStatisticsController {
  
  private final AppStatisticsService service;
  
  public AppStatisticsController(
      AppStatisticsService service) {
    this.service = service;
  }
  
  @GetMapping("/app-statistics/install")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> appStatisticsInstalls(
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate, 
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) throws IOException {
    validateDates(startDate, endDate);
    return ok(service.calculateInstalls(startDate, endDate));
  }

  @GetMapping("/app-statistics/ratings")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> appStatisticsRatings(
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate, 
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) throws IOException {
    validateDates(startDate, endDate);
    return ok(service.calculateRatings(startDate, endDate));
  }
  
  private void validateDates(LocalDate startDate, LocalDate endDate) {
    if (endDate.isBefore(startDate)) {
      throw new BadParamsException("endDate must be after startDate");
    }
    var now = LocalDate.now();
    if (startDate.isAfter(now) || endDate.isAfter(now)) {
      throw new BadParamsException("dates must be in past");
    }
  }
  
  
}
