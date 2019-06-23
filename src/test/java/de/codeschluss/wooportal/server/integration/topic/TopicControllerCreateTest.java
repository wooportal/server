package de.codeschluss.wooportal.server.integration.topic;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import de.codeschluss.wooportal.server.components.topic.TopicController;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.DuplicateEntryException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicControllerCreateTest {

  @Autowired
  private TopicController controller;

  @Test
  @WithUserDetails("super@user")
  @SuppressWarnings("unchecked")
  public void createSuperUserOk() throws Exception {
    TopicEntity topic = newTopic("createSuperUserOk");

    controller.create(topic);

    Resources<Resource<TopicEntity>> result = (Resources<Resource<TopicEntity>>) controller
        .readAll(new FilterSortPaginate()).getBody();
    assertThat(result.getContent()).haveAtLeastOne(new Condition<>(
        p -> p.getContent().getName().equals(topic.getName()), "topic exists"));
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void createNotValidNameDenied() throws Exception {
    TopicEntity topic = newTopic(null);

    controller.create(topic);
  }

  @Test(expected = DuplicateEntryException.class)
  @WithUserDetails("super@user")
  public void createSuperUserDuplicated() throws Exception {
    TopicEntity topic = newTopic("topic1");

    controller.create(topic);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("provider1@user")
  public void createProviderDenied() throws Exception {
    TopicEntity topic = newTopic("createProviderDenied");

    controller.create(topic);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void createNoUserDenied() throws Exception {
    TopicEntity topic = newTopic("createNoUserDenied");

    controller.create(topic);
  }

  private TopicEntity newTopic(String name) {
    TopicEntity topic = new TopicEntity();
    topic.setName(name);
    return topic;
  }
}
