package de.codeschluss.wooportal.server.core.analytics.visit.visitable;

import org.springframework.data.repository.NoRepositoryBean;
import de.codeschluss.wooportal.server.core.analytics.visit.visitor.VisitorEntity;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.repository.DataRepository;

@NoRepositoryBean
public interface VisitableRepository<T extends VisitableEntity<?>> extends DataRepository<T> {

  <E extends BaseEntity> T findByParentAndVisitor(E parent, VisitorEntity visitor);
  
  <E extends BaseResource> T findByParentAndVisitor(E parent, VisitorEntity visitor);
}