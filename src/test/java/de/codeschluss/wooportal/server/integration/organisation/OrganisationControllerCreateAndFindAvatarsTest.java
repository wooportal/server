package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.integration.helper.ImageReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganisationControllerCreateAndFindAvatarsTest {

  @Autowired
  private OrganisationController controller;

  @Autowired
  private ImageReader avatarReader;

  @Test
  @WithUserDetails("super@user")
  public void addAndFindAvatarSuperUserOk() throws IOException {
    ImageEntity avatarInput =
        newImageEntity(avatarReader.getBase64Picture(), avatarReader.getMimeType());

    controller.addAvatar("00000000-0000-0000-0008-100000000000", avatarInput);
    ImageEntity result = controller.readAvatar("00000000-0000-0000-0008-100000000000").getBody();

    assertThat(result).isNotNull();
  }

  @Test
  @WithUserDetails("admin@user")
  public void addAndFindAvatarAdminUserOk() throws IOException {
    ImageEntity avatarInput =
        newImageEntity(avatarReader.getBase64Picture(), avatarReader.getMimeType());

    controller.addAvatar("00000000-0000-0000-0008-100000000000", avatarInput);
    ImageEntity result = controller.readAvatar("00000000-0000-0000-0008-100000000000").getBody();

    assertThat(result).isNotNull();
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("admin@user")
  public void addNotValidAvatarDenied() throws IOException {
    ImageEntity avatarInput = newImageEntity(null, avatarReader.getMimeType());

    controller.addAvatar("00000000-0000-0000-0008-100000000000", avatarInput);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("admin@user")
  public void addNullMimeTypeDenied() throws IOException {
    ImageEntity avatarInput = newImageEntity(avatarReader.getBase64Picture(), null);

    controller.addAvatar("00000000-0000-0000-0008-100000000000", avatarInput);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("admin@user")
  public void addNotValidMimeTypeDenied() throws IOException {
    ImageEntity avatarInput = newImageEntity(avatarReader.getBase64Picture(), "notvalid");

    controller.addAvatar("00000000-0000-0000-0008-100000000000", avatarInput);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void addAndFindImagesOtherOrgaDenied() throws IOException {
    ImageEntity avatarInput =
        newImageEntity(avatarReader.getBase64Picture(), avatarReader.getMimeType());

    controller.addAvatar("00000000-0000-0000-0008-100000000000", avatarInput);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void addAndFindImagesNotRegisteredDenied() throws IOException {
    ImageEntity avatarInput =
        newImageEntity(avatarReader.getBase64Picture(), avatarReader.getMimeType());

    controller.addAvatar("00000000-0000-0000-0008-100000000000", avatarInput);
  }

  @Test(expected = NotFoundException.class)
  public void findImagesByOrganisationNotFound() {
    ImageEntity result = controller.readAvatar("00000000-0000-0000-0008-XX0000000000").getBody();

    assertThat(result).isNotNull();
  }

  private ImageEntity newImageEntity(String data, String mimeType) {
    ImageEntity image = new ImageEntity();
    image.setCaption("test");
    image.setImageData(data);
    image.setMimeType(mimeType);
    return image;
  }
}