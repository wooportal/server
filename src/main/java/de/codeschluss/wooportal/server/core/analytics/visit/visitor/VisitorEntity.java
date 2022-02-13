package de.codeschluss.wooportal.server.core.analytics.visit.visitor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.hateoas.core.Relation;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "visitors")
@Relation(collectionRelation = "data")
public class VisitorEntity extends BaseEntity {
  
  private static final long serialVersionUID = 1L;

  @Column(nullable = true, unique = true)
  private String ipAddress;
  
  @Column(nullable = false)
  private String userAgent;

}
