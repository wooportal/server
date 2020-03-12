package de.codeschluss.wooportal.server.components.video;

import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.naming.ServiceUnavailableException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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

  public VideoService(VideoRepository repo, VideoQueryBuilder entities,
      PagingAndSortingAssembler assembler, ImageService imageService) {
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
   * Gets the resources with embeddables.
   *
   * @param orgaId the orga id
   * @return the resources with embeddables
   */
  public Resources<?> getResourcesWithEmbeddables(String orgaId) {
    List<VideoEntity> result = repo.findAll(entities.withOrgaId(orgaId));
    
    if (result == null || result.isEmpty()) {
      throw new NotFoundException("No videos found");
    }
    
    List<Resource<?>> embeddedVideos = result.stream().map(video -> {
      ImageEntity thumbnail = video.getThumbnail(); 
      if (thumbnail != null) {
        Map<String, Object> embedded = new HashMap<>();
        embedded.put("thumbnail", thumbnail);
        return assembler.resourceWithEmbeddable(video, embedded); 
      }
      return video.toResource();
    }).collect(Collectors.toList());
    
    return assembler.toListResources(embeddedVideos, null);
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
        video.setThumbnail(getThumbnail(video));
        return add(video);
      } catch (ServiceUnavailableException e) {
        return null;
      }
    }).collect(Collectors.toList());
  }

  private ImageEntity getThumbnail(VideoEntity video) {
    try {
      ImageEntity image = new ImageEntity();
      String formatType = imageService.extractFormatFromUrl(video.getThumbnailUrl());
      image.setMimeType("image/" + formatType);
      image.setImage(imageService.getImageFromUrl(video.getThumbnailUrl(), formatType));
      image.setCaption(video.getThumbnailCaption());
      return imageService.add(image);
    } catch (IOException e) {
      return null;
    }
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
    for (String videoId : videoIds) {
      Optional<VideoEntity> video = 
          repo.findOne(entities.withIdAndOrgaId(videoId, organisationId));
      if (!video.isPresent()) {
        return false;
      }
    }
    return true;
  }
  
}
