package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;
import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganisationControllerAddVideoThumbnailTest {

  @Autowired
  private OrganisationController controller;
  
  @Test
  public void addVideosNotRegisteredDenied() throws IOException {
    String imageId = "00000000-0000-0000-0017-400000000000";
    String organisationId = "00000000-0000-0000-0008-100000000000";
    String videoId = "00000000-0000-0000-0020-100000000000";
    
    ImageEntity image = (ImageEntity) controller
        .readVideoThumbnail(organisationId, videoId).getBody();
    
    assertThat(image.getId()).isEqualTo(imageId);
    assertThat(image.getImageData()).isNotBlank();
  }
}
