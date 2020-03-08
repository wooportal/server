package de.codeschluss.wooportal.server.core.location.model.address;

import java.util.List;
import lombok.Data;

/**
 * The Class AddressResource.
 * 
 * @author Valmir Etemi
 *
 */
@Data
public class AddressResource {

  private String type;
  private List<Double> bbox;
  private String name;
  private Point point;
  private Address address;
  private String confidence;
  private String entityType;
  private List<GeocodePoint> geocodePoints;
  private List<String> matchCodes;
}
