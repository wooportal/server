package de.codeschluss.wooportal.server.core.location.model.address;

import java.util.List;
import lombok.Data;

/**
 * The Class GeocodePoint.
 * 
 * @author Valmir Etemi
 *
 */
@Data
public class GeocodePoint {

  private String type;
  private List<Double> coordinates;
  private String calculationMethod;
  private List<String> usageTypes;
  
}
