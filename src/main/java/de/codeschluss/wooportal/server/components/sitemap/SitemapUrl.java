package de.codeschluss.wooportal.server.components.sitemap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
public class SitemapUrl {
  
  @JacksonXmlProperty(namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
  private String loc;
  
  @JacksonXmlProperty(namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
  private String lastmod;
  
}
