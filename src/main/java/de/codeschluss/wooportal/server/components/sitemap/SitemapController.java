package de.codeschluss.wooportal.server.components.sitemap;

import static org.springframework.http.ResponseEntity.ok;

import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
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
  
  @GetMapping("/sitemap")
  public ResponseEntity<Sitemap> getSitemap(HttpServletRequest request) 
      throws MalformedURLException {
    return ok(sitemapService.generateSitemap(new URL(request.getRequestURL().toString())));
  }
}
