package de.codeschluss.wooportal.server.components.video;

import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.naming.ServiceUnavailableException;
import org.springframework.stereotype.Service;

/**
 * The Class VideoService.
 *  
 * @author Valmir Etemi
 */
@Service
public class VideoService extends ResourceDataService<VideoEntity, VideoQueryBuilder> {

  /** The image service. */
  private final ImageService imageService;
  
  public VideoService(
      VideoRepository repo,
      VideoQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      ImageService imageService) {
    super(repo, entities, assembler);
    this.imageService = imageService;
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
   * Adds the thumbnail.
   *
   * @param videoId the video id
   * @param thumbnail the thumbnail
   * @return the video entity
   */
  public VideoEntity addThumbnail(String videoId, ImageEntity thumbnail) {
    VideoEntity video = getById(videoId);
    imageService.deleteExisting(video.getThumbnail());

    video.setThumbnail(thumbnail);
    return repo.save(video);
  }

  /**
   * Are orga videos.
   *
   * @param organisationId the organisation id
   * @param videoIds the video ids
   * @return true, if successful
   */
  public boolean belongsToOrga(String organisationId, List<String> videoIds) {
    for (String id : videoIds) {
      if (!belongsToOrga(organisationId, id)) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Belongs to orga.
   *
   * @param organisationId the organisation id
   * @param videoId the video id
   * @return true, if successful
   */
  public boolean belongsToOrga(String organisationId, String videoId) {
    Optional<VideoEntity> video = repo.findOne(entities.withIdAndOrgaId(videoId, organisationId));
    return video.isPresent();
  }

  /**
   * Gets the thumbnail.
   *
   * @param videoId the video id
   * @return the thumbnail
   */
  public ImageEntity getThumbnail(String videoId) {
    ImageEntity thumbnail = getById(videoId).getThumbnail();
    if (thumbnail != null) {
      return thumbnail;
    }
    throw new NotFoundException("No thumbnail found");
  }
}
