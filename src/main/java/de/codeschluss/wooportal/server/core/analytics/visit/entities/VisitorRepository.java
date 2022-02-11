package de.codeschluss.wooportal.server.core.analytics.visit.entities;

import org.springframework.data.repository.NoRepositoryBean;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.repository.DataRepository;

@NoRepositoryBean
public interface VisitorRepository<T extends VisitorEntity<?>> extends DataRepository<T> {

  <E extends BaseEntity> T findByParentAndIpAddress(E parent, String ipAddress);
  
  <E extends BaseResource> T findByParentAndIpAddress(E parent, String ipAddress);
}