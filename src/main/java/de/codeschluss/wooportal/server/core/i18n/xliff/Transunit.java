package de.codeschluss.wooportal.server.core.i18n.xliff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transunit {
  
  public String source;
  
  public String target;
  
  public String datatype;
  
  public String id;
  
  public String text;
}
