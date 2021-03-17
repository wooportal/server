package de.codeschluss.wooportal.server.core.i18n.xliff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Xliff {
  
  public File file;
  
  public String xmlns;
  
  public String version;
  
  public String text;
}
