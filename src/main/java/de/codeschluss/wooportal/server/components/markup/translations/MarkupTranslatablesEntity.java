package de.codeschluss.wooportal.server.components.markup.translations;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;
import de.codeschluss.wooportal.server.components.markup.MarkupController;
import de.codeschluss.wooportal.server.components.markup.MarkupEntity;
import de.codeschluss.wooportal.server.core.i18n.entities.TranslatableEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "markup_translatables")
@Relation(collectionRelation = "data")
public class MarkupTranslatablesEntity extends TranslatableEntity<MarkupEntity> {

  private static final long serialVersionUID = 1L;
  
  @Column(nullable = false)
  private String content;

  @Override
  public List<Link> createResourceLinks() {    
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(MarkupController.class)
        .readTranslations(parent.getId())).withSelfRel());

    return links;
  }
}