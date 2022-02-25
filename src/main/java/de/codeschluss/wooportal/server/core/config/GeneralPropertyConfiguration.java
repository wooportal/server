package de.codeschluss.wooportal.server.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "general")
public class GeneralPropertyConfiguration {

  private String portalName;
  
  private String clientIpHeader;
  
}
