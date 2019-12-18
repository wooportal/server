package de.codeschluss.wooportal.server.components.video;

import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import org.springframework.stereotype.Service;

/**
 * The Class VideoService.
 *  
 * @author Valmir Etemi
 */
@Service
public class VideoService extends ResourceDataService<VideoEntity, VideoQueryBuilder> {

  public VideoService(
      VideoRepository repo,
      VideoQueryBuilder entities,
      PagingAndSortingAssembler assembler) {
    super(repo, entities, assembler);
  }

  @Override
  public VideoEntity getExisting(VideoEntity video) {
    return repo.findOne(entities.withId(video.getId())).orElse(null);
  }

  @Override
  public boolean validCreateFieldConstraints(VideoEntity video) {
    return validFields(video);
  }

  @Override
  public boolean validUpdateFieldConstraints(VideoEntity video) {
    return validFields(video);
  }
  
  public boolean validFields(VideoEntity video) {
    return video.getUrl() != null && !video.getUrl().isEmpty();
  }
  

  @Override
  public VideoEntity update(String id, VideoEntity updatedEntity) {
    return repo.findById(id).map(video -> {
      video.setUrl(updatedEntity.getUrl());
      return repo.save(video);
    }).orElseGet(() -> {
      updatedEntity.setId(id);
      return repo.save(updatedEntity);
    });
  }
}
