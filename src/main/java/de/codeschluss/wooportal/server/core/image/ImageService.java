package de.codeschluss.wooportal.server.core.image;

import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

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
  public boolean validCreateFieldConstraints(ImageEntity newOrgaImage) {
    return validFields(newOrgaImage);
  }
  
  @Override
  public boolean validUpdateFieldConstraints(ImageEntity newOrgaImage) {
    return validFields(newOrgaImage);
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

  public ImageEntity add(ImageEntity image) {
    setImageDate(image);
    return repo.save(image);
  }
  
  private ImageEntity setImageDate(ImageEntity image) {
    try {
      byte[] resizedImage = resize(image);
      image.setImage(resizedImage);
      image.setImageData(Base64Utils.encodeToString(resizedImage));
      return image;
    } catch (IOException e) {
      return null;
    }
  }
 
  /**
   * Resize.
   *
   * @param image the image
   * @return the byte[]
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public byte[] resize(ImageEntity image) throws IOException {
    byte[] imageByte = Base64Utils.decodeFromString(image.getImageData());
    if (imageByte == null || imageByte.length == 1) {
      return null;
    }

    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByte);
    BufferedImage imageBuff = ImageIO.read(inputStream);
    
    if (imageBuff.getHeight() <= config.getMaxHeight()
        && imageBuff.getWidth() <= config.getMaxWidth()) {
      return imageByte;
    }
    BufferedImage resized = Scalr.resize(
        imageBuff, Method.ULTRA_QUALITY, config.getMaxWidth(), config.getMaxWidth());
    
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(resized, extractFormatType(image.getMimeType()), outputStream);
    return outputStream.toByteArray();
  }
  
  private String extractFormatType(String mimeType) {
    String[] parts = mimeType.split("/");
    return parts[1];
  }
}
