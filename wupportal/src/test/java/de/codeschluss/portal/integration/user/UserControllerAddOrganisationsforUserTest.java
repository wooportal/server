package de.codeschluss.portal.integration.user;

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

import de.codeschluss.portal.exception.BadParamsException;
import de.codeschluss.portal.exception.DuplicateEntryException;
import de.codeschluss.portal.organisation.OrganisationEntity;
import de.codeschluss.portal.user.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerAddOrganisationsforUserTest {

	@Autowired
    private UserController controller;
	
	@Test
	@WithUserDetails("super@user")
	public void addSingleOrganisationForOtherUserSuperUserOK() {
		String userId = "00000000-0000-0000-0004-300000000000";
		String orgaId = "00000000-0000-0000-0008-200000000000";
		
		Resources<Resource<OrganisationEntity>> result = (Resources<Resource<OrganisationEntity>>) controller.addOrganisationforUser(userId, orgaId).getBody();
		
		assertThat(result.getContent()).haveExactly(1, new Condition<>(
				p -> p.getContent().getId().equals(orgaId), "new organisation with given orga exists"));
	}
	
	@Test
	@WithUserDetails("provider1@user")
	public void addMultipleOrganisationForOwnUserOK() {
		String userId = "00000000-0000-0000-0004-300000000000";
		String orgaId1 = "00000000-0000-0000-0008-300000000000";
		String orgaId2 = "00000000-0000-0000-0008-400000000000";
		String[] requestBody = new String[2];
		requestBody[0] = orgaId1;
		requestBody[1] = orgaId2;
		
		Resources<Resource<OrganisationEntity>> result = (Resources<Resource<OrganisationEntity>>) controller.addOrganisationforUser(userId, requestBody).getBody();
		
		assertThat(result.getContent()).haveExactly(1, new Condition<>(
				p -> p.getContent().getId().equals(orgaId1), "new organisation with given orga1 exists"));
		
		assertThat(result.getContent()).haveExactly(1, new Condition<>(
				p -> p.getContent().getId().equals(orgaId2), "new organisation with given orga2 exists"));
	}
	
	@Test
	@WithUserDetails("provider1@user")
	public void addOrganisationFilterDuplicateOrgasForOwnUserOK() {
		String userId = "00000000-0000-0000-0004-300000000000";
		String orgaId1 = "00000000-0000-0000-0008-500000000000";
		String orgaId2 = "00000000-0000-0000-0008-500000000000";
		String[] requestBody = new String[2];
		requestBody[0] = orgaId1;
		requestBody[1] = orgaId2;
		
		Resources<Resource<OrganisationEntity>> result = (Resources<Resource<OrganisationEntity>>) controller.addOrganisationforUser(userId, requestBody).getBody();
		
		assertThat(result.getContent()).haveExactly(1, new Condition<>(
				p -> p.getContent().getId().equals(orgaId1), "new organisation with given orga1 exists"));
	}
	
	@Test(expected = DuplicateEntryException.class)
	@WithUserDetails("provider1@user")
	public void addDuplicateOrganisationForOwnUserDenied() {
		String userId = "00000000-0000-0000-0004-300000000000";
		String orgaId = "00000000-0000-0000-0008-100000000000";
		
		controller.addOrganisationforUser(userId, orgaId);
	}
	
	@Test(expected = AccessDeniedException.class)
	@WithUserDetails("provider1@user")
	public void addOrganisationForOtherUserDenied() {
		String userId = "00000000-0000-0000-0004-400000000000";
		String orgaId = "00000000-0000-0000-0008-300000000000";
		
		controller.addOrganisationforUser(userId, orgaId);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void addOrganisationForOtherUserNotRegisteredDenied() {
		String userId = "00000000-0000-0000-0004-400000000000";
		String orgaId = "00000000-0000-0000-0008-300000000000";
		
		controller.addOrganisationforUser(userId, orgaId);
	}
	
	@Test(expected = BadParamsException.class)
	@WithUserDetails("super@user")
	public void addOrganisationBadParamsNoUser() {
		String userId = "12345678-0000-0000-0004-XX0000000000";
		String orgaId = "00000000-0000-0000-0008-300000000000";
		
		controller.addOrganisationforUser(userId, orgaId);
	}
	
	@Test(expected = BadParamsException.class)
	@WithUserDetails("super@user")
	public void addOrganisationBadParamsNoOrga() {
		String userId = "00000000-0000-0000-0004-400000000000";
		String orgaId = "12345678-0000-0000-0008-XX0000000000";
		
		controller.addOrganisationforUser(userId, orgaId);
	}
}
