package de.codeschluss.wooportal.server.core.analytics.search;

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
import de.codeschluss.wooportal.server.core.analytics.AnalyticsDto;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;

@RestController
public class SearchConsoleController {
  
  private final SearchConsoleService service;
  
  public SearchConsoleController(
      SearchConsoleService service) {
    this.service = service;
  }
  
  @GetMapping("/search-console/overview")
  @SuperUserPermission
  public ResponseEntity<SearchAnalyticsDto> searchConsoleOverview(
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDate startDate, 
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDate endDate) throws IOException {
    return ok(service.calculateTotal(startDate, endDate));
  }
  
  @GetMapping("/search-console/details")
  @SuperUserPermission
  public ResponseEntity<List<AnalyticsDto>> searchConsoleDetails(
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDate startDate, 
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDate endDate,
      @RequestParam SearchDimension dimension) throws IOException {
    return ok(service.calculateForDimension(startDate, endDate, dimension));
  }

}
