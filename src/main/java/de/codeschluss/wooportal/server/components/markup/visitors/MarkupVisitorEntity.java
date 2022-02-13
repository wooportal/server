package de.codeschluss.wooportal.server.components.markup.visitors;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.hateoas.core.Relation;
import de.codeschluss.wooportal.server.components.markup.MarkupEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "markup_visitors")
@Relation(collectionRelation = "data")
public class MarkupVisitorEntity extends VisitableEntity<MarkupEntity> {

  private static final long serialVersionUID = 1L;
}