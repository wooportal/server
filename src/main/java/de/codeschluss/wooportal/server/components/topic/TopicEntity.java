package de.codeschluss.wooportal.server.components.topic;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.topic.translations.TopicTranslatablesEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The persistent class for the topics database table.
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
@Table(name = "topics")
@Relation(collectionRelation = "data")
public class TopicEntity extends BaseResource {

  private static final long serialVersionUID = 1L;
  
  @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<BlogEntity> blogs;

  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String name;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<TopicTranslatablesEntity> translatables;
  
  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(TopicController.class)
        .readOne(id)).withSelfRel());
    links.add(linkTo(methodOn(TopicController.class)
        .readTranslations(id)).withRel("translations"));
    links.add(linkTo(methodOn(TopicController.class)
        .readBlogs(id, null)).withRel("blogs"));

    return links;
  }

}
