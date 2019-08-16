package de.codeschluss.wooportal.server.integration.targetgroup;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TargetGroupControllerReadAllTest {

  @Autowired
  private TargetGroupController controller;

  private FilterSortPaginate params = new FilterSortPaginate("target", 0, 5, "name", "asc", null);

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
    FilterSortPaginate params = new FilterSortPaginate("target", 1, 5, "blablabla123", "wrong",
        null);
    controller.readAll(params);
  }
}