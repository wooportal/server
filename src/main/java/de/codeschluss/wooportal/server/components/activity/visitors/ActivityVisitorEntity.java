package de.codeschluss.wooportal.server.components.activity.visitors;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.hateoas.core.Relation;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "activity_visitors")
@Relation(collectionRelation = "data")
public class ActivityVisitorEntity extends VisitableEntity<ActivityEntity> {

  private static final long serialVersionUID = 1L;
}