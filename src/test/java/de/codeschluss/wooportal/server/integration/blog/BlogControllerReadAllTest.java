package de.codeschluss.wooportal.server.integration.blog;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.codeschluss.wooportal.server.components.blog.BlogController;
import de.codeschluss.wooportal.server.core.api.dto.EmbeddedGraph;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogControllerReadAllTest {

  @Autowired
  private BlogController controller;

  private FilterSortPaginate params = new FilterSortPaginate("blog", 0, 5, "title", "asc", null);

  @Test
  public void findAllWithoutPaginationOk() {
    FilterSortPaginate params = new FilterSortPaginate(null, null, null, "title", "asc", null);

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
  
  @Test
  public void readWithEmbeddingsOk() throws JsonProcessingException {
    String bas64Embeddings = createBase64Embeddings();
    
    FilterSortPaginate params = new FilterSortPaginate(
        null, null, null, null, null, bas64Embeddings);
    
    Resources<?> result = (Resources<?>) controller.readAll(params).getBody();
    
    assertThat(result.getContent()).isNotEmpty();
  }

  private String createBase64Embeddings() throws JsonProcessingException {
    EmbeddedGraph images = new EmbeddedGraph();
    images.setName("images");
    
    List<EmbeddedGraph> blogEmbeddings = new ArrayList<>();
    blogEmbeddings.add(images);
    
    ObjectMapper mapper = new ObjectMapper();
    return Base64Utils.encodeToString(mapper.writeValueAsString(blogEmbeddings).getBytes());
  }

  @Test(expected = PropertyReferenceException.class)
  public void findAllWrongParams() {
    FilterSortPaginate params = new FilterSortPaginate("blog", 1, 5, "blablabla123", "wrong",
        null);
    controller.readAll(params);
  }
}
