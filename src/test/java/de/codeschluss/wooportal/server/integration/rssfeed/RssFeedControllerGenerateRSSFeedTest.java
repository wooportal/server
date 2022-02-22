package de.codeschluss.wooportal.server.integration.rssfeed;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.View;
import de.codeschluss.wooportal.server.components.rssfeed.RssFeedController;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RssFeedControllerGenerateRSSFeedTest {


  @Autowired
  private RssFeedController controller;
  @Autowired

  @Test
  public void RssFeedNotNull() throws IOException {


    View rssfeed = controller.getFeed();
    assertThat(rssfeed).isNotNull();
  }
}

