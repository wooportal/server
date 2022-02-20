package de.codeschluss.wooportal.server.core.analytics.app;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageOptions;
import com.opencsv.bean.CsvToBeanBuilder;
import de.codeschluss.wooportal.server.core.analytics.AnalyticsEntry;

@Service
public class AppStatisticsService {
  
  private final PlayStoreConfig config;
  
  public AppStatisticsService(PlayStoreConfig config) {
    this.config = config;
  }

  public List<AnalyticsEntry> calculateRatings(LocalDate startDate, LocalDate endDate) throws IOException {
    var csvFiles = getAnalyticsData(config.getRatings(), startDate, endDate);
    if (csvFiles != null && !csvFiles.isEmpty()) {
      var result = new ArrayList<AnalyticsEntry>();
      for (var csv : csvFiles) {
        var entries = new CsvToBeanBuilder<GoogleRatingDto>(new StringReader(csv))
            .withType(GoogleRatingDto.class).build().parse();
        
        if (entries != null && !entries.isEmpty()) {
          result.addAll(entries.stream().map(e -> new AnalyticsEntry(e.getDate(), e.getRating(), null))
              .collect(Collectors.toList()));
        }
      }
      return result;
    }
    return null;
  }

  public List<AnalyticsEntry> calculateInstalls(LocalDate startDate, LocalDate endDate) throws IOException {
    var csvFiles = getAnalyticsData(config.getInstalls(), startDate, endDate);
    if (csvFiles != null && !csvFiles.isEmpty()) {
      var result = new ArrayList<AnalyticsEntry>();
      for (var csv : csvFiles) {
        var entries = new CsvToBeanBuilder<GoogleDownladDto>(new StringReader(csv))
            .withType(GoogleDownladDto.class).build().parse();
        
        if (entries != null && !entries.isEmpty()) {
          result.addAll(entries.stream().map(e -> new AnalyticsEntry(e.getDate(), e.getInstalls(), null))
              .collect(Collectors.toList()));
        }
      }
      return result;
    }
    return null;
  }
  
  private List<String> getAnalyticsData(
      String baseUrl, LocalDate startDate, LocalDate endDate) throws IOException {
    var bucket = getBucket();
    return createDateFilter(startDate, endDate).stream().map(date -> {
      try {
        return new String(bucket.get(String.format("%s_%s_%s_overview.csv", baseUrl, config.getProject(), date)).getContent(), "UTF-16");
      } catch (UnsupportedEncodingException e) { return null; }
    }).collect(Collectors.toList());
  }

  private Bucket getBucket() throws IOException {
    return StorageOptions.newBuilder()
        .setCredentials(GoogleCredentials
            .fromStream(new ClassPathResource(config.getCredentials()).getInputStream())
            .createScoped(Arrays.asList(config.getScope()))).build().getService()
        .get(config.getBucket());
  }
  
  private List<String> createDateFilter(LocalDate startDate, LocalDate endDate) {
    List<String> dates = new ArrayList<>();
    for (var date = startDate.withDayOfMonth(1); date.isBefore(endDate) || date.isEqual(endDate);
        date = date.plus(1, ChronoUnit.MONTHS)) {
      dates.add(date.format(DateTimeFormatter.ofPattern("yyyyMM")));
    }
    return dates;
  }

}
