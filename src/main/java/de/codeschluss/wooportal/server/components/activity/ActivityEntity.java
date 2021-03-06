package de.codeschluss.wooportal.server.components.activity;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.activity.translations.ActivityTranslatablesEntity;
import de.codeschluss.wooportal.server.components.address.AddressEntity;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.category.CategoryEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.schedule.ScheduleEntity;
import de.codeschluss.wooportal.server.components.tag.TagEntity;
import de.codeschluss.wooportal.server.components.targetgroup.TargetGroupEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;

/**
 * The persistent class for the activities database table.
 *
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Localized
@Table(name = "activities")
@Relation(collectionRelation = "data")
@GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
)
public class ActivityEntity extends BaseResource {

  private static final long serialVersionUID = 1L;

  @Transient
  @JsonDeserialize
  private String addressId;

  @ManyToOne
  @ToString.Exclude
  @JsonIgnore
  private AddressEntity address;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "activity")
  @ToString.Exclude
  @JsonIgnore
  private List<BlogEntity> blogs;

  @Transient
  @JsonDeserialize
  private String categoryId;
  
  @ManyToMany(fetch = FetchType.EAGER)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "activities_images",
      joinColumns = @JoinColumn(name = "activity_id"),
      inverseJoinColumns = @JoinColumn(name = "image_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "activity_id", "image_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<ImageEntity> images;

  @ManyToOne
  @ToString.Exclude
  @JsonIgnore
  @JoinColumn(nullable = false)
  private CategoryEntity category;

  @Column(name = "contact_name")
  private String contactName;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String description;

  @JsonProperty(access = Access.READ_ONLY)
  private int likes;

  private String mail;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String name;

  @Transient
  @JsonDeserialize
  private String organisationId;

  private String phone;

  @ManyToOne
  @ToString.Exclude
  @JsonIgnore
  @JoinColumn(nullable = false)
  private ProviderEntity provider;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "activity")
  @ToString.Exclude
  @JsonIgnore
  private List<ScheduleEntity> schedules;

  @ManyToMany(fetch = FetchType.EAGER)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "activities_tags",
      joinColumns = @JoinColumn(name = "activity_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "activity_id", "tag_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<TagEntity> tags;

  @ManyToMany(fetch = FetchType.EAGER)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "activities_target_groups",
      joinColumns = @JoinColumn(name = "activity_id"),
      inverseJoinColumns = @JoinColumn(name = "target_group_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "activity_id", "target_group_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<TargetGroupEntity> targetGroups;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<ActivityTranslatablesEntity> translatables;

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(selfLink());
    links.add(linkTo(methodOn(ActivityController.class)
        .readOrganisation(id)).withRel("organisation"));
    links.add(linkTo(methodOn(ActivityController.class)
        .readCategory(id)).withRel("category"));
    links.add(linkTo(methodOn(ActivityController.class)
        .readSchedules(id, null)).withRel("schedules"));
    links.add(linkTo(methodOn(ActivityController.class)
        .readTags(id, null)).withRel("tags"));
    links.add(linkTo(methodOn(ActivityController.class)
        .readTargetGroups(id, null)).withRel("targetgroups"));
    links.add(linkTo(methodOn(ActivityController.class)
        .readAddress(id)).withRel("address"));
    links.add(linkTo(methodOn(ActivityController.class)
        .readBlogs(id, null)).withRel("blogs"));
    links.add(linkTo(methodOn(ActivityController.class)
        .readTranslations(id)).withRel("translations"));
    links.add(linkTo(methodOn(ActivityController.class)
        .readImages(id)).withRel("images"));
    
    return links;
  }

  public Link selfLink() {
    return linkTo(methodOn(ActivityController.class)
        .readOne(id)).withSelfRel();
  }
}
