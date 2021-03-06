package de.codeschluss.wooportal.server.components.page;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.codeschluss.wooportal.server.components.page.translations.PageTranslatablesEntity;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;

/**
 * The persistent class for the pages database table.
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
@Table(name = "pages")
@Relation(collectionRelation = "data")
public class PageEntity extends BaseResource {

  private static final long serialVersionUID = 1L;
  
  @Transient
  @JsonDeserialize
  private String topicId;

  @ManyToOne
  @ToString.Exclude
  @JsonIgnore
  @JoinColumn(nullable = false)
  private TopicEntity topic;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String content;
  
  @JsonProperty(access = Access.READ_ONLY)
  private int likes;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String title;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<PageTranslatablesEntity> translatables;
  
  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(selfLink());
    links.add(linkTo(methodOn(PageController.class)
        .readTopic(id)).withSelfRel());
    links.add(linkTo(methodOn(PageController.class)
        .readTranslations(id)).withRel("translations"));

    return links;
  }

  public Link selfLink() {
    return linkTo(methodOn(PageController.class)
        .readOne(id)).withSelfRel();
  }

}
