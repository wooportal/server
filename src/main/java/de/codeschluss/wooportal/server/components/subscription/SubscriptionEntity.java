package de.codeschluss.wooportal.server.components.subscription;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.components.page.PageEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.push.subscriptiontype.SubscriptionTypeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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

/**
 * The persistent class for the subscription database table.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
@GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
)
public class SubscriptionEntity extends BaseResource {
  
  private static final long serialVersionUID = 1L;

  @Column(name = "auth_secret", nullable = false, unique = true)
  private String authSecret;
  
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "subscription_activities_likes",
      joinColumns = @JoinColumn(name = "subscription_id"),
      inverseJoinColumns = @JoinColumn(name = "activity_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "subscription_id", "activity_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<ActivityEntity> activityLikes;
  
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "subscription_blog_likes",
      joinColumns = @JoinColumn(name = "subscription_id"),
      inverseJoinColumns = @JoinColumn(name = "blog_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "subscription_id", "blog_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<BlogEntity> blogLikes;
  
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "subscription_organisation_likes",
      joinColumns = @JoinColumn(name = "subscription_id"),
      inverseJoinColumns = @JoinColumn(name = "organisation_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "subscription_id", "organisation_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<OrganisationEntity> organisationLikes;
  
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "subscription_page_likes",
      joinColumns = @JoinColumn(name = "subscription_id"),
      inverseJoinColumns = @JoinColumn(name = "page_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "subscription_id", "page_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<PageEntity> pageLikes;
  
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "subscribed_types",
      joinColumns = @JoinColumn(name = "subscription_id"),
      inverseJoinColumns = @JoinColumn(name = "subscription_type_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "subscription_id", "subscription_type_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<SubscriptionTypeEntity> subscribedTypes;

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();
    
    links.add(linkTo(methodOn(SubscriptionController.class)
        .readOne(id)).withSelfRel());
    links.add(linkTo(methodOn(SubscriptionController.class)
        .readSubscribedTypes(id)).withRel("subscribedtypes"));
    
    return links;
  }

}
