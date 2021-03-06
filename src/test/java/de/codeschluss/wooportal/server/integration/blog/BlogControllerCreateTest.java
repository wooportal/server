package de.codeschluss.wooportal.server.integration.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;

import de.codeschluss.wooportal.server.components.blog.BlogController;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.push.PushService;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogControllerCreateTest {

  @Autowired
  private BlogController controller;
  
  @MockBean
  private PushService pushService;
  
  @Before
  public void setUp() {
    willReturn(null).given(
        pushService).pushNewActivity(Mockito.any());
  }

  @Test
  @WithUserDetails("blog1@user")
  @SuppressWarnings("unchecked")
  public void createBloggerOk() throws Exception {
    BlogEntity blog = newBlog("createBloggerOk", "createBloggerOk");

    controller.create(blog);

    Resources<Resource<BlogEntity>> result = (Resources<Resource<BlogEntity>>) controller
        .readAll(new FilterSortPaginate()).getBody();
    assertThat(result.getContent()).haveAtLeastOne(
        new Condition<>(p -> p.getContent().getTitle().equals(blog.getTitle()), "blog exists"));
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("blog1@user")
  public void createNotValidTitleDenied() throws Exception {
    BlogEntity blog = newBlog(null, "createNotValidDenied");

    controller.create(blog);
  }
  
  @Test(expected = BadParamsException.class)
  @WithUserDetails("blog1@user")
  public void createNotValidContentDenied() throws Exception {
    BlogEntity blog = newBlog("createNotValidDenied", null);

    controller.create(blog);
  }

  @Test(expected = AccessDeniedException.class)
  @WithUserDetails("super@user")
  public void createSuperUserDenied() throws Exception {
    BlogEntity blog = newBlog("createSuperUserDenied", "createSuperUserDenied");

    controller.create(blog);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void createNoUserDenied() throws Exception {
    BlogEntity blog = newBlog("createNoUserDenied", "createNoUserDenied");

    controller.create(blog);
  }
  
  private BlogEntity newBlog(String title, String content) {
    BlogEntity blog = new BlogEntity();
    blog.setTitle(title);
    blog.setContent(content);
    return blog;
  }
}
