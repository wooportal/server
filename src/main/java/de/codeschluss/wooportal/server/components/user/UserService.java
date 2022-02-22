package de.codeschluss.wooportal.server.components.user;

import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.mail.MailService;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: Auto-generated Javadoc
/**
 * The Class UserService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
@Transactional
public class UserService extends ResourceDataService<UserEntity, UserQueryBuilder> {

  /** The bcrypt password encoder. */
  private final BCryptPasswordEncoder bcryptPasswordEncoder;

  /** The mail service. */
  private final MailService mailService;

  /**
   * Instantiates a new user service.
   *
   * @param repo the repo
   * @param assembler the assembler
   * @param encoder the encoder
   * @param mailService the mail service
   * @param entities the entities
   */
  public UserService(
      UserRepository repo, 
      PagingAndSortingAssembler assembler,
      BCryptPasswordEncoder encoder, 
      MailService mailService,
      UserQueryBuilder entities) {
    super(repo, entities, assembler);
    this.bcryptPasswordEncoder = encoder;
    this.mailService = mailService;
  }

  /**
   * User exists.
   *
   * @param username
   *          the username
   * @return true, if successful
   */
  public boolean userExists(String username) {
    return repo.exists(entities.withUsername(username));
  }

  @Override
  public UserEntity getExisting(UserEntity user) {
    try {
      return getUser(user.getUsername());
    } catch (NotFoundException e) {
      return null;
    }
  }
  
  @Override
  public boolean validCreateFieldConstraints(UserEntity newUser) {
    return validBaseFields(newUser)
        && newUser.getPassword() != null && !newUser.getPassword().isEmpty();
  }
  
  @Override
  public boolean validUpdateFieldConstraints(UserEntity newUser) {
    return validBaseFields(newUser);
  }

  private boolean validBaseFields(UserEntity newUser) {
    return newUser.getUsername() != null && !newUser.getUsername().isEmpty();
  }

  /**
   * Gets the user.
   *
   * @param username
   *          the username
   * @return the user
   */
  public UserEntity getUser(String username) {
    return repo.findOne(entities.withUsername(username))
        .orElseThrow(() -> new NotFoundException(username));
  }

  @Override
  public UserEntity add(UserEntity newUser) {
    newUser.setPassword(bcryptPasswordEncoder.encode(newUser.getPassword()));
    return repo.save(newUser);
  }

  @Override
  public UserEntity update(String id, UserEntity newUser) {
    return repo.findById(id).map(user -> {
      user.setUsername(newUser.getUsername());
      user.setName(newUser.getName());
      user.setPhone(newUser.getPhone());
      
      if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
        user.setPassword(bcryptPasswordEncoder.encode(newUser.getPassword()));
      }
      
      return repo.save(user);
    }).orElseGet(() -> {
      newUser.setId(id);
      return repo.save(newUser);
    });
  }

  /**
   * Grant super user.
   *
   * @param id
   *          the id
   * @param isSuperuser
   *          the is superuser
   */
  public void grantSuperUser(String id, Boolean isSuperuser) {
    UserEntity user = repo.findById(id).orElseThrow(() -> new NotFoundException(id));
    user.setSuperuser(isSuperuser);
    repo.save(user);
  }
  
  public void grantTranslator(String id, Boolean isTranslator) {
    UserEntity user = repo.findById(id).orElseThrow(() -> new NotFoundException(id));
    user.setTranslator(isTranslator);
    repo.save(user);
  }
  
  /**
   * Reset all passwords.
   */
  public void resetAllPasswords() {
    repo.findAll().stream().forEach(user -> 
        resetPasswordAndSendMail(user, "resetallpasswords.ftl"));
  }

  /**
   * Reset password.
   *
   * @param username
   *          the username
   * @return true, if successful
   */
  public boolean resetPassword(String username) {
    return resetPasswordAndSendMail(getUser(username), "resetpassword.ftl");
  }
  
  /**
   * Reset password and send mail.
   *
   * @param user the user
   * @param templateName the template name
   * @return true, if successful
   */
  public boolean resetPasswordAndSendMail(UserEntity user, String templateName) {
    String newPwd = RandomStringUtils.randomAlphanumeric(16);
    user.setPassword(bcryptPasswordEncoder.encode(newPwd));

    if (sendResetPasswordMail(user, newPwd, templateName)) {
      repo.save(user);
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Send reset password mail.
   *
   * @param user the user
   * @param newPassword the new password
   * @param templateName the template name
   * @return true, if successful
   */
  public boolean sendResetPasswordMail(UserEntity user, String newPassword, String templateName) {
    Map<String, Object> model = new HashMap<>();
    model.put("name", user.getName());
    model.put("newPwd", newPassword);
    String subject = "Ihr Passwort wurde zurück gesetzt";

    return mailService.sendEmail(
        subject, 
        templateName, 
        model, 
        user.getUsername());
  }

  /**
   * Convert to resources embedded providers.
   *
   * @param providers the providers
   * @return the resources
   */
  public Resources<?> convertToResourcesEmbeddedProviders(List<ProviderEntity> providers) {
    if (providers == null || providers.isEmpty()) {
      throw new NotFoundException("No member exists");
    }
    
    List<Resource<?>> embeddedUser = providers.stream().map(provider -> {
      Map<String, Object> embedded = new HashMap<>();
      embedded.put("provider", provider);
      return assembler.resourceWithEmbeddable(provider.getUser(), embedded);
    }).collect(Collectors.toList());
    
    return assembler.toListResources(embeddedUser, null);
  }

  /**
   * Gets the resource by provider.
   *
   * @param provider
   *          the provider
   * @return the resource by provider
   */
  public Resource<UserEntity> getResourceByProvider(ProviderEntity provider) {
    return assembler.toResource(provider.getUser());
  }

  /**
   * Gets the super user mail addresses.
   *
   * @return the super user mail addresses
   */
  public List<String> getSuperUserMailAddresses() {
    return getSuperUsers().stream().map(user -> user.getUsername()).collect(Collectors.toList());
  }
  
  public List<String> getAllMailAddresses() {
    return getAll().stream().map(user -> user.getUsername()).collect(Collectors.toList());
  }

  /**
   * Gets the super users.
   *
   * @return the super users
   */
  public List<UserEntity> getSuperUsers() {
    return repo.findAll(entities.asSuperuser());
  }

  /**
   * Gets the mails for providers.
   *
   * @param adminProviders the admin providers
   * @return the mails for providers
   */
  public List<String> getMailsForProviders(List<ProviderEntity> adminProviders) {
    return adminProviders.stream().map(provider -> provider.getUser().getUsername())
        .collect(Collectors.toList());
  }

  /**
   * Gets the mails for organisation.
   *
   * @param orga the orga
   * @return the mails for organisation
   */
  public List<String> getMailsForOrganisation(OrganisationEntity orga) {
    List<UserEntity> orgaUsers = repo.findAll(entities.withOrgaId(orga.getId()));
    
    if (orgaUsers == null) {
      throw new NotFoundException(orga.getId());
    }
    
    return orgaUsers.stream()
        .map(user -> user.getUsername())
        .collect(Collectors.toList());
  }
  
  /**
   * gets the avatar
   *
   * @return avatar
   */
  public ImageEntity getAvatar(String userId) {
    UserEntity result = repo.findOne(entities.withId(userId))
        .orElseThrow(() -> new NotFoundException(userId));

    return result.getAvatar();
  }
  
  /**
   * adds the avatar
   *
   * @return saveavatar
   */
  public UserEntity addAvatar(String userId, ImageEntity avatar)
      throws IOException {
    if (userId == null || userId.isEmpty()) {
      throw new NotFoundException("No User exists");
    }
    UserEntity user = getById(userId);
    user.setAvatar(avatar);
    return repo.save(user);
  }
}
