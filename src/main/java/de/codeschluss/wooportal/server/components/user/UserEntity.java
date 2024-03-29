package de.codeschluss.wooportal.server.components.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import de.codeschluss.wooportal.server.core.security.Sensible;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The persistent class for the users database table.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Sensible
@Table(name = "users")
@Relation(collectionRelation = "data")
public class UserEntity extends BaseResource {

  private static final long serialVersionUID = 1L;
  
  @OneToOne
  @JsonIgnore
  @ToString.Exclude
  @JoinColumn(nullable = true)
  private BloggerEntity blogger;
  
  @Transient
  @JsonDeserialize
  private boolean applyBlogger;

  private String name;

  @Column(nullable = false)
  @JsonProperty(access = Access.WRITE_ONLY)
  private String password;

  private String phone;
  
  @Transient
  @JsonDeserialize
  private List<String> organisationRegistrations;
  
  @OneToMany(mappedBy = "user")
  @JsonIgnore
  @ToString.Exclude
  private List<ProviderEntity> providers;
  
  @OneToOne
  @JsonIgnore
  @ToString.Exclude
  @JoinColumn(name="avatar_id")
  private ImageEntity avatar;

  @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
  @JsonProperty(access = Access.READ_ONLY)
  private boolean superuser;
  
  @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
  @JsonProperty(access = Access.READ_ONLY)
  private boolean translator;

  @Column(unique = true, nullable = false)
  private String username;

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(UserController.class)
        .readOne(getId())).withSelfRel());
    links.add(linkTo(methodOn(UserController.class)
        .readOrganisations(id)).withRel("organisations"));
    links.add(linkTo(methodOn(UserController.class)
        .readActivities(id, null)).withRel("activities"));
    links.add(linkTo(methodOn(UserController.class)
        .readBlogs(id, null)).withRel("blogs"));
    links.add(linkTo(methodOn(UserController.class)
        .readAvatar(id)).withRel("avatar"));

    return links;
  }

}