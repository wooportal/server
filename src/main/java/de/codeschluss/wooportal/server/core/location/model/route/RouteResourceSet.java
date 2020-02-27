
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class RouteResourceSet {

  private Integer estimatedTotal;
  private List<RouteResource> resources;
  private Map<String, Object> additionalProperties;

}
