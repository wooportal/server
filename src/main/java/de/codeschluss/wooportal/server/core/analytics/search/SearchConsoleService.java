package de.codeschluss.wooportal.server.core.analytics.search;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.searchconsole.v1.SearchConsole;
import com.google.api.services.searchconsole.v1.model.SearchAnalyticsQueryRequest;
import com.google.auth.oauth2.GoogleCredentials;
import de.codeschluss.wooportal.server.core.analytics.AnalyticsDto;
import de.codeschluss.wooportal.server.core.config.GeneralPropertyConfiguration;

@Service
public class SearchConsoleService {
  
  private final GeneralPropertyConfiguration generalConfig;
  
  private final SearchConsoleConfig searchConfig;
  
  private final String clicks = "clicks";
  
  private final String impressions = "impressions";
  
  public SearchConsoleService(
      SearchConsoleConfig searchConfig,
      GeneralPropertyConfiguration generalConfig) {
    this.generalConfig = generalConfig;
    this.searchConfig = searchConfig;
  }
  
  public SearchAnalyticsDto calculateTotal(
      LocalDate startDate, LocalDate endDate) throws IOException {
    var searchService = createSearchService();
    var query = new SearchAnalyticsQueryRequest()
        .setSearchType(null)
        .setStartDate(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        .setEndDate(endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

    var result = new SearchAnalyticsDto();
    for (var row : searchService
        .searchanalytics()
        .query(generalConfig.getHost(), query)
        .execute()
        .getRows()) {
      result.addClicks(row.getClicks().intValue());
      result.addImpressions(row.getImpressions().intValue());
      result.addPosition(row.getPosition());
      result.addCtr(row.getCtr());
    }

    return result;
  }
  
  public List<AnalyticsDto> calculateForDimension(
      LocalDate startDate, LocalDate endDate, SearchDimension dimension) 
          throws IOException {
    var searchService = createSearchService();
    var query = new SearchAnalyticsQueryRequest()
        .setSearchType(null)
        .setStartDate(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        .setEndDate(endDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        .setDimensions(Arrays.asList(dimension.toString()));

    var result = new HashMap<String, HashMap<String, Double>>();
    for (var row : searchService
        .searchanalytics()
        .query(generalConfig.getHost(), query)
        .execute()
        .getRows()) {
      var entry = result.containsKey(row.getKeys().get(0))
          ? result.get(row.getKeys().get(0))
          : new HashMap<String, Double>();
      entry.put(clicks, 
          entry.containsKey(clicks) ? entry.get(clicks) + row.getClicks() : row.getClicks());
      entry.put(impressions, 
          entry.containsKey(impressions) ? entry.get(impressions) + row.getImpressions() : row.getImpressions());
      
      result.put(row.getKeys().get(0), entry);
    }
    
    return result.entrySet().stream().map(e -> new AnalyticsDto(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  public SearchConsole createSearchService() throws IOException {
//  // As of now, this cannot be passed to SearchConsole.Builder
//  var credentials = GoogleCredentials
//    .fromStream(new ClassPathResource(searchConfig.getCredentials())
//        .getInputStream())
//    .createScoped(Collections.singletonList(searchConfig.getScope()));
  
  // Only the deprecated method works
  @SuppressWarnings("deprecation")
  var credentials = GoogleCredential
    .fromStream(new ClassPathResource(searchConfig.getCredentials())
        .getInputStream())
    .createScoped(Collections.singletonList(searchConfig.getScope()));

  return new SearchConsole.Builder(
      new NetHttpTransport(), 
      new GsonFactory(), 
      credentials).build();
  }
  
}
