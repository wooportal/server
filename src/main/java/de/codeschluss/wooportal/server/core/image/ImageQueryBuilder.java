package de.codeschluss.wooportal.server.core.image;

import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class ImageQueryBuilder extends QueryBuilder<QImageEntity> {
  
  /**
   * Instantiates a new organisation image query builder.
   *
   */
  public ImageQueryBuilder() {
    super(QImageEntity.imageEntity, "caption");
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    String filter = prepareFilter(params.getFilter());
    return query.caption.likeIgnoreCase(filter);
  }
}
