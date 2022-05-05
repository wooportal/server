package de.codeschluss.wooportal.server.components.markup;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.markup.translations.MarkupTranslatablesEntity;
import de.codeschluss.wooportal.server.components.markup.visitors.MarkupVisitorEntity;
import de.codeschluss.wooportal.server.components.video.VideoEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.annotations.Visitable;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
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
@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
public class MarkupEntity extends BaseResource {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false, unique = true)
  private String tagId;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String content;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String title;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", orphanRemoval = true)
  @ToString.Exclude
  @JsonIgnore
  protected Set<MarkupTranslatablesEntity> translatables;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<MarkupVisitorEntity> visits;


  @OneToOne
  @JsonIgnore
  @ToString.Exclude
  @JoinColumn
  private ImageEntity titleImage;

  @ManyToMany(fetch = FetchType.EAGER)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(name = "markup_videos", joinColumns = @JoinColumn(name = "markup_id"),
      inverseJoinColumns = @JoinColumn(name = "video_id"),
      uniqueConstraints = {@UniqueConstraint(columnNames = {"markup_id", "video_id"})})
  @CollectionId(columns = @Column(name = "id"), type = @Type(type = "uuid-char"),
      generator = "UUID")
  private List<VideoEntity> videos;


  @ManyToMany(fetch = FetchType.EAGER)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(name = "markup_images", joinColumns = @JoinColumn(name = "markup_id"),
      inverseJoinColumns = @JoinColumn(name = "image_id"),
      uniqueConstraints = {@UniqueConstraint(columnNames = {"markup_id", "image_id"})})
  @CollectionId(columns = @Column(name = "id"), type = @Type(type = "uuid-char"),
      generator = "UUID")
  private List<ImageEntity> images;

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(selfLink());

    try {
      links.add(linkTo(methodOn(MarkupController.class).calculateVisitors(id)).withRel("visitors"));
      links.add(linkTo(methodOn(MarkupController.class).readTitleImage(id)).withRel("titleImage"));
    } catch (Throwable e) {
      e.printStackTrace();
    }

    return links;
  }

  public Link selfLink() {
    return linkTo(methodOn(MarkupController.class).readOne(id)).withSelfRel();
  }
}
