package de.codeschluss.wooportal.server.core.i18n.xliff;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
  
  public List<Transunit> body;
  
  public String datatype;
  
  public String original;
  
  @JsonProperty("source-language")
  public String sourceLanguage;
  
  public String text;
  
}
