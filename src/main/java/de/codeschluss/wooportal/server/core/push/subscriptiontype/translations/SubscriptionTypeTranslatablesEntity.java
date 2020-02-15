package de.codeschluss.wooportal.server.core.push.subscriptiontype.translations;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import de.codeschluss.wooportal.server.core.i18n.entities.TranslatableEntity;
import de.codeschluss.wooportal.server.core.push.subscriptiontype.SubscriptionTypeController;
import de.codeschluss.wooportal.server.core.push.subscriptiontype.SubscriptionTypeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;

/**
 * The persistent class for the Subscription Type Translatables.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "subscription_type_translatables")
@Relation(collectionRelation = "data")
public class SubscriptionTypeTranslatablesEntity 
    extends TranslatableEntity<SubscriptionTypeEntity> {

  private static final long serialVersionUID = 1L;
  
  @Lob
  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;
  
  @Column(nullable = false)
  private String name;
  
  @Override
  public List<Link> createResourceLinks() {    
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(SubscriptionTypeController.class)
        .readTranslations(parent.getId())).withSelfRel());

    return links;
  }
}