package de.codeschluss.wooportal.server.core.analytics.visit.visitable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.springframework.hateoas.core.Relation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.codeschluss.wooportal.server.core.analytics.visit.visitor.VisitorEntity;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
@Relation(collectionRelation = "data")
public abstract class VisitableEntity<E extends BaseEntity> extends BaseEntity {

  private static final long serialVersionUID = 1L;
  
  @Column(nullable = false)
  protected Integer visits;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  protected VisitorEntity visitor;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "parent_id", nullable = false)
  protected E parent;
  
  @SuppressWarnings("unchecked")
  public void setParent(BaseEntity parent) {
    this.parent = (E) parent;
  }

  public void addVisit() {
    this.visits = this.visits != null
        ? this.visits + 1
        : 1;
  }

}

  