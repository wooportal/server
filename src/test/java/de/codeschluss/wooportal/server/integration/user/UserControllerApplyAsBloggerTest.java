package de.codeschluss.wooportal.server.integration.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import de.codeschluss.wooportal.server.components.user.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserControllerApplyAsBloggerTest {
  
  @Autowired
  private UserController controller;
  
  @Test
  @WithUserDetails("bloggerApply@user")
  public void applyAsBloggerOk() {
    controller.applyAsBlogger();
    
  }

  @Test(expected = AuthenticationException.class)
  public void findBlogsNotFound() {
    controller.applyAsBlogger();
  }

}
