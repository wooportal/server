package de.codeschluss.wooportal.server.components.sitemap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@JacksonXmlRootElement(localName = "urlset", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
public class Sitemap {
  
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "url", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
  private List<SitemapUrl> urlset;
  
  /**
   * Adds the url.
   *
   * @param newUrl the new url
   */
  public void addUrl(SitemapUrl newUrl) {
    if (urlset == null) {
      urlset = new ArrayList<>();
    }
    urlset.add(newUrl);
  }
  
  /**
   * Adds the urls.
   *
   * @param urls the urls
   */
  public void addUrls(List<SitemapUrl> urls) {
    if (urlset == null) {
      urlset = new ArrayList<>();
    }
    urlset.addAll(urls);
  }
}
