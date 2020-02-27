
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ManeuverPoint {

  private String type;
  private List<Double> coordinates;
  private Map<String, Object> additionalProperties;

}
