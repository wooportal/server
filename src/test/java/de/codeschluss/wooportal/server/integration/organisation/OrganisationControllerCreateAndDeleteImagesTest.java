package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.integration.helper.ImageReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
public class OrganisationControllerCreateAndDeleteImagesTest {

  @Autowired
  private OrganisationController controller;
  
  @Autowired
  private ImageReader imageReader;

  @Test
  @WithUserDetails("super@user")
  @SuppressWarnings("unchecked")
  public void addAndDeleteImagesSuperUserOk() throws IOException {
    
    String organisationId = "00000000-0000-0000-0008-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity());
    
    List<ImageEntity> result = 
        ((List<ImageEntity>) controller
            .addImage(organisationId, imageInput).getBody());
   
    controller.deleteImages(organisationId, result.stream().map(
        imageRes -> imageRes.getId()).collect(Collectors.toList()));
    
    try {
      controller.readImages(organisationId);
      assertThat(false).isTrue();
    } catch (NotFoundException e) {
      assertThat(true).isTrue();
    }
  }

  @Test
  @WithUserDetails("admin@user")
  @SuppressWarnings("unchecked")
  public void addAndDeleteImagesOwnOrgaOk() throws IOException {
    
    String organisationId = "00000000-0000-0000-0008-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity());
    
    List<ImageEntity> result = 
        ((List<ImageEntity>) controller
            .addImage(organisationId, imageInput).getBody());
   
    controller.deleteImages(organisationId, result.stream().map(
        imageRes -> imageRes.getId()).collect(Collectors.toList()));
    
    try {
      controller.readImages(organisationId);
      assertThat(false).isTrue();
    } catch (NotFoundException e) {
      assertThat(true).isTrue();
    }
  }
  
  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  @SuppressWarnings("unchecked")
  public void addAndFindImagesOtherOrgaDenied() throws IOException {
    
    String organisationId = "00000000-0000-0000-0008-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity());
    
    List<ImageEntity> result = 
        ((List<ImageEntity>) controller
            .addImage(organisationId, imageInput).getBody());
   
    controller.deleteImages(organisationId, result.stream().map(
        imageRes -> imageRes.getId()).collect(Collectors.toList()));
  }
  
  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  @SuppressWarnings("unchecked")
  public void addAndFindImagesNotRegisteredDenied() throws IOException {
    
    String organisationId = "00000000-0000-0000-0008-100000000000";
    List<ImageEntity> imageInput = new ArrayList<>();
    imageInput.add(newImageEntity());
    
    List<ImageEntity> result = 
        ((List<ImageEntity>) controller
            .addImage(organisationId, imageInput).getBody());
   
    controller.deleteImages(organisationId, result.stream().map(
        imageRes -> imageRes.getId()).collect(Collectors.toList()));
  }
  
  private ImageEntity newImageEntity() {
    ImageEntity image = new ImageEntity();
    image.setCaption("test");
    image.setMimeType(imageReader.getMimeType());
    image.setImageData(imageReader.getBase64Picture());
    return image;
  }
}
