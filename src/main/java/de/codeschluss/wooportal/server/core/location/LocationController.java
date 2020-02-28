package de.codeschluss.wooportal.server.core.location;

import static org.springframework.http.ResponseEntity.ok;

import de.codeschluss.wooportal.server.core.exception.BadParamsException;
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
  
  /**
   * Calculate route.
   *
   * @param params the params
   * @return the response entity
   * @throws ServiceUnavailableException the service unavailable exception
   */
  @GetMapping("/locations")
  public ResponseEntity<?> calculateRoute(
      LocationParam params) throws ServiceUnavailableException {
    if (isValid(params)) {
      return ok(mapService.calculateRoute(params)); 
    }
    throw new BadParamsException("Start or target is empty");
  }

  private boolean isValid(LocationParam params) {
    return params.getStartPoint() != null
        && (params.getStartPoint().getLatitude() != 0 
          || params.getStartPoint().getLongitude() != 0)
      && params.getTargetPoint() != null
        && (params.getTargetPoint().getLatitude() != 0
          || params.getTargetPoint().getLongitude() != 0);
  }

}
