package de.codeschluss.wooportal.server.core.location.model.address;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * The Class Point.
 * 
 * @author Valmir Etemi
 *
 */
@Data
public class Point {

  private String type;
  private List<Float> coordinates;
  private Map<String, Object> additionalProperties;

  public float getLatitude() {
    return coordinates.get(0);
  }

  public float getLongitude() {
    return coordinates.get(1);
  }

}
