package de.codeschluss.portal.components.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.codeschluss.portal.components.provider.ProviderEntity;
import de.codeschluss.portal.core.entity.BaseResource;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;

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
@Table(name = "users")
@Relation(collectionRelation = "data")
public class UserEntity extends BaseResource {

  private static final long serialVersionUID = 1L;

  private String fullname;

  @Column(nullable = false)
  private String password;

  private String phone;

  @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean superuser;

  @Column(unique = true, nullable = false)
  private String username;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
  @JsonIgnore
  @ToString.Exclude
  private List<ProviderEntity> providerEntities;

  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  @JsonProperty
  public boolean isSuperuser() {
    return this.superuser;
  }

  @JsonIgnore
  public void setSuperuser(boolean superuser) {
    this.superuser = superuser;
  }

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(UserController.class)
        .readOne(getId())).withSelfRel());
    links.add(linkTo(methodOn(UserController.class)
        .readOrganisations(getId())).withRel("organisations"));
    links.add(linkTo(methodOn(UserController.class)
        .readActivities(getId())).withRel("activities"));

    return links;
  }

}