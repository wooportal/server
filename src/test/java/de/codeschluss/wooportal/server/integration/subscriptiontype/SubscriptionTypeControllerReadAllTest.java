package de.codeschluss.wooportal.server.integration.subscriptiontype;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.push.subscriptiontype.SubscriptionTypeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionTypeControllerReadAllTest {

  @Autowired
  private SubscriptionTypeController controller;

  private FilterSortPaginate params = 
      new FilterSortPaginate("subscriptionType1", 0, 5, "name", "asc", null);

  @Test
  public void findAllWithoutPaginationOk() {
    FilterSortPaginate params = new FilterSortPaginate(null, null, null, "name", "asc", null);

    Resources<?> result = (Resources<?>) controller.readAll(params).getBody();

    assertThat(result.getContent()).isNotEmpty();
  }

  @Test
  public void findAllEmptyParamsOk() {
    FilterSortPaginate params = new FilterSortPaginate(null, null, null, null, null, null);

    Resources<?> result = (Resources<?>) controller.readAll(params).getBody();

    assertThat(result.getContent()).isNotEmpty();
  }

  @Test
  public void findAllWithPaginationOk() {
    PagedResources<?> result = (PagedResources<?>) controller.readAll(params).getBody();
    assertThat(result.getContent()).isNotEmpty();
  }

  @Test(expected = PropertyReferenceException.class)
  public void findAllWrongParams() {
    FilterSortPaginate params = 
        new FilterSortPaginate("subscriptionType1", 1, 5, "blablabla123", "wrong", null);
    controller.readAll(params);
  }
}
