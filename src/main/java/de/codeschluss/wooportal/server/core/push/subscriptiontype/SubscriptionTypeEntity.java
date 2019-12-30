package de.codeschluss.wooportal.server.core.push.subscriptiontype;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@Table(name = "subscription_types")
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

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();
    
    links.add(linkTo(methodOn(SubscriptionTypeController.class)
        .readOne(id)).withSelfRel());
    
    return links;
  }
}
