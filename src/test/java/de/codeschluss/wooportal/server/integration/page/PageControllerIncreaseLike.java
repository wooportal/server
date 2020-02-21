package de.codeschluss.wooportal.server.integration.page;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.page.PageController;
import de.codeschluss.wooportal.server.components.page.PageService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageControllerIncreaseLike {
  
  @Autowired
  private PageController controller;
  
  @Autowired
  private SubscriptionService subscriptionService;
  
  @Autowired
  private PageService pageService;

  @Test
  public void increaseLikeOk() {
    String pageId = "00000000-0000-0000-0010-100000000000";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";
    int likesCount = controller.readOne(pageId).getContent().getLikes();
    
    controller.increaseLike(pageId, new StringPrimitive(subscriptionId));
    
    assertThat(controller.readOne(pageId).getContent().getLikes()).isEqualTo(likesCount + 1);
    assertThat(subscriptionService.getById(subscriptionId).getPageLikes())
        .haveAtLeastOne(new Condition<>(
            p -> p.getTitle().equals(pageService.getById(pageId).getTitle()), "page exists"));
  }

  @Test(expected = BadParamsException.class)
  public void increaseLikeNotFound() {
    String pageId = "00000000-0000-0000-0010-XX0000000000";
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    controller.increaseLike(pageId, new StringPrimitive(subscriptionId));
  }

}
