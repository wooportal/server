
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ItineraryItem {

  private String compassDirection;
  private List<Detail> details;
  private String exit;
  private String iconType;
  private Instruction instruction;
  private Boolean isRealTimeTransit;
  private ManeuverPoint maneuverPoint;
  private Integer realTimeTransitDelay;
  private String sideOfStreet;
  private String tollZone;
  private String towardsRoadName;
  private String transitTerminus;
  private Integer travelDistance;
  private Integer travelDuration;
  private String travelMode;
  private List<Hint> hints;
  private Map<String, Object> additionalProperties;

}