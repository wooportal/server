package de.codeschluss.wooportal.server.components.topic;

import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import org.springframework.stereotype.Service;

@Service
public class TopicService extends ResourceDataService<TopicEntity, TopicQueryBuilder> {
  
  /**
   * Instantiates a new topic service.
   *
   * @param repo
   *          the repo
   * @param assembler
   *          the assembler
   */
  public TopicService(
      TopicRepository repo, 
      PagingAndSortingAssembler assembler,
      TopicQueryBuilder entities) {
    super(repo, entities, assembler);
  }

  @Override
  public TopicEntity getExisting(TopicEntity newTopic) {
    return repo.findOne(entities.withName(newTopic.getName())).orElse(null);
  }
  
  @Override
  public boolean validCreateFieldConstraints(TopicEntity newTopic) {
    return validFields(newTopic);
  }
  
  @Override
  public boolean validUpdateFieldConstraints(TopicEntity newTopic) {
    return validFields(newTopic);
  }

  /**
   * Valid fields.
   *
   * @param newTopic the new page
   * @return true, if successful
   */
  private boolean validFields(TopicEntity newTopic) {
    return newTopic.getName() != null && !newTopic.getName().isEmpty();
  }

  @Override
  public TopicEntity update(String id, TopicEntity newTopic) {
    return repo.findById(id).map(topic -> {
      topic.setName(newTopic.getName());
      return repo.save(topic);
    }).orElseGet(() -> {
      newTopic.setId(id);
      return repo.save(newTopic);
    });
  }

  /**
   * Gets the resource by blog.
   *
   * @param blogId the blig id
   * @return the resource by blog
   */
  public Object getResourceByBlog(String blogId) {
    TopicEntity topic = repo.findOne(entities.withAnyBlogId(blogId))
        .orElseThrow(() -> new NotFoundException(blogId));
    return assembler.toResource(topic);
  }
}
