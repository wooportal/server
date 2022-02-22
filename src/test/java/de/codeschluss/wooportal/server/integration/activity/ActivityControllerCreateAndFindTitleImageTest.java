package de.codeschluss.wooportal.server.integration.activity;

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
import de.codeschluss.wooportal.server.components.activity.ActivityController;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.integration.helper.ImageReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityControllerCreateAndFindTitleImageTest {

  @Autowired
  private ActivityController controller;

  @Autowired
  private ImageReader titleImageReader;

  @Test
  @WithUserDetails("super@user")
  public void addAndFindTitleImageSuperUserOk() throws IOException {
    ImageEntity titleImageInput =
        newImageEntity(titleImageReader.getBase64Picture(), titleImageReader.getMimeType());

    controller.addTitleImage("00000000-0000-0000-0010-100000000000", titleImageInput);
    ImageEntity result = controller.readTitleImage("00000000-0000-0000-0010-100000000000").getBody();

    assertThat(result).isNotNull();
  }

  @Test
  @WithUserDetails("super@user")
  public void addAndFindTitleImageAdminUserOk() throws IOException {
    ImageEntity titleImageInput =
        newImageEntity(titleImageReader.getBase64Picture(), titleImageReader.getMimeType());

    controller.addTitleImage("00000000-0000-0000-0010-100000000000", titleImageInput);
    ImageEntity result = controller.readTitleImage("00000000-0000-0000-0010-100000000000").getBody();

    assertThat(result).isNotNull();
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void addNotValidTitleImageDenied() throws IOException {
    ImageEntity titleImageInput = newImageEntity(null, titleImageReader.getMimeType());

    controller.addTitleImage("00000000-0000-0000-0010-100000000000", titleImageInput);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void addNullMimeTypeDenied() throws IOException {
    ImageEntity titleImageInput = newImageEntity(titleImageReader.getBase64Picture(), null);

    controller.addTitleImage("00000000-0000-0000-0010-100000000000", titleImageInput);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void addNotValidMimeTypeDenied() throws IOException {
    ImageEntity titleImageInput = newImageEntity(titleImageReader.getBase64Picture(), "notvalid");

    controller.addTitleImage("00000000-0000-0000-0010-100000000000", titleImageInput);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void addAndFindImagesOtherOrgaDenied() throws IOException {
    ImageEntity titleImageInput =
        newImageEntity(titleImageReader.getBase64Picture(), titleImageReader.getMimeType());

    controller.addTitleImage("00000000-0000-0000-0010-100000000000", titleImageInput);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void addAndFindImagesNotRegisteredDenied() throws IOException {
    ImageEntity titleImageInput =
        newImageEntity(titleImageReader.getBase64Picture(), titleImageReader.getMimeType());

    controller.addTitleImage("00000000-0000-0000-0010-100000000000", titleImageInput);
  }

  @Test(expected = NotFoundException.class)
  public void findImagesUserNotFound() {
    controller.readTitleImage("00000000-0000-0000-0008-XX0000000000").getBody();

  }

  private ImageEntity newImageEntity(String data, String mimeType) {
    ImageEntity image = new ImageEntity();
    image.setCaption("test");
    image.setImageData(data);
    image.setMimeType(mimeType);
    return image;
  }
}