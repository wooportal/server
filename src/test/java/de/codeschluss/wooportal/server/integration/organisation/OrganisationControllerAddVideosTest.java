package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.components.video.VideoEntity;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganisationControllerAddVideosTest {

  @Autowired
  private OrganisationController controller;

  @Test
  @WithUserDetails("super@user")
  public void addVideosSuperUserOk() throws Exception {
    
    String organisationId = "00000000-0000-0000-0008-100000000000";
    var video = newVideoEntity();
    List<VideoEntity> videoInput = new ArrayList<>();
    videoInput.add(video);
    
    controller.addVideos(organisationId, videoInput);
    
    controller.readVideos(organisationId).getBody();

    assertContaining(organisationId, video);
  }

  @Test
  @WithUserDetails("admin@user")
  public void addVideosOwnOrgaOk() throws Exception {
    
    String organisationId = "00000000-0000-0000-0008-100000000000";
    var video = newVideoEntity();
    List<VideoEntity> videoInput = new ArrayList<>();
    videoInput.add(video);
    
    controller.addVideos(organisationId, videoInput);
    
    controller.readVideos(organisationId).getBody();

    assertContaining(organisationId, video);
  }
  
  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("admin@user")
  public void addOtherOrgaVideoDenied() throws Exception {
    
    String organisationId = "00000000-0000-0000-0008-200000000000";
    var video = newVideoEntity();
    List<VideoEntity> videoInput = new ArrayList<>();
    videoInput.add(video);
    
    controller.addVideos(organisationId, videoInput);
  }
  
  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void addVideosNotRegisteredDenied() throws Exception {

    String organisationId = "00000000-0000-0000-0008-200000000000";
    var video = newVideoEntity();
    List<VideoEntity> videoInput = new ArrayList<>();
    videoInput.add(video);
    
    controller.addVideos(organisationId, videoInput);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void addVideosByOrganisationNotFound() throws Exception {
    String organisationId = "00000000-0000-0000-0008-XX0000000000";
    var video = newVideoEntity();
    List<VideoEntity> videoInput = new ArrayList<>();
    videoInput.add(video);
    
    controller.addVideos(organisationId, videoInput);
  }
  
  private VideoEntity newVideoEntity() {
    VideoEntity video = new VideoEntity();
    video.setUrl("testUrl");
    return video;
  }
  
  @SuppressWarnings("unchecked")
  private void assertContaining(String organisationId, VideoEntity video) {
    List<VideoEntity> result = (List<VideoEntity>) controller
        .readVideos(organisationId).getBody();
    assertThat(result).haveAtLeastOne(
        new Condition<>(p -> p.getId().equals(video.getId()), "video exists"));
  }
}
