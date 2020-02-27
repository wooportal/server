
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class EndWaypoint {

  private String type;
  private List<Double> coordinates;
  private String description;
  private Boolean isVia;
  private String locationIdentifier;
  private Integer routePathIndex;
  private Map<String, Object> additionalProperties;
}
