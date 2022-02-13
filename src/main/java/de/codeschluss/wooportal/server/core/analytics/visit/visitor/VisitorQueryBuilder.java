package de.codeschluss.wooportal.server.core.analytics.visit.visitor;

import org.springframework.stereotype.Service;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

@Service
public class VisitorQueryBuilder extends QueryBuilder<QVisitorEntity> {

  public VisitorQueryBuilder() {
    super(QVisitorEntity.visitorEntity, "userAgent");
  }

  @Override
  public <P extends FilterSortPaginate> Predicate search(P params) {
    return query.userAgent.likeIgnoreCase(params.getFilter())
        .or(query.ipAddress.likeIgnoreCase(params.getFilter()));
  }
  
  public BooleanExpression withIpAddress(String ipAddress) {
    return query.ipAddress.eq(ipAddress);
  }

  public BooleanExpression withIpAddressNotEmpty() {
    return query.ipAddress.isNotNull().and(query.ipAddress.isNotEmpty());
  }

}
