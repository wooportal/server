package de.codeschluss.wooportal.server.components.sitemap;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sitemap")
public class SitemapConfiguration {
  
  private String baseUrl;
  private List<String> staticUrls;
}
