
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class Detail {

  private Integer compassDegrees;
  private List<Integer> endPathIndices;
  private String maneuverType;
  private String mode;
  private List<String> names;
  private String roadType;
  private List<Integer> startPathIndices;
  private List<String> locationCodes;
  private Map<String, Object> additionalProperties;

}
