package de.codeschluss.wooportal.server.components.organisation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.components.address.AddressEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.user.UserEntity;
import de.codeschluss.wooportal.server.components.user.UserService;
import de.codeschluss.wooportal.server.components.video.VideoEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.mail.MailService;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;

// TODO: Auto-generated Javadoc
/**
 * The Class OrganisationService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
@Transactional
public class OrganisationService
    extends ResourceDataService<OrganisationEntity, OrganisationQueryBuilder> {

  /** The mail service. */
  private final MailService mailService;

  /** The user service. */
  private final UserService userService;

  /** The user service. */


  /**
   * Instantiates a new organisation service.
   *
   * @param repo the repo
   * @param entities the entities
   * @param assembler the assembler
   * @param mailService the mail service
   */
  public OrganisationService(OrganisationRepository repo, OrganisationQueryBuilder entities,
      PagingAndSortingAssembler assembler, MailService mailService, UserService userService) {
    super(repo, entities, assembler);
    this.mailService = mailService;
    this.userService = userService;

  }

  /**
   * Exists by name.
   *
   * @param name the name
   * @return true, if successful
   */
  public boolean existsByName(String name) {
    return repo.exists(entities.withName(name));
  }

  @Override
  public OrganisationEntity getExisting(OrganisationEntity orga) {
    return repo.findOne(entities.withName(orga.getName())).orElse(null);
  }

  @Override
  public boolean validCreateFieldConstraints(OrganisationEntity newOrga) {
    return validFields(newOrga) && newOrga.getAddressId() != null
        && !newOrga.getAddressId().isEmpty();
  }

  @Override
  public boolean validUpdateFieldConstraints(OrganisationEntity newOrga) {
    return validFields(newOrga);
  }

  /**
   * Valid fields.
   *
   * @param newOrga the new orga
   * @return true, if successful
   */
  private boolean validFields(OrganisationEntity newOrga) {
    return newOrga.getName() != null && !newOrga.getName().isEmpty();
  }

  @Override
  public OrganisationEntity update(String id, OrganisationEntity newOrga) {
    return repo.findById(id).map(orga -> {
      orga.setDescription(newOrga.getDescription());
      orga.setMail(newOrga.getMail());
      orga.setName(newOrga.getName());
      orga.setPhone(newOrga.getPhone());
      orga.setWebsite(newOrga.getWebsite());
      return repo.save(orga);
    }).orElseGet(() -> {
      newOrga.setId(id);
      return repo.save(newOrga);
    });
  }

  /**
   * Update address.
   *
   * @param organisationId the organisation id
   * @param address the address
   * @return the organisation entity
   */
  public OrganisationEntity updateAddress(String organisationId, AddressEntity address) {
    OrganisationEntity orga = getById(organisationId);
    orga.setAddress(address);
    return repo.save(orga);
  }

  /**
   * Gets the by providers.
   *
   * @param providers the providers
   * @param params the params
   * @return the by providers
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Resources<?> getByProviders(List<ProviderEntity> providers, BaseParams params)
      throws JsonParseException, JsonMappingException, IOException {
    Predicate query = entities.withAnyOfProviders(providers);
    List<OrganisationEntity> result =
        params == null ? repo.findAll(query) : repo.findAll(query, entities.createSort(params));

    return assembler.entitiesToResources(result, params);
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

    List<Resource<?>> embeddedOrgas = providers.stream().map(provider -> {
      Map<String, Object> embedded = new HashMap<>();
      embedded.put("provider", provider);
      return assembler.resourceWithEmbeddable(provider.getOrganisation(), embedded);
    }).collect(Collectors.toList());

    return assembler.toListResources(embeddedOrgas, null);
  }

  /**
   * Gets the orga activity.
   *
   * @param activityId the activity id
   * @return the orga activity
   */
  public OrganisationEntity getOrgaActivity(String activityId) {
    return repo.findOne(entities.forActivity(activityId))
        .orElseThrow(() -> new NotFoundException(activityId));
  }

  /**
   * Sets the approval.
   *
   * @param organisationId the organisation id
   * @param isApproved the is approved
   */
  public void setApproval(String organisationId, Boolean isApproved) {
    OrganisationEntity organisation = getById(organisationId);
    organisation.setApproved(isApproved);
    if (isApproved) {
      this.sendApprovalMail(organisation);
    }
    repo.save(organisation);
  }

  private boolean sendApprovalMail(OrganisationEntity orga) {
    Map<String, Object> model = new HashMap<>();
    model.put("name", orga.getName());
    String subject = "Organisation bestätigt";

    return mailService.sendEmail(subject, "organisationapproved.ftl", model,
        userService.getMailsForOrganisation(orga).toArray(new String[0]));
  }

  /**
   * Gets the resource by provider.
   *
   * @param provider the provider
   * @return the resource by provider
   */
  public Resource<?> getResourceByProvider(ProviderEntity provider) {
    OrganisationEntity orga = repo.findOne(entities.withProvider(provider))
        .orElseThrow(() -> new NotFoundException(provider.getId()));
    return assembler.toResource(orga);
  }

  public Resources<?> getResourcesByAddress(String addressId)
      throws JsonParseException, JsonMappingException, IOException {
    return assembler.entitiesToResources(repo.findAll(entities.withAddress(addressId)), null);
  }

  /**
   * Gets the for admin provider.
   *
   * @param user the user
   * @return the for admin provider
   */
  public List<OrganisationEntity> getForAdminProvider(UserEntity user) {
    return repo.findAll(entities.forOrgaAdmin(user.getId()));
  }

  /**
   * Increase like.
   *
   * @param organisationId the activity id
   */
  public void increaseLike(String organisationId) {
    OrganisationEntity organisation = getById(organisationId);
    organisation.setLikes(organisation.getLikes() + 1);
    repo.save(organisation);
  }

  /**
   * Adds the images.
   *
   * @param id the id
   * @param images the images
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<ImageEntity> addImages(String id, List<ImageEntity> images) throws IOException {
    OrganisationEntity savedEntity = null;
    for (ImageEntity image : images) {
      savedEntity = addImage(id, image);
    }
    return savedEntity.getImages();
  }

  /**
   * Adds the image.
   *
   * @param id the id
   * @param image the image
   * @return the organisation entity
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public OrganisationEntity addImage(String id, ImageEntity image) throws IOException {
    OrganisationEntity organisation = getById(id);
    organisation.getImages().add(image);
    return repo.save(organisation);
  }

  /**
   * Gets the images.
   *
   * @param id the id
   * @return the images
   */
  public List<ImageEntity> getImages(String id) {
    List<ImageEntity> result = getById(id).getImages();
    if (result == null || result.isEmpty()) {
      throw new NotFoundException("No images found");
    }
    return result;
  }

  /**
   * Gets the all mail addresses.
   *
   * @return the all mail addresses
   */
  public List<String> getAllMailAddresses() {
    return repo.findAll(entities.withMail()).stream().map(o -> o.getMail())
        .collect(Collectors.toList());
  }

  public ImageEntity getAvatar(String organisationId) {
    var result = repo.findOne(entities.withId(organisationId))
        .orElseThrow(() -> new NotFoundException(organisationId));

    if (result.getAvatar() == null) {
      throw new NotFoundException("avatar");
    }

    return result.getAvatar();
  }

  public OrganisationEntity addAvatar(String organisationId, ImageEntity avatar)
      throws IOException {
    if (organisationId == null || organisationId.isEmpty()) {
      throw new NotFoundException("No Organisation exists");
    }
    OrganisationEntity organisation = getById(organisationId);
    organisation.setAvatar(avatar);
    return repo.save(organisation);
  }

  public Resources<?> getVideos(String id) {
    List<VideoEntity> result = getById(id).getVideos();
    if (result == null || result.isEmpty()) {
      throw new NotFoundException("No Videos found");
    }
    List<Resource<?>> embeddedVideos = result.stream().map(video -> {
      ImageEntity thumbnail = video.getThumbnail();
      if (thumbnail != null) {
        Map<String, Object> embedded = new HashMap<>();
        embedded.put("thumbnail", thumbnail);
        return assembler.resourceWithEmbeddable(video, embedded);
      }
      return video.toResource();
    }).collect(Collectors.toList());

    return assembler.toListResources(embeddedVideos, null);
  }

  public List<VideoEntity> addVideos(String id, List<VideoEntity> videos) throws Exception {
    OrganisationEntity savedEntity = null;
    for (VideoEntity video : videos) {
      savedEntity = addVideo(id, video);
    }
    return savedEntity.getVideos();
  }

  public OrganisationEntity addVideo(String id, VideoEntity video) throws Exception {
    OrganisationEntity organisation = getById(id);
    organisation.getVideos().add(video);
    return repo.save(organisation);
  }
}
