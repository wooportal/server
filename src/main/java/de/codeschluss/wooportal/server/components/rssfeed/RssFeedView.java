package de.codeschluss.wooportal.server.components.rssfeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.blog.BlogService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.config.GeneralPropertyConfiguration;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;


@Component
public class RssFeedView extends AbstractRssFeedView {

  private final ActivityService activityService;
  
  private final BlogService blogService;
  
  private final GeneralPropertyConfiguration generalConfig;
  
  private final OrganisationService orgaService;
  
  @Autowired
  protected HttpServletRequest request;

  public RssFeedView(ActivityService activityService,
      BlogService blogService, OrganisationService orgaService,
      GeneralPropertyConfiguration generalConfig) {
    this.activityService = activityService;
    this.blogService = blogService;
    this.orgaService = orgaService;
    this.generalConfig = generalConfig;
  }

  @Override
  protected void buildFeedMetadata(Map<String, Object> model, Channel feed,
      HttpServletRequest request) {
    feed.setTitle(generalConfig.getPortalName() + " RSS Feed");
    feed.setDescription(generalConfig.getPortalName() + " News");
    feed.setLink(request.getHeader("host"));
  }

  @Override
  protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
      HttpServletResponse response) {

    List<Item> feedItem = new ArrayList<>();

    List<BaseResource> entities = sortEntities(activityService);
    entities.addAll(sortEntities(blogService));
    entities.addAll(sortEntities(orgaService));

    entities.sort((c1, c2) -> c1.getCreated().compareTo(c2.getCreated()));
    entities = entities.subList(0, 9);

    for (var entity : entities) {

      if (entity instanceof ActivityEntity) {
        var activity = (ActivityEntity) entity;
        feedItem.add(new RssFeedItem(activity.getName(),
            activity.getMail() + " (" + activity.getContactName() + ")",
            activity.selfLink().getHref().toString().replace("api/", ""), activity.getCreated(),
            activity.selfLink().getHref().toString(), activity.getDescription()));

      } else if (entity instanceof BlogEntity) {
        var blog = (BlogEntity) entity;
        feedItem.add(new RssFeedItem(blog.getTitle(), blog.getMailAddress() + " (" + blog.getAuthor() + ")",
            blog.selfLink().getHref().toString(), blog.getCreated(), blog.getId(),
            blog.getTopic().getName()));

      } else if (entity instanceof OrganisationEntity) {
        var orga = (OrganisationEntity) entity;
        feedItem.add(new RssFeedItem(orga.getName(), orga.getMail() + " (" + orga.getName() + ")",
            orga.selfLink().getHref().toString(), orga.getCreated(), orga.getId(),
            orga.getDescription()));
      }
    }
    return feedItem;
  }

  private List<BaseResource> sortEntities(ResourceDataService<?, ?> tempService) {
    try {
      var entities = new ArrayList<BaseResource>();
      var filter = new FilterSortPaginate();
      filter.setDir("desc");
      filter.setPage(1);
      filter.setSize(10);
      filter.setSort("created");
      entities.addAll(tempService.getPaged(filter).getContent());
      return entities;
    } catch (NotFoundException e) {
      return new ArrayList<BaseResource>();
    }

  }
}
