package de.codeschluss.wooportal.server.components.activity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
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
import de.codeschluss.wooportal.server.components.schedule.ScheduleService;
import de.codeschluss.wooportal.server.components.suburb.SuburbService;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupEntity;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupService;
import de.codeschluss.wooportal.server.components.user.UserEntity;
import de.codeschluss.wooportal.server.core.analytics.AnalyticsEntry;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.api.dto.BaseParams;
import de.codeschluss.wooportal.server.core.api.dto.BooleanPrimitive;
import de.codeschluss.wooportal.server.core.config.GeneralPropertyConfiguration;
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
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

/**
 * The Class ActivityService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class ActivityService extends ResourceDataService<ActivityEntity, ActivityQueryBuilder> {

  private GeneralPropertyConfiguration generalConfig;
  
  private CategoryService categoryService;
  
  private ScheduleService scheduleService;
  
  private SuburbService suburbService;
  
  private TargetGroupService targetGroupService;
  
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
      GeneralPropertyConfiguration generalConfig,
      CategoryService categoryService,
      ScheduleService scheduleService,
      SuburbService suburbService,
      TargetGroupService targetGroupService) {
    super(repo, entities, assembler);
    
    this.generalConfig = generalConfig;
    this.categoryService = categoryService;
    this.scheduleService = scheduleService;
    this.suburbService = suburbService; 
    this.targetGroupService = targetGroupService;
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
    ActivityEntity activity = getById(id);
    activity.getImages().add(image);
    return repo.save(activity);
  }
  
  public List<AnalyticsEntry> calculateActivitiesPerCategory(BooleanPrimitive current) {
    var data = categoryService.getAll().stream().collect(Collectors.toMap(c -> c, c -> 0.0));
    var activities = getByCurrent(current);
    if (activities != null && !activities.isEmpty()) {
      for (var activity : activities) {
        var category = activity.getCategory();
        var value = data.get(category);
        data.put(category, value + 1);
      }
    }
    
    return data.entrySet()
        .stream()
        .map(entry -> new AnalyticsEntry(
            entry.getKey().getName(), entry.getValue(), entry.getKey().getColor()))
        .sorted()
        .collect(Collectors.toList());
  }
  
  public List<AnalyticsEntry> calculateActivitiesPerSuburb(BooleanPrimitive current) {
    var data = suburbService.getAll().stream().collect(Collectors.toMap(c -> c, c -> 0.0));
    var activities = getByCurrent(current);
    if (activities != null && !activities.isEmpty()) {
      for (var activity : activities) {
        var suburb = activity.getAddress().getSuburb();
        var value = data.get(suburb);
        data.put(suburb, value + 1);
      }
    }
    
    return data.entrySet()
        .stream()
        .map(entry -> new AnalyticsEntry(
            entry.getKey().getName(), entry.getValue(), null))
        .sorted()
        .collect(Collectors.toList());
  }
  
  public List<AnalyticsEntry> calculateActivitiesPerTargetGroup(BooleanPrimitive current) {
    var data = targetGroupService.getAll().stream().collect(Collectors.toMap(c -> c, c -> 0.0));
    var activities = getByCurrent(current);
    if (activities != null && !activities.isEmpty()) {
      for (var activity : activities) {
        for (var targetGroup : activity.getTargetGroups()) {
          var value = data.get(targetGroup);
          data.put(targetGroup, value + 1);
        }
      }
    }
    
    return data.entrySet()
        .stream()
        .map(entry -> new AnalyticsEntry(
            entry.getKey().getName(), entry.getValue(), null))
        .sorted()
        .collect(Collectors.toList());
  }
 
  /**
   * Creates an ICal String
   * 
   * @return
   * @throws URISyntaxException
   *
   */
  public String generateAllIcal(String activityId) {
    ActivityEntity activity = repo.findOne(entities.withId(activityId))
        .orElseThrow(() -> new NotFoundException(activityId));
    Calendar calendar = createCalendar();
    
    for (ScheduleEntity schedule : activity.getSchedules()) {
      calendar.add(createVEvent(activity, 
          schedule,
          categoryService.getById(activity.getCategory().getId())));
    }
    
    return calendar.toString();
  }

  public String generateIcal(String activityId, String scheduleId) {
    ActivityEntity activity = repo.findOne(entities.withId(activityId))
        .orElseThrow(() -> new NotFoundException(activityId));
    Calendar calendar = createCalendar();
    
    calendar.add(createVEvent(activity, 
        scheduleService.getById(scheduleId),
        categoryService.getById(activity.getCategory().getId())));
    
    return calendar.toString();
  }
  
  private Calendar createCalendar() {
    Calendar calendar = new Calendar();
    calendar.add(new ProdId("-//" + generalConfig.getPortalName() + "//IcalExport//EN"));
    calendar.add(Version.VERSION_2_0);
    calendar.add(CalScale.GREGORIAN);
    return calendar;
  }

  public VEvent createVEvent(
      ActivityEntity activity,
      ScheduleEntity schedule,
      CategoryEntity category) {
    VEvent event = new VEvent(
        LocalDateTime.ofInstant(schedule.getStartDate().toInstant(),
            TimeZone.getDefault().toZoneId()),
        LocalDateTime.ofInstant(schedule.getEndDate().toInstant(),
            TimeZone.getDefault().toZoneId()),
        activity.getName());

    try {
      if (activity.getContactName() != null && activity.getMail() != null) {
        event.add(new Organizer(new ParameterList(List.of(new Cn(activity.getContactName()))),
            "MAILTO:" + activity.getMail()));
      }
    } catch (URISyntaxException e) {
    }
    event.add(new Description(activity.getDescription()));
    event.add(new Uid(UUID.randomUUID().toString()));
    event.add(new Status("confirmed"));
    event.add(new Location(
        activity.getAddress().getStreet() + " " + activity.getAddress().getHouseNumber() + " "
            + activity.getAddress().getPostalCode() + " " + activity.getAddress().getPlace()));
    event.add(new Categories(category.getName()));
    event.add(new Geo(
        activity.getAddress().getLatitude() + ";" + activity.getAddress().getLongitude()));
    return event;
  }

  public Integer calculateVisitors(String activityId) {
    // TODO Auto-generated method stub
    return null;
  }
}


