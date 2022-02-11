package de.codeschluss.wooportal.server.components.markup.visitors;

import org.springframework.stereotype.Repository;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorRepository;

@Repository
public interface MarkupVisitorRepository
    extends VisitorRepository<MarkupVisitorEntity> {
}
