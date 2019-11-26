package de.codeschluss.wooportal.server.components.images.activities;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageService;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@Service
public class ActivityImageService 
    extends ResourceDataService<ActivityImageEntity, ActivityImageQueryBuilder> {
  
  private final ImageService imageService;

  /**
   * Instantiates a new activity image service.
   *
   * @param repo the repo
   * @param entities the entities
   * @param assembler the assembler
   * @param imageService the image service
   */
  public ActivityImageService(
      ActivityImageRepository repo, 
      ActivityImageQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      ImageService imageService) {
    super(repo, entities, assembler);
    this.imageService = imageService;
  }
  
  @Override
  public ActivityImageEntity getExisting(ActivityImageEntity newEntity) {
    return repo.findOne(entities.withId(newEntity.getId())).orElse(null);
  }
  
  @Override
  public boolean validCreateFieldConstraints(ActivityImageEntity newActivityImage) {
    return validFields(newActivityImage);
  }
  
  @Override
  public boolean validUpdateFieldConstraints(ActivityImageEntity newActivityImage) {
    return validFields(newActivityImage);
  }

  /**
   * Valid fields.
   *
   * @param newActivityImage the new activity image
   * @return true, if successful
   */
  private boolean validFields(ActivityImageEntity newActivityImage) {
    return newActivityImage.getImageData() != null && !newActivityImage.getImageData().isEmpty();
  }

  @Override
  public ActivityImageEntity update(String id, ActivityImageEntity updatedEntity) {
    return repo.findById(id).map(image -> {
      image.setCaption(updatedEntity.getCaption());
      image.setImage(updatedEntity.getImage());
      return repo.save(image);
    }).orElseGet(() -> {
      updatedEntity.setId(id);
      return repo.save(updatedEntity);
    });
  }

  /**
   * Gets the resources by activity.
   *
   * @param activityId the activity id
   * @return the resources by activity
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Resources<?> getResourcesByActivity(String activityId) 
       throws JsonParseException, JsonMappingException, IOException {
    List<ActivityImageEntity> images = repo.findAll(
        entities.forActivityId(activityId));
    if (images == null || images.isEmpty()) {
      throw new NotFoundException(activityId);
    }
    
    return assembler.entitiesToResources(encodeImages(images), null);
  }
  
  /**
   * Encode images.
   *
   * @param images the images
   * @return the list
   */
  private List<ActivityImageEntity> encodeImages(List<ActivityImageEntity> images) {
    return images.stream().map(image -> {
      image.setImageData(Base64Utils.encodeToString(image.getImage()));
      return image;
    }).collect(Collectors.toList());
  }

  /**
   * Adds the resources.
   *
   * @param activity the activity
   * @param images the images
   * @return the resources
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Resources<?> addResources(
      ActivityEntity activity,
      List<ActivityImageEntity> images) throws IOException {
    List<Resource<?>> savedImages = new ArrayList<>();
    for (ActivityImageEntity image : images) {
      savedImages.add(addResource(image, activity));
    }
    return assembler.toListResources(savedImages, null);
  }

  /**
   * Adds the resource.
   *
   * @param image the input image
   * @param activity the activity
   * @return the resource
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Resource<?> addResource(
      ActivityImageEntity image, 
      ActivityEntity activity) throws IOException {
    if (image.getImageData() == null || image.getImageData().isEmpty()
        || image.getMimeType() == null || image.getMimeType().isEmpty()
        || !image.getMimeType().contains("/")) {
      throw new BadParamsException("Image or Mime Type with correct form required");
    }
    
    String formatType = extractFormatType(image.getMimeType());
    byte[] resizedImage = imageService.resize(
        Base64Utils.decodeFromString(image.getImageData()),
        formatType);
    
    image.setActivity(activity);
    image.setImage(resizedImage);
    image.setImageData(Base64Utils.encodeToString(resizedImage));
    ActivityImageEntity saved = repo.save(image);
    return assembler.toResource(saved);
  }

  private String extractFormatType(String mimeType) {
    String[] parts = mimeType.split("/");
    return parts[1];
  }
}
