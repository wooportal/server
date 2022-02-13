package de.codeschluss.wooportal.server.components.activity.visitors;

import org.springframework.stereotype.Repository;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableRepository;

@Repository
public interface ActivityVisitableRepository
    extends VisitableRepository<ActivityVisitorEntity> {
}
