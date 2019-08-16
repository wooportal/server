package de.codeschluss.wooportal.server.integration.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.category.CategoryController;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
public class CategoryControllerDeleteTest {

  @Autowired
  private CategoryController controller;

  @Test(expected = NotFoundException.class)
  @WithUserDetails("super@user")
  public void deleteSuperUserOk() throws URISyntaxException {
    String categoryId = "00000000-0000-0000-0007-400000000000";
    assertThat(controller.readOne(categoryId)).isNotNull();

    controller.delete(categoryId);

    controller.readOne(categoryId);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void deleteProviderUserDenied() throws URISyntaxException {
    String categoryId = "00000000-0000-0000-0007-100000000000";

    controller.delete(categoryId);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void deleteOtherNotRegisteredDenied() {
    String categoryId = "00000000-0000-0000-0007-100000000000";

    controller.delete(categoryId);
  }

}