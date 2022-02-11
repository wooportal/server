package de.codeschluss.wooportal.server.core.analytics.visit.entities;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.springframework.hateoas.core.Relation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
@Relation(collectionRelation = "data")
public abstract class VisitorEntity<E extends BaseEntity> extends BaseEntity {

  private static final long serialVersionUID = 1L;
  
  @Column(nullable = false)
  protected Integer visits;
  
  @Column(nullable = true, unique = true)
  private String ipAddress;
  
  @Column(nullable = false)
  private String userAgent;
  
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

  