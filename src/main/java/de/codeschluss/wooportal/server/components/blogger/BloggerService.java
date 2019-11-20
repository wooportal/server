package de.codeschluss.wooportal.server.components.blogger;

import de.codeschluss.wooportal.server.components.user.UserEntity;
import de.codeschluss.wooportal.server.components.user.UserService;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.DuplicateEntryException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.mail.MailConfiguration;
import de.codeschluss.wooportal.server.core.mail.MailService;
import de.codeschluss.wooportal.server.core.mail.MailTemplateService;
import de.codeschluss.wooportal.server.core.repository.DataRepository;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.mail.MessagingException;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

/**
 * The Class BloggerService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class BloggerService extends ResourceDataService<BloggerEntity, BloggerQueryBuilder> {
  
  /** The user service. */
  private final UserService userService;
  
  /** The mail config. */
  private final MailService mailService;
  
  /** The template service. */
  private final MailTemplateService templateService;
  
  /** The mail config. */
  private final MailConfiguration mailConfig;

  /**
   * Instantiates a new blogger service.
   *
   * @param repo the repo
   * @param entities the entities
   * @param assembler the assembler
   * @param mailService the mail service
   * @param userService the user service
   * @param templateService the template service
   * @param mailConfig the mail config
   */
  public BloggerService(
      DataRepository<BloggerEntity> repo, 
      BloggerQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      MailService mailService,
      UserService userService,
      MailTemplateService templateService,
      MailConfiguration mailConfig) {
    super(repo, entities, assembler);
    
    this.userService = userService;
    this.mailService = mailService;
    this.templateService = templateService;
    this.mailConfig = mailConfig;
  }

  @Override
  public BloggerEntity getExisting(BloggerEntity newBlogger) {
    try {
      return getByUser(newBlogger.getUser().getId());
    } catch (NotFoundException e) {
      return null;
    }
  }
  
  @Override
  public boolean validCreateFieldConstraints(BloggerEntity newEntity) {
    return newEntity.getUser().getBlogger() != null;
  }

  @Override
  public boolean validUpdateFieldConstraints(BloggerEntity newEntity) {
    return newEntity.getUser().getBlogger() != null;
  }

  /**
   * Gets the by user.
   *
   * @param userId
   *          the user id
   * @return the by user
   */
  public BloggerEntity getByUser(String userId) {
    return repo.findOne(entities.withUserId(userId))
        .orElseThrow(() -> new NotFoundException(userId));
  }
  
  /**
   * Gets the by user.
   *
   * @param userId
   *          the user id
   * @return the by user
   */
  public Resource<?> getResourceByUser(String userId) {
    return assembler.toResource(getByUser(userId));
  }

  @Override
  public <P extends FilterSortPaginate> Resources<?> getSortedListResources(P params) {
    List<BloggerEntity> result = getSortedList(params);

    if (result == null || result.isEmpty()) {
      throw new NotFoundException("no bloggers found");
    }
    return assembler.toListResources(convertToEmbedded(result.stream()), params);
  }

  @Override
  public <P extends FilterSortPaginate> PagedResources<Resource<?>> getPagedResources(P params) {
    Page<BloggerEntity> pagedResult = getPaged(params);

    if (pagedResult == null || pagedResult.isEmpty()) {
      throw new NotFoundException("no bloggers found");
    }

    return assembler.toPagedResources(convertToEmbedded(pagedResult.stream()), pagedResult, params);
  }

  /**
   * Convert to embedded.
   *
   * @param stream
   *          the stream
   * @return the list
   */
  public List<Resource<?>> convertToEmbedded(Stream<BloggerEntity> stream) {
    return stream.map(blogger -> {
      Map<String, Object> embedded = new HashMap<>();
      embedded.put("blogger", blogger);
      return assembler.resourceWithEmbeddable(blogger.getUser(), embedded);
    }).collect(Collectors.toList());
  }

  @Override
  public BloggerEntity update(String id, BloggerEntity updatedEntity) {
    throw new NotImplementedException("For security reasons");
  }

  /**
   * Approve by user id.
   *
   * @param userId the user id
   */
  public void approveByUserId(String userId) {
    BloggerEntity blogger = getByUser(userId);
    blogger.setApproved(true);
    repo.save(blogger);
  }

  /**
   * Creates the application.
   *
   * @param user
   *          the user
   */
  public BloggerEntity createApplication(UserEntity user) {
    BloggerEntity blogger = new BloggerEntity();
    blogger.setApproved(false);
    blogger.setUser(user);
    if (getExisting(blogger) == null) {
      BloggerEntity added = repo.save(blogger);
      this.sendApplicationMail(blogger);
      return added;
    } else {
      throw new DuplicateEntryException("Blogger already exists");
    }
  }

  private boolean sendApplicationMail(BloggerEntity blogger) {
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("userName", blogger.getUser().getName());
      model.put("portalName", mailConfig.getPortalName());
      String subject = "Neue Bloggeranfrage";

      mailService.sendEmail(
          subject, 
          templateService.createMessage("newblogger.ftl", model), 
          userService.getSuperUserMails().toArray(new String[0]));
      return true;
    } catch (IOException | TemplateException | MessagingException e) {
      return false;
    }
  }

  /**
   * Delete by user.
   *
   * @param userId the user id
   */
  public void deleteByUser(String userId) {
    repo.delete(getByUser(userId));
  }
}
