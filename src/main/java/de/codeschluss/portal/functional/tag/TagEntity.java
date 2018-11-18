package de.codeschluss.portal.functional.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.codeschluss.portal.core.common.BaseEntity;
import de.codeschluss.portal.functional.activity.ActivityEntity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.core.Relation;

/**
 * The persistent class for the tags database table.
 * 
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Table(name = "tags")
@Entity
@Relation(collectionRelation = "data")
public class TagEntity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String description;

  private String name;

  @ManyToMany(mappedBy = "tags")
  @JsonIgnore
  private List<ActivityEntity> activities;

  public void setName(String name) {
    // TODO: More preparations
    this.name = name.trim();
  }

}
