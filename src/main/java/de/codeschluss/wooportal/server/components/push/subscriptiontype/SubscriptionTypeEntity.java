package de.codeschluss.wooportal.server.components.push.subscriptiontype;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.push.subscriptiontype.translations.SubscriptionTypeTranslatablesEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * The persistent class for the subscription database table.
 * 
 * @author Valmir Etemi
 *
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Localized
@Entity
@Table(name = "subscription_types")
@Relation(collectionRelation = "data")
public class SubscriptionTypeEntity extends BaseResource {
  
  private static final long serialVersionUID = 1L;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String name;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String description;
  
  @Column(name = "config_type", nullable = false, unique = true)
  @JsonIgnore
  private String configType;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<SubscriptionTypeTranslatablesEntity> translatables;

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();
    
    links.add(linkTo(methodOn(SubscriptionTypeController.class)
        .readOne(id)).withSelfRel());
    links.add(linkTo(methodOn(SubscriptionTypeController.class)
        .readTranslations(id)).withRel("translations"));
    
    return links;
  }
}
