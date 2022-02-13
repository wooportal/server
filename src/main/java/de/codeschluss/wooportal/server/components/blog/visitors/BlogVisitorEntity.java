package de.codeschluss.wooportal.server.components.blog.visitors;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.hateoas.core.Relation;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "blog_visitors")
@Relation(collectionRelation = "data")
public class BlogVisitorEntity extends VisitableEntity<BlogEntity> {

  private static final long serialVersionUID = 1L;
}