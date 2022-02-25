package de.codeschluss.wooportal.server.components.blog;

import java.util.List;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BlogQueryParam extends FilterSortPaginate {

  /** The categories. */
  protected List<String> topics;

  public BlogQueryParam(
      String filter, 
      Integer page, 
      Integer size, 
      String sort,
      String dir, 
      String embeddings,
      List<String> topics) {
    super(filter, page, size, sort, dir, embeddings);
    this.topics = topics;
  }
  
  @Override
  public boolean isEmptyQuery() {
    return super.isEmptyQuery()
        && (topics == null || topics.isEmpty());
  }  
}
