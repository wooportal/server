package de.codeschluss.wooportal.server.integration.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.provider.ProviderService;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.integration.helper.SmtpServerRule;
import org.assertj.core.api.Condition;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrganisationControllerCreateTest {

  @Autowired
  private OrganisationController controller;

  @Autowired
  private OrganisationService service;

  @Autowired
  private ProviderService providerService;
  
  @Rule
  public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

  @Test
  @WithUserDetails("super@user")
  public void createSuperUserOk() throws Exception {
    OrganisationEntity organisation = newOrganisation(true, "createSuperUserOk",
        "create@SuperUserOk", "createSuperUserOk", "123456789", "createSuperUserOk",
        "createSuperUserOk", "00000000-0000-0000-0006-100000000000");

    controller.create(organisation);

    assertThat(service.existsByName(organisation.getName())).isTrue();
    
    assertThat(smtpServerRule.getMessages()).isNotEmpty();
  }

  @Test
  @WithUserDetails("createorga@user")
  @SuppressWarnings("unchecked")
  public void createCreateOrgaOk() throws Exception {
    OrganisationEntity organisation = newOrganisation(false, "createCreateOrgaOk",
        "createCreateOrgaOk@createCreateOrgaOk", "createCreateOrgaOk", "123456789",
        "createCreateOrgaOk", "createCreateOrgaOk", "00000000-0000-0000-0006-100000000000");

    OrganisationEntity savedOrga = ((Resource<OrganisationEntity>) controller.create(organisation)
        .getBody()).getContent();

    assertThat(service.existsByName(savedOrga.getName())).isTrue();
    assertThat(service.getById(savedOrga.getId()).isApproved()).isFalse();

    assertThat(providerService.getProvidersByOrganisation(savedOrga.getId())).haveAtLeastOne(
        new Condition<>(p -> p.getUser().getUsername().equals("createorga@user"), "user exists"));
    
    assertThat(smtpServerRule.getMessages()).isNotEmpty();
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void createNotValidNameOk() throws Exception {
    OrganisationEntity organisation = newOrganisation(true, "createNotValidOk", "create@NotValidOk",
        null, "123456789", "createNotValidOk", "createSuperNotValidOk",
        "00000000-0000-0000-0006-100000000000");

    controller.create(organisation);
  }

  @Test(expected = BadParamsException.class)
  @WithUserDetails("super@user")
  public void createNotValidAddressOk() throws Exception {
    OrganisationEntity organisation = newOrganisation(true, "createNotValidAddressOk",
        "create@NotValidAddressOk", "createNotValidAddressOk", "123456789",
        "createNotValidAddressOk", "createNotValidAddressOk", null);

    controller.create(organisation);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void createNoUserDenied() throws Exception {
    OrganisationEntity organisation = newOrganisation(true, "createNoUserDenied",
        "createNoUserDenied", "createNoUserDenied", "123456789", "createNoUserDenied",
        "createNoUserDenied", "00000000-0000-0000-0006-100000000000");

    controller.create(organisation);
  }

  private OrganisationEntity newOrganisation(boolean approved, String description, String mail,
      String name, String phone, String videoUrl, String website, String addressId) {
    OrganisationEntity organisation = new OrganisationEntity();
    organisation.setApproved(approved);
    organisation.setDescription(description); 
    organisation.setMail(mail);
    organisation.setName(name);
    organisation.setPhone(phone);
    organisation.setVideoUrl(videoUrl);
    organisation.setWebsite(website);
    organisation.setAddressId(addressId);

    return organisation;
  }

}
