package de.codeschluss.wooportal.server.integration.helper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "test")
public class TestConfiguration {
  
  private String picturePath;

}
