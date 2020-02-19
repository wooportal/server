package de.codeschluss.wooportal.server.components.schedule;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * The Class ScheduleQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class ScheduleQueryBuilder extends QueryBuilder<QScheduleEntity> {
  
  public ScheduleQueryBuilder() {
    super(QScheduleEntity.scheduleEntity, "startDate");
  }

  public BooleanExpression forActivityAndCurrentOnly(String activityId) {
    return withActivity(activityId)
    .and(query.startDate.after(Expressions.currentTimestamp()));
  }
 
  public BooleanExpression forActivityAndNext(String activityId) {
    return withActivity(activityId)
        .and(query.startDate.between(new Date(), endOfDay()));
  }
  
  private Date endOfDay() {
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DATE);
    calendar.set(year, month, day, 23, 59, 59);
    return calendar.getTime();
  }

  private BooleanExpression withActivity(String activityId) {
    return query.activity.id.eq(activityId);
  }
  
  @Override
  public BooleanExpression search(FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return query.activity.translatables.any().name.likeIgnoreCase(filter);
  }

}
