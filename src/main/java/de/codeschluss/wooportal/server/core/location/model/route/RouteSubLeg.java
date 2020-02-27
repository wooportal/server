
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.Map;
import lombok.Data;

@Data
public class RouteSubLeg {

  private EndWaypoint endWaypoint;
  private StartWaypoint startWaypoint;
  private Double travelDistance;
  private Integer travelDuration;
  private Map<String, Object> additionalProperties;

}
