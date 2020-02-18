package de.codeschluss.wooportal.server.integration.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionController;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class SubscriptionControllerDeleteBloggerSubscriptionsTest {

  @Autowired
  private SubscriptionController controller;

  @Test
  @WithUserDetails("super@user")
  public void deleteBloggerSubscriptions() throws URISyntaxException {
    List<String> bloggerId = new ArrayList<>();
    bloggerId.add("00000000-0000-0000-0015-200000000000");
    String subscriptionId = "00000000-0000-0000-0020-100000000000";

    assertContaining(subscriptionId, bloggerId);

    controller.deleteBloggerSubscriptions(subscriptionId, bloggerId);

    assertNotContaining(subscriptionId, bloggerId);
  }
  
  @Test(expected = BadParamsException.class)
  public void deleteBloggerSubscriptionsNotFound() throws URISyntaxException {
    List<String> bloggerId = new ArrayList<>();
    bloggerId.add("00000000-0000-0000-0015-100000000000");
    String subscriptionId = "00000000-0000-0000-0020-xx0000000000";

    controller.deleteBloggerSubscriptions(subscriptionId, bloggerId);
  }

  private void assertContaining(String subscriptionId, List<String> bloggerIds) {
    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>)
        controller.readOne(subscriptionId);
    assertThat(result.getContent().getBloggerSubscriptions()).haveAtLeastOne(
        new Condition<>(t -> bloggerIds.contains(t.getId()), "blogger exists"));
  }

  private void assertNotContaining(String subscriptionId, List<String> bloggerIds) {
    Resource<SubscriptionEntity> result = (Resource<SubscriptionEntity>)
        controller.readOne(subscriptionId);
    assertThat(result.getContent().getBloggerSubscriptions())
        .noneMatch(t -> bloggerIds.contains(t.getId()));
  }
}
