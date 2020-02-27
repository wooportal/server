
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class RouteLeg {

  private ActualEnd actualEnd;
  private ActualStart actualStart;
  private List<Object> alternateVias;
  private Integer cost;
  private String description;
  private List<ItineraryItem> itineraryItems;
  private String routeRegion;
  private List<RouteSubLeg> routeSubLegs;
  private Double travelDistance;
  private Integer travelDuration;
  private Map<String, Object> additionalProperties;

}
