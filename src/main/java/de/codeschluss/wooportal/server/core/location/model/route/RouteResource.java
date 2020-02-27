
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class RouteResource {

  private String type;
  private List<Double> bbox;
  private String id;
  private String distanceUnit;
  private String durationUnit;
  private List<RouteLeg> routeLegs;
  private String trafficCongestion;
  private String trafficDataUsed;
  private Double travelDistance;
  private Integer travelDuration;
  private Integer travelDurationTraffic;
  private String travelMode;
  private Map<String, Object> additionalProperties;

}
