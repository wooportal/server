package de.codeschluss.wooportal.server.components.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = "value")
public class AnalyticsEntry implements Comparable<AnalyticsEntry> {
  
  private String name;
  
  private double value; 
  
  @Override
  public int compareTo(AnalyticsEntry other) {
    return this.getName().compareTo(other.getName());
  }

}
