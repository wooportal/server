package de.codeschluss.wooportal.server.core.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

/**
 * The Class ImageService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class ImageService extends ResourceDataService<ImageEntity, ImageQueryBuilder>  {

  private final ImageConfiguration config;

  public ImageService(
      ImageConfiguration config,
      ImageRepository repo,
      ImageQueryBuilder entities,
      PagingAndSortingAssembler assembler) {
    super(repo, entities, assembler);
    this.config = config;
  }
  
  @Override
  public ImageEntity getExisting(ImageEntity newEntity) {
    return repo.findOne(entities.withId(newEntity.getId())).orElse(null);
  }
  
  @Override
  public boolean validCreateFieldConstraints(ImageEntity newImage) {
    return validFields(newImage);
  }
  
  @Override
  public boolean validUpdateFieldConstraints(ImageEntity newImage) {
    return validFields(newImage);
  }

  /**
   * Valid fields.
   *
   * @param image the image
   * @return true, if successful
   */
  public boolean validFields(ImageEntity image) {
    return image.getImageData() != null && !image.getImageData().isEmpty()
        && image.getMimeType() != null && !image.getMimeType().isEmpty()
        && image.getMimeType().contains("/");
  }
  
  @Override
  public ImageEntity update(String id, ImageEntity updatedEntity) {
    return repo.findById(id).map(image -> {
      image.setCaption(updatedEntity.getCaption());
      setImageDate(image);
      return repo.save(image);
    }).orElseGet(() -> {
      updatedEntity.setId(id);
      return repo.save(updatedEntity);
    });
  }

  @Override
  public ImageEntity add(ImageEntity image) {
    setImageDate(image);
    return repo.save(image);
  }
  
  private ImageEntity setImageDate(ImageEntity image) {
    try {
      byte[] resizedImage = resizeImage(image);
      image.setImage(resizedImage);
      return image;
    } catch (IOException e) {
      return null;
    }
  }
  
  private String extractFormatFromMimeType(String mimeType) {
    String[] parts = mimeType.split("/");
    return parts[1];
  }
 
  /**
   * Resize.
   *
   * @param image the image
   * @return the byte[]
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public byte[] resizeImage(ImageEntity image) throws IOException {
    if (image.getImage() != null && image.getImage().length > 1) {
      return image.getImage();
    }
    
    byte[] imageByte = Base64Utils.decodeFromString(image.getImageData());
    if (imageByte == null || imageByte.length == 1) {
      return null;
    }

    String formatType = extractFormatFromMimeType(image.getMimeType());
    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByte);
    BufferedImage imageBuff = ImageIO.read(inputStream);
    
    return needsResize(imageBuff, formatType)
        ? resize(imageBuff, formatType)
        : imageByte;
  }
  
  public String extractFormatFromUrl(String imageUrl) {
    String[] splitUrl = imageUrl.split("\\.");
    return splitUrl[splitUrl.length - 1];
  }

  /**
   * Gets the image data from url.
   *
   * @param imageUrl the image url
   * @return the image data from url
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public byte[] getImageFromUrl(String imageUrl, String formatType) 
      throws IOException {
    URL url = new URL(imageUrl);
    BufferedImage imageBuff = ImageIO.read(url);
    return needsResize(imageBuff, formatType)
        ? resize(imageBuff, formatType)
        : convertToByte(imageBuff, formatType);
  }
  
  private boolean needsResize(BufferedImage imageBuff, String formatType) {
    return !formatType.contains("svg") &&
        (imageBuff.getHeight() >= config.getMaxHeight() 
          || imageBuff.getWidth() >= config.getMaxWidth());
  }
  
  private byte[] resize(BufferedImage imageBuff, String formatType) 
      throws IOException {
    BufferedImage resized = Scalr.resize(
        imageBuff, Method.ULTRA_QUALITY, config.getMaxWidth(), config.getMaxWidth());
    
    return convertToByte(resized, formatType);
  }

  private byte[] convertToByte(BufferedImage image, String mimeType) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, mimeType, outputStream);
    return outputStream.toByteArray();
  }
}

