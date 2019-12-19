package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.components.video.VideoEntity;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganisationControllerDeleteVideosTest {

  @Autowired
  private OrganisationController controller;

  @Test
  @WithUserDetails("super@user")
  public void deleteForOtherSuperUserOk() {
    String organisationId = "00000000-0000-0000-0008-100000000000";
    
    String videoId = "00000000-0000-0000-0020-200000000000";
    List<String> videoInput = new ArrayList<>();
    videoInput.add(videoId);

    assertContaining(organisationId, videoId);

    controller.deleteVideos(organisationId, videoInput);

    assertNotContaining(organisationId, videoId);
  }

  @Test
  @WithUserDetails("admin@user")
  public void deleteOwnOrganisationOk() {
    String organisationId = "00000000-0000-0000-0008-100000000000";
    
    String videoId = "00000000-0000-0000-0020-300000000000";
    List<String> videoInput = new ArrayList<>();
    videoInput.add(videoId);

    assertContaining(organisationId, videoId);

    controller.deleteVideos(organisationId, videoInput);

    assertNotContaining(organisationId, videoId);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("admin@user")
  public void deleteVideoForNotMatchingIdsDenied() {
    String organisationId = "00000000-0000-0000-0008-100000000000";
    
    String videoId = "00000000-0000-0000-0020-400000000000";
    List<String> videoInput = new ArrayList<>();
    videoInput.add(videoId);

    controller.deleteVideos(organisationId, videoInput);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void deleteForOtherDenied() {
    String organisationId = "00000000-0000-0000-0008-100000000000";
    
    String videoId = "00000000-0000-0000-0020-400000000000";
    List<String> videoInput = new ArrayList<>();
    videoInput.add(videoId);

    controller.deleteVideos(organisationId, videoInput);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void deleteForOtherNotRegisteredDenied() {
    String organisationId = "00000000-0000-0000-0008-100000000000";
    
    String videoId = "00000000-0000-0000-0020-400000000000";
    List<String> videoInput = new ArrayList<>();
    videoInput.add(videoId);

    controller.deleteVideos(organisationId, videoInput);
  }

  @SuppressWarnings("unchecked")
  private void assertContaining(String organisationId, String videoId) {
    List<VideoEntity> result = (List<VideoEntity>) controller
        .readVideos(organisationId).getBody();
    assertThat(result).haveAtLeastOne(
        new Condition<>(p -> p.getId().equals(videoId), "video exists"));
  }

  @SuppressWarnings("unchecked")
  private void assertNotContaining(String organisationId, String videoId) {
    List<VideoEntity> result = (List<VideoEntity>) controller
        .readVideos(organisationId).getBody();
    assertThat(result).noneMatch(p -> p.getId().equals(videoId));
  }
}
