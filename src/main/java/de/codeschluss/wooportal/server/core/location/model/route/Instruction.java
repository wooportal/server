
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.Map;
import lombok.Data;

@Data
public class Instruction {

  private Object formattedText;
  private String maneuverType;
  private String text;
  private Map<String, Object> additionalProperties;

}
