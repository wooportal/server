package de.codeschluss.wooportal.server.components.analytics;

import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.category.CategoryEntity;
import de.codeschluss.wooportal.server.components.category.CategoryService;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionEntity;
import de.codeschluss.wooportal.server.components.push.subscription.SubscriptionService;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeEntity;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.SubscriptionTypeService;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupEntity;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupService;
import de.codeschluss.wooportal.server.core.api.dto.BooleanPrimitive;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import de.codeschluss.wooportal.server.core.service.DataService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
  
  /** The activity service. */
  private final ActivityService activityService;
  
  /** The category service. */
  private final CategoryService categoryService;
  
  /** The subscription service. */
  private final SubscriptionService subscriptionService;
  
  /** The subscription type service. */
  private final SubscriptionTypeService subscriptionTypeService;
  
  /** The target group service. */
  private final TargetGroupService targetGroupService;
  
  /**
   * Instantiates a new analytics service.
   *
   * @param activityService the activity service
   * @param subscriptionService the subscription service
   * @param translationService the translation service
   */
  public AnalyticsService(
      ActivityService activityService,
      CategoryService categoryService,
      SubscriptionService subscriptionService,
      SubscriptionTypeService subscriptionTypeService,
      TargetGroupService targetGroupService,
      TranslationService translationService) {
    this.activityService = activityService;
    this.categoryService = categoryService;
    this.subscriptionService = subscriptionService;
    this.subscriptionTypeService = subscriptionTypeService;
    this.targetGroupService = targetGroupService; 
  }

  /**
   * Calculate activities per category.
   *
   * @param current the current
   * @return the list
   */
  public List<AnalyticsEntry> calculateActivitiesPerCategory(BooleanPrimitive current) {
    Map<CategoryEntity, Double> data = createContainer(categoryService);
    List<ActivityEntity> activities = activityService.getByCurrent(current);
    if (activities != null && !activities.isEmpty()) {
      for (ActivityEntity activity : activities) {
        CategoryEntity category = activity.getCategory();
        Double value = data.get(category);
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

  /**
   * Calculate activities per target group.
   *
   * @param current the current
   * @return the list
   */
  public List<AnalyticsEntry> calculateActivitiesPerTargetGroup(BooleanPrimitive current) {
    Map<TargetGroupEntity, Double> data = createContainer(targetGroupService);
    List<ActivityEntity> activities = activityService.getByCurrent(current);
    if (activities != null && !activities.isEmpty()) {
      for (ActivityEntity activity : activities) {
        for (TargetGroupEntity targetGroup : activity.getTargetGroups()) {
          Double value = data.get(targetGroup);
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
   * Calculate subscriptions.
   *
   * @return the list
   */
  public List<AnalyticsEntry> calculateSubscriptions() {
    List<SubscriptionEntity> subscriptions = subscriptionService.getAll();
    Map<SubscriptionTypeEntity, Double> data = createContainer(subscriptionTypeService);
    
    if (subscriptions != null && !subscriptions.isEmpty()) {
      for (SubscriptionEntity subscription : subscriptions) {
        for (SubscriptionTypeEntity type : subscription.getSubscribedTypes()) {
          Double value = data.get(type);
          data.put(type, value + 1);
        }
      }
    }
    
    return data.entrySet()
        .stream()
        .map(entry -> 
          new AnalyticsEntry(entry.getKey().getConfigType(), entry.getValue(), null))
        .sorted()
        .collect(Collectors.toList());
  }
  
  private <E extends BaseEntity> Map<E, Double> createContainer(
      DataService<E, ?> service) {
    return service.getAll().stream()
        .collect(Collectors.toMap(c -> c, c -> 0.0));
  }
}
