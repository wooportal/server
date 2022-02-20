package de.codeschluss.wooportal.server.components.push;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The Class PushConfig.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "push")
public class PushConfig {
  
  private String credentials;
  
  private String typeNews;
  
  private String typeSingleContent;
  
  private String typeNewContent;
  
  private String typeActivityReminder;
}
