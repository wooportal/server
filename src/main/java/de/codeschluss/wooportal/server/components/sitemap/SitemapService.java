package de.codeschluss.wooportal.server.components.sitemap;

import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.blog.BlogService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.page.PageService;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.net.URL;
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
   * @param requestUrl 
   *
   * @return the object
   */
  public Sitemap generateSitemap(URL requestUrl) {
    StringBuilder baseUrlBuilder = getBaseUrlBuilder(requestUrl);
    Sitemap map = new Sitemap();
    
    map.addUrl(new SitemapUrl(baseUrlBuilder.toString(), null));
    map.addUrls(getStaticUrls(baseUrlBuilder));
    map.addUrls(getEntityUrls(activityService, baseUrlBuilder, "activities"));
    map.addUrls(getEntityUrls(blogService, baseUrlBuilder, "blogposts"));
    map.addUrls(getEntityUrls(orgaService, baseUrlBuilder, "organisations"));
    map.addUrls(getEntityUrls(pageService, baseUrlBuilder, "infopages"));
    return map;
  }

  private StringBuilder getBaseUrlBuilder(URL requestUrl) {
    StringBuilder builder = new StringBuilder();
    builder.append(requestUrl.getProtocol());
    builder.append("://");
    builder.append(requestUrl.getAuthority());
    builder.append("/");
    return builder;
  }

  /**
   * Gets the static urls.
   *
   * @param baseUrlBuilder the base url builder
   * @return the static urls
   */
  private List<SitemapUrl> getStaticUrls(StringBuilder baseUrlBuilder) {
    return config.getStaticUrls().stream().map(url -> {
      return new SitemapUrl(
          new StringBuilder(baseUrlBuilder).append(url).toString(),
          null
      );
    }).collect(Collectors.toList());
  }

  /**
   * gets the entity urls.
   *
   * @param <D> the generic type
   * @param service the service
   * @param baseUrlBuilder the base url builder
   * @param resource the resource
   * @return the entity urls
   */
  public <D extends ResourceDataService<? extends BaseEntity,?>> List<SitemapUrl> getEntityUrls(
      D service, 
      StringBuilder baseUrlBuilder, 
      String resource) {
    
    StringBuilder resourceUrlBuilder = new StringBuilder(baseUrlBuilder);
    resourceUrlBuilder.append(resource);
    resourceUrlBuilder.append("/");
    
    return service.getAll().stream().map(entity -> {
      StringBuilder urlBuilder = new StringBuilder(resourceUrlBuilder);
      urlBuilder.append(entity.getId());
      return new SitemapUrl(
          urlBuilder.toString(), 
          dateFormatter.format(entity.getModified())
      );
    }).collect(Collectors.toList());
  }
}
