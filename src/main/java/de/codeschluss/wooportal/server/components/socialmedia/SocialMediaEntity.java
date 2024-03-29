package de.codeschluss.wooportal.server.components.socialmedia;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import de.codeschluss.wooportal.server.core.entity.BaseResource;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;

/**
 * The persistent class for the configurations database table.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "social_media")
@Relation(collectionRelation = "data")
public class SocialMediaEntity extends BaseResource {
  
  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private String name;

  @Column(
      nullable = false,
      unique = true)
  private String icon;
  
  @Column(
      nullable = false,
      unique = true)
  private String url;

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(SocialMediaController.class)
        .readOne(getId())).withSelfRel());

    return links;
  }
}