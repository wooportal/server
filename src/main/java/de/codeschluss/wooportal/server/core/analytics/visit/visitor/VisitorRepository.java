package de.codeschluss.wooportal.server.core.analytics.visit.visitor;

import org.springframework.stereotype.Repository;
import de.codeschluss.wooportal.server.core.repository.DataRepository;

@Repository
public interface VisitorRepository extends DataRepository<VisitorEntity> {

}
