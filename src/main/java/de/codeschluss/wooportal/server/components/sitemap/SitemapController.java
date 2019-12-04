package de.codeschluss.wooportal.server.components.sitemap;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class SitemapController.
 */
@RestController
public class SitemapController {
  
  private final SitemapService sitemapService;
  
  public SitemapController(
      SitemapService service) {
    this.sitemapService = service;
  }
  
  @GetMapping(
      path = "/sitemap", 
      headers = "Accept=*/*", 
      produces = { MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE },
      consumes = MediaType.ALL_VALUE)
  public ResponseEntity<Sitemap> getSitemap() {
    return ok(sitemapService.generateSitemap());
  }
}
