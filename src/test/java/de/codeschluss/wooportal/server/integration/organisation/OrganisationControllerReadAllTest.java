package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.components.organisation.OrganisationQueryParam;
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
public class OrganisationControllerReadAllTest {

  @Autowired
  private OrganisationController controller;

  private OrganisationQueryParam params = new OrganisationQueryParam("organisation", 1, 5, "name",
      "asc", null, null);

  @Test
  public void findAllWithoutPaginationOk() {
    OrganisationQueryParam params = new OrganisationQueryParam(null, null, null, "name", "asc",
        null, null);

    Resources<?> result = (Resources<?>) controller.readAll(params).getBody();

    assertThat(result.getContent()).isNotEmpty();
  }

  @Test
  public void findAllEmptyParamsOk() {
    OrganisationQueryParam params = new OrganisationQueryParam(null, null, null, null, null, null,
        null);

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
    OrganisationQueryParam params = new OrganisationQueryParam(
        null, null, null, null, null, bas64Embeddings, null);
    
    Resources<?> result = (Resources<?>) controller.readAll(params).getBody();
    
    assertThat(result.getContent()).isNotEmpty();
  }

  private String createBase64Embeddings() throws JsonProcessingException {
    EmbeddedGraph images = new EmbeddedGraph();
    images.setName("images");
    
    List<EmbeddedGraph> orgaEmbeddings = new ArrayList<>();
    orgaEmbeddings.add(images);
    
    ObjectMapper mapper = new ObjectMapper();
    return Base64Utils.encodeToString(mapper.writeValueAsString(orgaEmbeddings).getBytes());
  }

  @Test(expected = PropertyReferenceException.class)
  public void findAllWrongParams() {
    OrganisationQueryParam params = new OrganisationQueryParam("organisation", 1, 5, "blablabla123",
        "wrong", null, null);
    controller.readAll(params);
  }
}
