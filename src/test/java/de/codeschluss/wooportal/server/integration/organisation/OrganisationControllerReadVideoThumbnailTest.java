package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.integration.helper.ImageReader;
import java.io.IOException;
import lombok.var;
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
public class OrganisationControllerReadVideoThumbnailTest {

  @Autowired
  private OrganisationController controller;
  
  @Autowired
  private ImageReader imageReader;

  @Test
  @WithUserDetails("super@user")
  public void addVideoThumbnailSuperUserOk() throws IOException {
    
    String organisationId = "00000000-0000-0000-0008-100000000000";
    String videoId = "00000000-0000-0000-0020-200000000000";
    var thumbnail = newImageEntity();
    
    controller.addVideoThumbnail(organisationId, videoId, thumbnail);

    assertContaining(organisationId, videoId, thumbnail.getId());
  }

  @Test
  @WithUserDetails("admin@user")
  public void addVideoThumbnailOwnOrgaOk() throws IOException {
    
    String organisationId = "00000000-0000-0000-0008-100000000000";
    String videoId = "00000000-0000-0000-0020-200000000000";
    var thumbnail = newImageEntity();
    
    controller.addVideoThumbnail(organisationId, videoId, thumbnail);

    assertContaining(organisationId, videoId, thumbnail.getId());
  }
  
  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("admin@user")
  public void addOtherOrgaVideoThumbnailDenied() throws IOException {
    
    String organisationId = "00000000-0000-0000-0008-200000000000";
    String videoId = "00000000-0000-0000-0020-600000000000";
    var thumbnail = newImageEntity();
    
    controller.addVideoThumbnail(organisationId, videoId, thumbnail);
  }
  
  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void addVideosNotRegisteredDenied() throws IOException {

    String organisationId = "00000000-0000-0000-0008-100000000000";
    String videoId = "00000000-0000-0000-0020-200000000000";
    var thumbnail = newImageEntity();
    
    controller.addVideoThumbnail(organisationId, videoId, thumbnail);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void addVideoThumbnailByOrganisationNotFound() {
    String organisationId = "00000000-0000-0000-0008-XX0000000000";
    String videoId = "00000000-0000-0000-0020-200000000000";
    var thumbnail = newImageEntity();
    
    controller.addVideoThumbnail(organisationId, videoId, thumbnail);
  }
  
  private ImageEntity newImageEntity() {
    ImageEntity image = new ImageEntity();
    image.setCaption("test");
    image.setMimeType(imageReader.getMimeType());
    image.setImageData(imageReader.getBase64Picture());
    return image;
  }
  
  private void assertContaining(String organisationId, String videoId, String imageId) {
    ImageEntity result = (ImageEntity) controller
        .readVideoThumbnail(organisationId, videoId).getBody();
    assertThat(result.getId()).isEqualTo(imageId);
  }
}
