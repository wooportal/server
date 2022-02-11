package de.codeschluss.wooportal.server.components.blog.visitors;

import org.springframework.stereotype.Repository;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorRepository;

@Repository
public interface BlogVisitorRepository
    extends VisitorRepository<BlogVisitorEntity> {
}
