package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.image.ImageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganisationControllerCreateAndFindImagesTest {

  @Autowired
  private OrganisationController controller;

  @MockBean
  private ImageService imageService;

  @SuppressWarnings("unchecked")
  @Test
  @WithUserDetails("super@user")
  public void addAndFindImagesSuperUserOk() throws IOException {
    given(this.imageService.resize(Mockito.any())).willReturn("test".getBytes());
    
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        "test", Base64Utils.encodeToString("test".getBytes()), "image/png"));
    controller.addImage("00000000-0000-0000-0008-100000000000", imageInput);
    
    List<ImageEntity> result = (List<ImageEntity>) controller
        .readImages("00000000-0000-0000-0008-100000000000").getBody();

    assertThat(result).isNotEmpty();
  }

  @SuppressWarnings("unchecked")
  @Test
  @WithUserDetails("admin@user")
  public void addAndFindImagesOwnOrgaOk() throws IOException {
    given(this.imageService.resize(Mockito.any())).willReturn("test".getBytes());
    
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        "test", Base64Utils.encodeToString("test".getBytes()), "image/png"));
    controller.addImage("00000000-0000-0000-0008-100000000000", imageInput);

    List<ImageEntity> result = (List<ImageEntity>) controller
        .readImages("00000000-0000-0000-0008-100000000000").getBody();

    assertThat(result).isNotEmpty();
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("admin@user")
  public void addNotValidImageDenied() throws IOException {
    given(this.imageService.resize(Mockito.any())).willReturn(null);
    
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        "test", null, "image/png"));
    
    controller.addImage("00000000-0000-0000-0008-100000000000", imageInput);
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("admin@user")
  public void addNullMimeTypeDenied() throws IOException {
    given(this.imageService.resize(Mockito.any())).willReturn(null);
    
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        "test", "test", null));
    
    controller.addImage("00000000-0000-0000-0008-100000000000", imageInput);
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("admin@user")
  public void addNotValidMimeTypeDenied() throws IOException {
    given(this.imageService.resize(Mockito.any())).willReturn(null);
    
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        "test", "test", "notvalid"));
    
    controller.addImage("00000000-0000-0000-0008-100000000000", imageInput);
  }
  
  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void addAndFindImagesOtherOrgaDenied() throws IOException {
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        "test", Base64Utils.encodeToString("test".getBytes()), "test"));
    
    controller.addImage("00000000-0000-0000-0008-100000000000", imageInput);
  }
  
  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void addAndFindImagesNotRegisteredDenied() throws IOException {
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity(
        "test", Base64Utils.encodeToString("test".getBytes()), "test"));
    
    controller.addImage("00000000-0000-0000-0008-100000000000", imageInput);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = NotFoundException.class)
  public void findImagesByOrganisationNotFound() {
    List<ImageEntity> result = (List<ImageEntity>) controller
        .readImages("00000000-0000-0000-0008-XX0000000000").getBody();

    assertThat(result).isNotEmpty();
  }
  
  /**
   * New organisation image entity.
   *
   * @param string the string
   * @param encodeToString the encode to string
   * @return the organisation image entity
   */
  private ImageEntity newImageEntity(
      String caption, 
      String data,
      String mimeType) {
    ImageEntity image = new ImageEntity();
    image.setCaption(caption);
    image.setImageData(data);
    image.setMimeType(mimeType);
    return image;
  }
}
