package de.codeschluss.wooportal.server.core.analytics.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "playstore")
public class PlayStoreConfig {
  
  private String bucket;
  
  private String credentials;
  
  private String installs;
  
  private String project;
  
  private String ratings;
  
  private String scope;
}
