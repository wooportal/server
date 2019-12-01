package de.codeschluss.wooportal.server.integration.user;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.user.UserController;
import de.codeschluss.wooportal.server.integration.helper.SmtpServerRule;
import javax.mail.MessagingException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserControllerApplyAsBloggerTest {
  
  @Rule
  public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);
  
  @Autowired
  private UserController controller;
  
  @Test
  @WithUserDetails("bloggerApply@user")
  public void applyAsBloggerOk() {
    controller.applyAsBlogger();
    assertThat(smtpServerRule.getMessages()).anyMatch(message -> {
      try {
        return message.getSubject().contains("Neue Bloggeranfrage");
      } catch (MessagingException e) {
        e.printStackTrace();
      }
      return false;
    });
  }

  @Test(expected = AuthenticationException.class)
  public void findBlogsNotFound() {
    controller.applyAsBlogger();
  }

}
