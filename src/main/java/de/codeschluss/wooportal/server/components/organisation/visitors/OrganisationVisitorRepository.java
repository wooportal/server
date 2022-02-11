package de.codeschluss.wooportal.server.components.organisation.visitors;

import org.springframework.stereotype.Repository;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorRepository;

@Repository
public interface OrganisationVisitorRepository
    extends VisitorRepository<OrganisationVisitorEntity> {
}
