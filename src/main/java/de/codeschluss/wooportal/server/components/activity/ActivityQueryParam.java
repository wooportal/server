package de.codeschluss.wooportal.server.components.activity;

import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//TODO: Auto-generated Javadoc
/**
* The Class OrganisationQueryParam.
* 
* @author Valmir Etemi
*
*/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ActivityQueryParam extends FilterSortPaginate {

  /** The current. */
  protected Boolean current;
  
  /** The categories. */
  protected List<String> categories;
  
  /** The suburbs. */
  protected List<String> suburbs;
  
  /** The targetgroups. */
  protected List<String> targetgroups;
  
  @DateTimeFormat(iso = ISO.DATE)
  protected Date startDate;
  
  @DateTimeFormat(iso = ISO.DATE)
  protected Date endDate;

  /**
   * Instantiates a new activity query param.
   *
   * @param filter the filter
   * @param page the page
   * @param size the size
   * @param sort the sort
   * @param dir the dir
   * @param current the current
   * @param categories the categories
   * @param suburubs the suburubs
   * @param targetgroups the targetgroups
   */
  public ActivityQueryParam(
      String filter, 
      Integer page, 
      Integer size, 
      String sort,
      String dir, 
      String embeddings,
      Boolean current,
      List<String> categories,
      List<String> suburubs,
      List<String> targetgroups,
      Date startDate,
      Date endDate) {
    super(filter, page, size, sort, dir, embeddings);
    this.current = current;
    this.categories = categories;
    this.suburbs = suburubs;
    this.targetgroups = targetgroups;
    this.startDate = startDate;
    this.endDate = endDate;
  }
  
  @Override
  public boolean isEmptyQuery() {
    return super.isEmptyQuery()
        && startDate != null
        && endDate != null
        && (current == null || !current)
        && (categories == null || categories.isEmpty())
        && (suburbs == null || suburbs.isEmpty())
        && (targetgroups == null || targetgroups.isEmpty());
  }  
}
