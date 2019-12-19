package de.codeschluss.wooportal.server.components.video;

import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.util.List;
import java.util.stream.Collectors;
import javax.naming.ServiceUnavailableException;
import lombok.var;
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
  
  /**
   * Adds the all.
   *
   * @param newVideos the new videos
   * @param organisation the organisation
   * @return the list
   */
  public List<VideoEntity> addAll(List<VideoEntity> newVideos, OrganisationEntity organisation) {
    return newVideos.stream().map(video -> {
      try {
        video.setOrganisation(organisation);
        return add(video);
      } catch (ServiceUnavailableException e) {
        return null;
      }
    }).collect(Collectors.toList());
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

  /**
   * Are orga videos.
   *
   * @param organisationId the organisation id
   * @param videoIds the video ids
   * @return true, if successful
   */
  public boolean areOrgaVideos(String organisationId, List<String> videoIds) {
    for (String id : videoIds) {
      var something = repo.findOne(entities.withIdAndOrgaId(id, organisationId)).orElse(null);
      
      if (something == null) {
        return false;
      }
    }
    return true;
  }
}
