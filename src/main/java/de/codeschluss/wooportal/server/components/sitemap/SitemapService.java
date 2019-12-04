package de.codeschluss.wooportal.server.components.sitemap;

import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.blog.BlogService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.page.PageService;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SitemapService {
  
  private final SimpleDateFormat dateFormatter;
  
  private final SitemapConfiguration config;
  
  private final ActivityService activityService;
  private final BlogService blogService;
  private final OrganisationService orgaService;
  private final PageService pageService;
  
  /**
   * Instantiates a new sitemap service.
   *
   * @param config the config
   * @param activityService the activity service
   * @param blogService the blog service
   * @param orgaService the orga service
   * @param pageService the page service
   */
  public SitemapService(
      SitemapConfiguration config,
      ActivityService activityService,
      BlogService blogService,
      OrganisationService orgaService,
      PageService pageService) {
    this.config = config;
    this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    this.activityService = activityService;
    this.blogService = blogService;
    this.pageService = pageService;
    this.orgaService = orgaService;
  }
  
  /**
   * Generate sitemap.
   *
   * @return the object
   */
  public Sitemap generateSitemap() {
    Sitemap map = new Sitemap();
    map.addUrls(getStaticUrls());
    map.addUrls(getEntityUrls(activityService, "/activities/"));
    map.addUrls(getEntityUrls(blogService, "/blogposts/"));
    map.addUrls(getEntityUrls(orgaService, "/organisations/"));
    map.addUrls(getEntityUrls(pageService, "/infopages/"));
    return map;
  }

  private List<SitemapUrl> getStaticUrls() {
    return config.getStaticUrls().stream().map(url -> {
      return new SitemapUrl(
          url,
          null
      );
    }).collect(Collectors.toList());
  }

  /**
   * Gets the entity urls.
   *
   * @param service the service
   * @param path the path
   * @return the entity urls
   */
  public <D extends ResourceDataService<? extends BaseEntity,?>> 
      List<SitemapUrl> getEntityUrls(D service, String path) {
    return service.getAll().stream().map(entity -> {
      StringBuilder urlBuilder = new StringBuilder();
      urlBuilder.append(config.getBaseUrl());
      urlBuilder.append(path);
      urlBuilder.append(entity.getId());
      return new SitemapUrl(
          urlBuilder.toString(), 
          dateFormatter.format(entity.getModified())
      );
    }).collect(Collectors.toList());
  }
}
