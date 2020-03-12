package de.codeschluss.wooportal.server.components.video;

import com.querydsl.core.types.Predicate;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;
import org.springframework.stereotype.Service;

/**
 * The Class VideoQueryBuilder.
 *  
 * @author Valmir Etemi
 */
@Service
public class VideoQueryBuilder extends QueryBuilder<QVideoEntity> {
  
  /**
   * Instantiates a new video query builder.
   *
   */
  public VideoQueryBuilder() {
    super(QVideoEntity.videoEntity, "url");
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    String filter = prepareFilter(params.getFilter());
    return query.url.likeIgnoreCase(filter);
  }

  public Predicate withIdAndOrgaId(String id, String organisationId) {
    return query.id.eq(id).and(withOrgaId(organisationId));
  }

  public Predicate withOrgaId(String organisationId) {
    return query.organisation.id.eq(organisationId);
  }
}
