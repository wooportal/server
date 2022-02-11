package de.codeschluss.wooportal.server.core.analytics;

import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.core.api.dto.BooleanPrimitive;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {
  
  private final AnalyticsService analyticsService;
  
  public AnalyticsController(
      AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }
  
  @GetMapping("/analytic/activities/categories")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> calculateActivitiesPerCategory(
      BooleanPrimitive current) {
    return ok(analyticsService.calculateActivitiesPerCategory(current));
  }
  
  @GetMapping("/analytic/activities/suburbs")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> calculateActivitiesPerSuburbs(
      BooleanPrimitive current) {
    return ok(analyticsService.calculateActivitiesPerSuburb(current));
  }
  
  @GetMapping("/analytic/activities/targetgroups")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> calculateActivitiesPerTargetGroup(
      BooleanPrimitive current) {
    return ok(analyticsService.calculateActivitiesPerTargetGroup(current));
  }
  
  @GetMapping("/analytic/subscriptions")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsEntry>> calculateSubscriptions() {
    return ok(analyticsService.calculateSubscriptions());
  }

}
