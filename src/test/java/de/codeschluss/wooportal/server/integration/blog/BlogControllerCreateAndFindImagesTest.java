package de.codeschluss.wooportal.server.integration.blog;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.blog.BlogController;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.integration.helper.ImageReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogControllerCreateAndFindImagesTest {

  @Autowired
  private BlogController controller;

  @Autowired
  private ImageReader imageReader;

  @SuppressWarnings("unchecked")
  @Test
  @WithUserDetails("super@user")
  public void addAndFindImagesSuperUserOk() throws IOException {
    
    String blogId = "00000000-0000-0000-0016-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        imageReader.getBase64Picture(),
        imageReader.getMimeType()));
    
    controller.addImage(blogId, imageInput);
    
    List<ImageEntity> result = (List<ImageEntity>) controller
        .readImages(blogId).getBody();

    assertThat(result).isNotEmpty();
  }

  @SuppressWarnings("unchecked")
  @Test
  @WithUserDetails("blog1@user")
  public void addAndFindImagesOwnBloggerOk() throws IOException {
    
    String blogId = "00000000-0000-0000-0016-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        imageReader.getBase64Picture(),
        imageReader.getMimeType()));
    
    controller.addImage(blogId, imageInput);

    List<ImageEntity> result = (List<ImageEntity>) controller
        .readImages(blogId).getBody();

    assertThat(result).isNotEmpty();
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("blog1@user")
  public void addNotValidImageDenied() throws IOException {
    
    String blogId = "00000000-0000-0000-0016-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        null,
        imageReader.getMimeType()));
    
    controller.addImage(blogId, imageInput);
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("blog1@user")
  public void addNullMimeTypeDenied() throws IOException {
    
    String blogId = "00000000-0000-0000-0016-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        imageReader.getBase64Picture(),
        null));
    
    controller.addImage(blogId, imageInput);
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("blog1@user")
  public void addNotValidMimeTypeDenied() throws IOException {
    
    String blogId = "00000000-0000-0000-0016-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        imageReader.getBase64Picture(),
        "notvalid"));
    
    controller.addImage(blogId, imageInput);
  }
  
  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("blog2@user")
  public void addAndFindImagesOtherOrgaDenied() throws IOException {
    
    String blogId = "00000000-0000-0000-0016-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        imageReader.getBase64Picture(),
        imageReader.getMimeType()));
    
    controller.addImage(blogId, imageInput);
  }
  
  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void addAndFindImagesNotRegisteredDenied() throws IOException {
    
    String blogId = "00000000-0000-0000-0016-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        imageReader.getBase64Picture(),
        imageReader.getMimeType()));
    
    controller.addImage(blogId, imageInput);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = NotFoundException.class)
  public void findImagesByBlogNotFound() {
    
    String blogId = "00000000-0000-0000-0016-XX0000000000";
    List<ImageEntity> result = (List<ImageEntity>) controller
        .readImages(blogId).getBody();

    assertThat(result).isNotEmpty();
  }
  
  private ImageEntity newImageEntity(
      String data,
      String mimeType) {
    ImageEntity image = new ImageEntity();
    image.setCaption("test");
    image.setImageData(data);
    image.setMimeType(mimeType);
    return image;
  }
}
