package de.codeschluss.wooportal.server.components.blog;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.mail.MailService;
import de.codeschluss.wooportal.server.core.repository.DataRepository;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

/**
 * The Class BlogService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class BlogService extends ResourceDataService<BlogEntity, BlogQueryBuilder> {
  
  private final MailService mailService;

  /**
   * Instantiates a new blog service.
   *
   * @param repo
   *          the repo
   * @param entities
   *          the entities
   * @param assembler
   *          the assembler
   */
  public BlogService(
      DataRepository<BlogEntity> repo, 
      BlogQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      MailService mailService) {
    super(repo, entities, assembler);
    
    this.mailService = mailService;
  }

  @Override
  public BlogEntity getExisting(BlogEntity blog) {
    return repo.findById(blog.getId()).orElse(null);
  }
  
  @Override
  public boolean validCreateFieldConstraints(BlogEntity newBlog) {
    return validFields(newBlog);
  }

  @Override
  public boolean validUpdateFieldConstraints(BlogEntity newBlog) {
    return validFields(newBlog);
  }
  
  
  /**
   * Valid fields.
   *
   * @param newBlog the new blog
   * @return true, if successful
   */
  private boolean validFields(BlogEntity newBlog) {
    return newBlog.getTitle() != null && !newBlog.getTitle().isEmpty()
        && newBlog.getContent() != null && !newBlog.getContent().isEmpty();
  }

  /**
   * Checks if is blog user.
   *
   * @param blogId
   *          the blog id
   * @param userId
   *          the user id
   * @return true, if is blog user
   */
  public boolean isBlogUser(String blogId, String userId) {
    return repo.exists(entities.withBlogIdAndUserId(blogId, userId));
  }

  @Override
  public BlogEntity update(String id, BlogEntity newBlog) {
    return repo.findById(id).map(blog -> {
      blog.setContent(newBlog.getContent());
      blog.setTitle(newBlog.getTitle());
      return repo.save(blog);
    }).orElseGet(() -> {
      newBlog.setId(id);
      return repo.save(newBlog);
    });
  }

  /**
   * Gets the resource by user.
   *
   * @param userId
   *          the user id
   * @param params
   *          the params
   * @return the resource by user
   * @throws JsonParseException
   *           the json parse exception
   * @throws JsonMappingException
   *           the json mapping exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public Resources<?> getResourceByUser(String userId, BaseParams params)
      throws JsonParseException, JsonMappingException, IOException {
    List<BlogEntity> result = getByUser(userId, params);
    if (result == null || result.isEmpty()) {
      throw new NotFoundException("For params: " + params + " and User with id: " + userId);
    }
    return assembler.entitiesToResources(result, params);
  }
  
  /**
   * Gets the by user.
   *
   * @param userId
   *          the user id
   * @return the by user
   */
  public List<BlogEntity> getByUser(String userId, BaseParams params) {
    Predicate query = entities.withUserId(userId);
    List<BlogEntity> result = params == null ? repo.findAll(query)
        : repo.findAll(query, entities.createSort(params));

    if (result == null || result.isEmpty()) {
      return Collections.emptyList();
    }

    return transformList(result);
  }
  
  public Resources<?> getResourcesByTopic(String topicId, BaseParams params)
      throws JsonParseException, JsonMappingException, IOException {
    var pages = repo.findAll(
        entities.withTopicId(topicId), 
        entities.createSort(params));
    if (pages == null || pages.isEmpty()) {
      throw new NotFoundException(topicId);
    }
    return assembler.entitiesToResources(pages, params);
  }
  
  public Resource<?> updateResourceWithTopic(String blogId, TopicEntity topic) {
    var page = getById(blogId);
    page.setTopic(topic);
    return assembler.toResource(repo.save(page));
  }

  @Override
  public <P extends FilterSortPaginate> List<BlogEntity> getSortedList(P params) {
    List<BlogEntity> nonEmptyResult = super.getSortedList(params);
    return transformList(nonEmptyResult);
  }

  /**
   * Transform.
   *
   * @param stream
   *          the stream
   * @return the list
   */
  private List<BlogEntity> transformList(List<BlogEntity> list) {
    return list.parallelStream().map(blog -> {
      return transformSingle(blog);
    }).collect(Collectors.toList());
  }

  /**
   * Gets the paged.
   *
   * @param <P>
   *          the generic type
   * @param params
   *          the params
   * @return the paged
   */
  public <P extends FilterSortPaginate> Page<BlogEntity> getPaged(P params) {
    Page<BlogEntity> nonEmptyPage = super.getPaged(params);
    return nonEmptyPage.map(blog -> transformSingle(blog));
  }

  /**
   * Transform single.
   *
   * @param findOne
   *          the find one
   * @return the optional
   */
  private BlogEntity transformSingle(BlogEntity single) {
    single.setAuthor(single.getBlogger().getUser().getName());
    return single;
  }

  /**
   * Increase like.
   *
   * @param blogId
   *          the blog id
   */
  public void increaseLike(String blogId) {
    BlogEntity blog = getById(blogId);
    blog.setLikes(blog.getLikes() + 1);
    repo.save(blog);
  }
  
  /**
   * Gets the images.
   *
   * @param id the id
   * @return the images
   */
  public List<ImageEntity> getImages(String id) {
    List<ImageEntity> result = getById(id).getImages();
    if (result == null || result.isEmpty()) {
      throw new NotFoundException("No images found");
    }
    return result;
  }
  
  /**
   * Adds the images.
   *
   * @param id the id
   * @param images the images
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<ImageEntity> addImages(
      String id,
      List<ImageEntity> images) throws IOException {
    BlogEntity savedEntity = null;
    for (ImageEntity image : images) {
      savedEntity = addImage(id, image);
    }
    return savedEntity.getImages();
  }

  /**
   * Adds the image.
   *
   * @param id the id
   * @param image the image
   * @return the blog entity
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public BlogEntity addImage(String id, ImageEntity image) throws IOException {
    BlogEntity blog = getById(id);
    blog.getImages().add(image);
    return repo.save(blog);
  }
  
  public void setApproval(String blogId, Boolean isApproved) {
    var blog = getById(blogId);
    blog.setApproved(isApproved);
    repo.save(blog);
    if (isApproved) {
      this.sendApprovalMail(blog);
    }
  }

  private void sendApprovalMail(BlogEntity blog) {
    if (blog.getMailAddress() != null && !blog.getMailAddress().isEmpty()) {
      Map<String, Object> model = new HashMap<>();
      model.put("title", blog.getTitle());
      model.put("author", blog.getAuthor());
      String subject = "Beitrag veröffentlicht";
      mailService.sendEmail(
          subject, 
          "approvedblog.ftl", 
          model, 
          blog.getMailAddress());
    }

  }
}
