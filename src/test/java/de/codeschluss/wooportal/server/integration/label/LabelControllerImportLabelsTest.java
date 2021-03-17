package de.codeschluss.wooportal.server.integration.label;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resources;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import de.codeschluss.wooportal.server.components.label.LabelController;
import de.codeschluss.wooportal.server.components.label.LabelEntity;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.integration.helper.TestConfiguration;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class LabelControllerImportLabelsTest {

  @Autowired
  private MockMvc mvc;
  
  @Autowired
  private TestConfiguration testConfig;
  
  @Autowired
  private LabelController controller;

  @Test
  @WithUserDetails("super@user")
  @SuppressWarnings("unchecked")
  public void importSuperUserOk() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
        "text/plain", getXliff());
    
    mvc.perform(multipart("/labels/import").file(multipartFile));
    
    Resources<List<LabelEntity>> result = (Resources<List<LabelEntity>>) controller.readAll(new FilterSortPaginate()).getBody();
    assertThat(result.getContent()).isNotEmpty();
  }

  private byte[] getXliff() throws IOException {
    return Files.readAllBytes(Paths.get(testConfig.getXliffPath()));
  }
  
}
