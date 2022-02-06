package de.codeschluss.wooportal.server.components.activity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.components.address.AddressEntity;
import de.codeschluss.wooportal.server.components.category.CategoryEntity;
import de.codeschluss.wooportal.server.components.category.CategoryService;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.components.tag.TagEntity;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupEntity;
import de.codeschluss.wooportal.server.components.user.UserEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
import de.codeschluss.wooportal.server.core.api.dto.BooleanPrimitive;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.mail.MailConfiguration;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Geo;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;




// TODO: Auto-generated Javadoc
/**
 * The Class ActivityService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class ActivityService extends ResourceDataService<ActivityEntity, ActivityQueryBuilder> {

  private MailConfiguration mailConfig;
  
  private CategoryService categoryService;
  
  /**
   * Instantiates a new activity service.
   *
   * @param repo the repo
   * @param entities the entities
   * @param assembler the assembler
   */
  public ActivityService(
      ActivityRepository repo, 
      ActivityQueryBuilder entities,
      PagingAndSortingAssembler assembler,
      MailConfiguration mailConfig,
      CategoryService categoryService) {
    super(repo, entities, assembler);
    
    this.mailConfig = mailConfig;
    this.categoryService = categoryService;
  }

  @Override
  public ActivityEntity getExisting(ActivityEntity activity) {
    return repo.findById(activity.getId()).orElse(null);
  }
  
  @Override
  public boolean validCreateFieldConstraints(ActivityEntity newActivity) {
    return newActivity.getName() != null && !newActivity.getName().isEmpty()
        && newActivity.getAddressId() != null && !newActivity.getAddressId().isEmpty()
        && newActivity.getCategoryId() != null && !newActivity.getCategoryId().isEmpty()
        && newActivity.getOrganisationId() != null && !newActivity.getOrganisationId().isEmpty()
        && validContactData(newActivity);
  }
  
  @Override
  public boolean validUpdateFieldConstraints(ActivityEntity newActivity) {
    return newActivity.getName() != null && !newActivity.getName().isEmpty()
        && validContactData(newActivity);
  }

  /**
   * Valid contact data.
   *
   * @param newActivity the new activity
   * @return true, if successful
   */
  private boolean validContactData(ActivityEntity newActivity) {
    return (newActivity.getMail() != null && !newActivity.getMail().isEmpty())
        || (newActivity.getPhone() != null && !newActivity.getPhone().isEmpty());
  }

  /**
   * Gets the resources by providers.
   *
   * @param providers the providers
   * @return the resources by providers
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Resources<?> getResourcesByProviders(List<ProviderEntity> providers, BaseParams params) 
      throws JsonParseException, JsonMappingException, IOException {
    return assembler.entitiesToResources(getByProviders(providers, params), params);
  }

  /**
   * Gets the by providers.
   *
   * @param providers the providers
   * @param params the params
   * @return the by providers
   */
  public List<ActivityEntity> getByProviders(List<ProviderEntity> providers, BaseParams params) {
    Predicate query = entities.withAnyOfProviders(providers);
    List<ActivityEntity> result = params == null
        ? repo.findAll(query)
        : repo.findAll(query, entities.createSort(params));
    
    if (result == null || result.isEmpty()) {
      throw new NotFoundException(providers.toString());
    }
    return result;
  }
  
  public List<ActivityEntity> getByUser(UserEntity user) {
    return repo.findAll(entities.forUser(user.getId()));
  }
  
  /**
   * Gets the by current.
   *
   * @param current the current
   * @return the by current
   */
  public List<ActivityEntity> getByCurrent(BooleanPrimitive current) {
    if (current != null && current.getValue()) {
      return repo.findAll(entities.withCurrentSchedulesOnly());
    }
    return repo.findAll();
  }
  
  /**
   * Gets the resource by blog id.
   *
   * @param blogId the blog id
   * @return the resource by blog id
   */
  public Resource<ActivityEntity> getResourceByBlogId(String blogId) {
    return assembler.toResource(
        repo
        .findOne(entities.withBlogId(blogId))
        .orElseThrow(() -> new NotFoundException(blogId)));
  }

  /**
   * Checks if is activity for provider.
   *
   * @param activityId
   *          the activity id
   * @param providers
   *          the providers
   * @return true, if is activity for provider
   */
  public boolean isActivityForProvider(String activityId, List<ProviderEntity> providers) {
    return repo.exists(entities.forIdWithAnyOfProviders(activityId, providers));
  }

  @Override
  public ActivityEntity update(String id, ActivityEntity newActivity) {
    return repo.findById(id).map(activity -> {
      activity.setName(newActivity.getName());
      activity.setDescription(newActivity.getDescription());
      activity.setContactName(newActivity.getContactName());
      activity.setPhone(newActivity.getPhone());
      activity.setMail(newActivity.getMail());
      return repo.save(activity);
    }).orElseGet(() -> {
      newActivity.setId(id);
      return repo.save(newActivity);
    });
  }

  /**
   * Update address.
   *
   * @param activityId
   *          the activity id
   * @param address
   *          the address
   * @return the address entity
   */
  public AddressEntity updateAddress(String activityId, AddressEntity address) {
    ActivityEntity activity = getById(activityId);
    activity.setAddress(address);
    return repo.save(activity).getAddress();
  }

  /**
   * Update category.
   *
   * @param activityId
   *          the activity id
   * @param category
   *          the category
   * @return the activity entity
   */
  public ActivityEntity updateCategory(String activityId, CategoryEntity category) {
    ActivityEntity activity = getById(activityId);
    activity.setCategory(category);
    return repo.save(activity);
  }

  /**
   * Update provider.
   *
   * @param activityId
   *          the activity id
   * @param provider
   *          the provider
   * @return the activity entity
   */
  public ActivityEntity updateProvider(String activityId, ProviderEntity provider) {
    ActivityEntity activity = getById(activityId);
    activity.setProvider(provider);
    return repo.save(activity);
  }

  /**
   * Adds the tags.
   *
   * @param activityId
   *          the activity id
   * @param tags
   *          the tags
   * @return the list
   */
  public List<TagEntity> addTags(String activityId, List<TagEntity> tags) {
    ActivityEntity activity = getById(activityId);
    tags.stream().forEach(tagToAdd -> {
      if (activity.getTags().stream().noneMatch(tag -> tag.getId().equals(tagToAdd.getId()))) {
        activity.getTags().add(tagToAdd);
      }
    });
    return repo.save(activity).getTags();
  }

  /**
   * Delete tags.
   *
   * @param activityId
   *          the activity id
   * @param tagIds
   *          the tag ids
   */
  public void deleteTags(String activityId, List<String> tagIds) {
    ActivityEntity activity = getById(activityId);
    activity.getTags().removeIf(tag -> tagIds.contains(tag.getId()));
    repo.save(activity);
  }

  /**
   * Adds the target groups.
   *
   * @param activityId
   *          the activity id
   * @param targetGroups
   *          the target groups
   * @return the list
   */
  public List<TargetGroupEntity> addTargetGroups(String activityId,
      List<TargetGroupEntity> targetGroups) {
    ActivityEntity activity = getById(activityId);
    targetGroups.stream().forEach(targetGroupToAdd -> {
      if (activity.getTargetGroups().stream()
          .noneMatch(targetGroup -> targetGroup.getId().equals(targetGroupToAdd.getId()))) {
        activity.getTargetGroups().add(targetGroupToAdd);
      }
    });
    return repo.save(activity).getTargetGroups();
  }

  /**
   * Delete target group.
   *
   * @param activityId
   *          the activity id
   * @param targetGroupIds
   *          the target group ids
   */
  public void deleteTargetGroup(String activityId, List<String> targetGroupIds) {
    ActivityEntity activity = getById(activityId);
    activity.getTargetGroups()
        .removeIf(targetGroup -> targetGroupIds.contains(targetGroup.getId()));
    // TODO: Check if target groups are nullable and throw exception if last target
    // group is deleted
    repo.save(activity);
  }

  /**
   * Delete all by providers.
   *
   * @param providers the providers
   */
  public void deleteAllByProviders(List<ProviderEntity> providers) {
    List<ActivityEntity> activitiesToDelete = getByProviders(providers, null);
    repo.deleteAll(activitiesToDelete);
  }

  /**
   * Increase like.
   *
   * @param activityId the activity id
   */
  public void increaseLike(String activityId) {
    ActivityEntity activity = getById(activityId);
    activity.setLikes(activity.getLikes() + 1);
    repo.save(activity);
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
   * Adds the images.
   *
   * @param id the id
   * @param images the images
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<ImageEntity> addImages(
      String id,
      List<ImageEntity> images) throws IOException {
    ActivityEntity savedEntity = null;
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
   * @return the activity entity
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public ActivityEntity addImage(String id, ImageEntity image) throws IOException {
    ActivityEntity blog = getById(id);
    blog.getImages().add(image);
    return repo.save(blog);
  }
 
  /**
   * Creates an ICal String
   * @return 
   * @throws URISyntaxException 
   *
   * 
   */
  public String generateCalenderFile(String activityId) {
    ActivityEntity activity = repo.findOne(entities.withId(activityId))
        .orElseThrow(() -> new NotFoundException(activityId));
    CategoryEntity category = categoryService.getById(activity.getCategory().getId());
  
    UidGenerator ug = new RandomUidGenerator();
    Calendar calendar = new Calendar();
    calendar.add(new ProdId("-//" + mailConfig.getPortalName() + "//IcalExport//EN"));
    calendar.add(Version.VERSION_2_0);
    calendar.add(CalScale.GREGORIAN);
    
    for (ScheduleEntity schedule: activity.getSchedules()) {      
      VEvent event = new VEvent(
          LocalDateTime.ofInstant(schedule.getStartDate().toInstant(), TimeZone.getDefault().toZoneId()),
          LocalDateTime.ofInstant(schedule.getEndDate().toInstant(), TimeZone.getDefault().toZoneId()),
          activity.getName());

      try {
        if (activity.getContactName() != null && activity.getMail() != null) {
          event.add(new Organizer(
              new ParameterList(List.of(new Cn(activity.getContactName()))),
              "MAILTO:" + activity.getMail()));
        }
      } catch (URISyntaxException e) { }
      
      event.add(new Description(activity.getDescription()));
      event.add(ug.generateUid());
      event.add(new Status("confirmed"));
      event.add(new Location(activity.getAddress().getStreet() + " "+ activity.getAddress().getHouseNumber()
         +" " + activity.getAddress().getPostalCode() + " "+activity.getAddress().getPlace()));
      event.add(new Categories(category.getName()));
      event.add(new Geo(activity.getAddress().getLatitude()+";" +activity.getAddress().getLongitude()));
      calendar.add(event);
    }
    return calendar.toString();
  }
        
}



