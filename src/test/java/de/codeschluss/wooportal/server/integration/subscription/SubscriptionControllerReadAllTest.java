package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionControllerReadAllTest {

  @Autowired
  private SubscriptionController controller;

  private FilterSortPaginate params =
      new FilterSortPaginate("authSecret1", 0, 5, "authSecret", "asc", null);

  @Test
  @WithUserDetails("super@user")
  public void findAllWithoutPaginationOk() {
    FilterSortPaginate params = new FilterSortPaginate(null, null, null, "authSecret", "asc", null);

    Resources<?> result = (Resources<?>) controller.readAll(params).getBody();

    assertThat(result.getContent()).isNotEmpty();
  }

  @Test
  @WithUserDetails("super@user")
  public void findAllEmptyParamsOk() {
    FilterSortPaginate params = new FilterSortPaginate(null, null, null, null, null, null);

    Resources<?> result = (Resources<?>) controller.readAll(params).getBody();

    assertThat(result.getContent()).isNotEmpty();
  }

  @Test
  @WithUserDetails("super@user")
  public void findAllWithPaginationOk() {
    PagedResources<?> result = (PagedResources<?>) controller.readAll(params).getBody();
    assertThat(result.getContent()).isNotEmpty();
  }

  @Test(expected = PropertyReferenceException.class)
  @WithUserDetails("super@user")
  public void findAllWrongParams() {
    FilterSortPaginate params =
        new FilterSortPaginate("authSecret1", 1, 5, "blablabla123", "wrong", null);
    controller.readAll(params);
  }
  
  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void findAllDenied() {
    controller.readAll(params);
  }
}
