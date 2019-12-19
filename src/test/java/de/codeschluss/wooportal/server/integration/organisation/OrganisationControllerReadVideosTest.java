package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.components.video.VideoEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganisationControllerReadVideosTest {

  @Autowired
  private OrganisationController controller;

  @SuppressWarnings("unchecked")
  @Test
  public void findVideosByOrganisationOk() {
    String organisationId = "00000000-0000-0000-0008-100000000000";
    List<VideoEntity> result = (List<VideoEntity>) controller
        .readVideos(organisationId).getBody();

    assertThat(result).isNotEmpty();
  }

  @SuppressWarnings("unchecked")
  @Test(expected = NotFoundException.class)
  public void findVideosByOrganisationNotFound() {
    String organisationId = "00000000-0000-0000-0008-XX0000000000";
    List<VideoEntity> result = (List<VideoEntity>) controller
        .readVideos(organisationId).getBody();

    assertThat(result).isNotEmpty();
  }
}
