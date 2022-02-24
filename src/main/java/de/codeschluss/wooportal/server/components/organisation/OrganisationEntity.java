package de.codeschluss.wooportal.server.components.organisation;

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
import javax.persistence.ManyToOne;
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.address.AddressEntity;
import de.codeschluss.wooportal.server.components.organisation.translations.OrganisationTranslatablesEntity;
import de.codeschluss.wooportal.server.components.organisation.visitors.OrganisationVisitorEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
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

/**
 * The persistent class for the organisations database table.
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
@Visitable(overview = "organisations")
@Table(name = "organisations")
@Relation(collectionRelation = "data")
@GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
)
public class OrganisationEntity extends BaseResource {
  
  private static final long serialVersionUID = 1L;
  
  @Transient
  @JsonDeserialize
  private String addressId;
  
  @ManyToOne
  @JsonIgnore
  @ToString.Exclude
  private AddressEntity address;
  
  @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
  @JsonProperty(access = Access.READ_ONLY)
  private boolean approved;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String description;
  
  @ManyToMany(fetch = FetchType.EAGER)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "organisations_images",
      joinColumns = @JoinColumn(name = "organisation_id"),
      inverseJoinColumns = @JoinColumn(name = "image_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "organisation_id", "image_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<ImageEntity> images;
  
  @JsonProperty(access = Access.READ_ONLY)
  private int likes;

  private String mail;

  @Column(unique = true, nullable = false)
  private String name;

  private String phone;
  
  @OneToMany(mappedBy = "organisation", fetch = FetchType.LAZY)
  @JsonIgnore
  @ToString.Exclude
  private List<ProviderEntity> providers;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @JsonIgnore
  @ToString.Exclude
  protected Set<OrganisationTranslatablesEntity> translatables;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<OrganisationVisitorEntity> visits;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
  @JsonIgnore
  private List<VideoEntity> videos;
  
  @OneToOne
  @JsonIgnore
  @ToString.Exclude
  @JoinColumn(name="avatar_id")
  private ImageEntity avatar;
  
  private String website;

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(OrganisationController.class)
        .readOne(id)).withSelfRel());
    links.add(linkTo(methodOn(OrganisationController.class)
        .readActivities(id, null)).withRel("activities"));
    links.add(linkTo(methodOn(OrganisationController.class)
        .readUsers(id)).withRel("users"));
    links.add(linkTo(methodOn(OrganisationController.class)
        .readAddress(id)).withRel("address"));
    links.add(linkTo(methodOn(OrganisationController.class)
        .readTranslations(id)).withRel("translations"));
    links.add(linkTo(methodOn(OrganisationController.class)
        .readImages(id)).withRel("images"));
    links.add(linkTo(methodOn(OrganisationController.class)
        .readAvatar(id)).withRel("avatar"));
    
    try {
      links.add(linkTo(methodOn(OrganisationController.class)
          .calculateVisitors(id)).withRel("visitors"));
      links.add(linkTo(methodOn(OrganisationController.class)
          .calculateVisits(id)).withRel("visits"));
    } catch (Throwable e) {
      e.printStackTrace();
    }

    return links;
  }
  
  public Link selfLink() {
    return linkTo(methodOn(OrganisationController.class).readOne(id)).withSelfRel();
  }
}
