package de.codeschluss.wooportal.server.core.analytics.visit.page;

import org.springframework.stereotype.Service;
import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

@Service
public class PageQueryBuilder extends QueryBuilder<QPageEntity> {
  
  public PageQueryBuilder() {
    super(QPageEntity.pageEntity, "name");
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    return query.name.likeIgnoreCase(params.getFilter());
  }

  public Predicate withName(String name) {
    return query.name.eq(name);
  }
  
}
