package de.codeschluss.wooportal.server.components.markup;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.markup.translations.MarkupTranslatablesEntity;
import de.codeschluss.wooportal.server.components.markup.visitors.MarkupVisitorEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.annotations.Visitable;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Localized
@Visitable
@Table(name = "markups")
@Relation(collectionRelation = "data")
@GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
)
public class MarkupEntity extends BaseResource {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false, unique = true)
  private String tagId;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String content;

  @OneToMany(
      fetch = FetchType.EAGER, 
      mappedBy = "parent",
      orphanRemoval = true)
  @ToString.Exclude
  @JsonIgnore
  protected Set<MarkupTranslatablesEntity> translatables;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<MarkupVisitorEntity> visits;

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(selfLink());
    
    return links;
  }

  public Link selfLink() {
    return linkTo(methodOn(MarkupController.class)
        .readOne(id)).withSelfRel();
  }
}
