package de.codeschluss.wooportal.server.core.location;

import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.core.location.dto.LocationParam;
import javax.naming.ServiceUnavailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {
  
  private final BingMapService mapService;
  
  public LocationController(
      BingMapService mapService) {
    this.mapService = mapService;
  }
  
  @GetMapping("/locations")
  public ResponseEntity<?> readAll(
      LocationParam params) throws ServiceUnavailableException {
    return ok(mapService.calculateRoute(params));
  }

}
