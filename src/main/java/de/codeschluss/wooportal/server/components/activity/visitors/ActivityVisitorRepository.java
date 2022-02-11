package de.codeschluss.wooportal.server.components.activity.visitors;

import org.springframework.stereotype.Repository;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorRepository;

@Repository
public interface ActivityVisitorRepository
    extends VisitorRepository<ActivityVisitorEntity> {
}
