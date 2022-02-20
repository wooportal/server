package de.codeschluss.wooportal.server.core.analytics.app;

import com.opencsv.bean.CsvBindByName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class GoogleDownladDto {

  @CsvBindByName(column = "Date")
  private String date;
  
  @CsvBindByName(column = "Active Device Installs")
  private Double installs;
  
}
