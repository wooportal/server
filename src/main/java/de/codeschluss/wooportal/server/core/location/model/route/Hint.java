
package de.codeschluss.wooportal.server.core.location.model.route;

import java.util.Map;
import lombok.Data;

@Data
public class Hint {

  private String hintType;
  private String text;
  private Map<String, Object> additionalProperties;

}
