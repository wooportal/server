package de.codeschluss.wooportal.server.components.targetgroup;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.targetgroup.translations.TargetGroupTranslatablesEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;

/**
 * The persistent class for the target_groups database table.
 * 
 * @author Valmir Etemi
 *
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Localized
@Table(name = "target_groups")
@Relation(collectionRelation = "data")
public class TargetGroupEntity extends BaseResource {

  private static final long serialVersionUID = 1L;
  
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "targetGroups")
  @JsonIgnore
  private List<ActivityEntity> activities;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String description;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String name;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<TargetGroupTranslatablesEntity> translatables;
  
  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(TargetGroupController.class)
        .readOne(getId())).withSelfRel());
    links.add(linkTo(methodOn(TargetGroupController.class)
        .readTranslations(getId())).withRel("translations"));

    return links;
  }
}
