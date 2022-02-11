package de.codeschluss.wooportal.server.core.analytics.visit.page.visitors;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.hateoas.core.Relation;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.page.PageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "page_visitors")
@Relation(collectionRelation = "data")
public class PageVisitorEntity extends VisitorEntity<PageEntity> {

  private static final long serialVersionUID = 1L;
}