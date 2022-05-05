package de.codeschluss.wooportal.server.components.video;

import java.io.IOException;
import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

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

  @Override
  public VideoEntity add(VideoEntity video) {
    video.setThumbnail(getThumbnail(video));
    return repo.save(video);
  }

  public ImageEntity getThumbnail(VideoEntity video) {
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
}
