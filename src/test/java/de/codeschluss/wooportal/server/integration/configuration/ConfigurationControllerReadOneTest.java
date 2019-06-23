package de.codeschluss.wooportal.server.integration.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.core.config.ConfigurationController;
import de.codeschluss.wooportal.server.core.config.ConfigurationEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationControllerReadOneTest {

  @Autowired
  private ConfigurationController controller;

  @Test
  public void findOneOk() {
    String configurationId = "00000000-0000-0000-0001-000000000008";

    Resource<ConfigurationEntity> result = (Resource<ConfigurationEntity>) controller
        .readOne(configurationId);

    assertThat(result.getContent()).isNotNull();
  }

  @Test(expected = NotFoundException.class)
  public void findConfigurationNotFound() {
    String configurationId = "00000000-0000-0000-0001-XX0000000000";

    controller.readOne(configurationId);
  }
}
