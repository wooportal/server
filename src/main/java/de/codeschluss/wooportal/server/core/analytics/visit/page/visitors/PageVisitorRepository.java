package de.codeschluss.wooportal.server.core.analytics.visit.page.visitors;

import org.springframework.stereotype.Repository;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorRepository;

@Repository
public interface PageVisitorRepository
    extends VisitorRepository<PageVisitorEntity> {
}
