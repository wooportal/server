package de.codeschluss.wooportal.server.core.analytics.visit.page.visitors;

import org.springframework.stereotype.Repository;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableRepository;

@Repository
public interface PageVisitorRepository
    extends VisitableRepository<PageVisitorEntity> {
}
