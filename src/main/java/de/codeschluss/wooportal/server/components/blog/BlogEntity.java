package de.codeschluss.wooportal.server.components.blog;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.blog.translations.BlogTranslatablesEntity;
import de.codeschluss.wooportal.server.components.blog.visitors.BlogVisitorEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.components.topic.TopicEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.annotations.Visitable;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The persistent class for the blogs database table.
 * 
 * @author Valmir Etemi
 *
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Data
@Localized
@Visitable(overview = "blogposts")
@Entity
@Table(name = "blogs")
@Relation(collectionRelation = "data")
@GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
)
public class BlogEntity extends BaseResource {
  private static final long serialVersionUID = 1L;
  
  @JsonProperty(access = Access.READ_ONLY)
  private Boolean approved;
  
  @Transient
  @JsonDeserialize
  private String topicId;

  @ManyToOne
  @ToString.Exclude
  @JsonIgnore
  @JoinColumn(nullable = false)
  private TopicEntity topic;
  
  private String author;
  
  private String mailAddress;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(nullable = false)
  private BloggerEntity blogger;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String content;
  
  @ManyToMany(fetch = FetchType.EAGER)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(
      name = "blogs_images",
      joinColumns = @JoinColumn(name = "blog_id"),
      inverseJoinColumns = @JoinColumn(name = "image_id"),
      uniqueConstraints = {
          @UniqueConstraint(columnNames = { "blog_id", "image_id" })
      })
  @CollectionId(
      columns = @Column(name = "id"),
      type = @Type(type = "uuid-char"),
      generator = "UUID"
  )
  private List<ImageEntity> images;
  
  @JsonProperty(access = Access.READ_ONLY)
  private int likes;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String title;
  
  @OneToOne
  @JsonIgnore
  @ToString.Exclude
  @JoinColumn(name="title_image_id")
  private ImageEntity titleImage;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<BlogTranslatablesEntity> translatables;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<BlogVisitorEntity> visits;
  
  public String getAuthor() {
    return author != null && !author.isBlank() 
        ? author
        : getBlogger().getUser().getName();
  }

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(selfLink());
    links.add(linkTo(methodOn(BlogController.class)
        .readImages(id)).withRel("images"));
    links.add(linkTo(methodOn(BlogController.class)
        .readTopic(id)).withRel("topic"));
    links.add(linkTo(methodOn(BlogController.class)
        .readBlogger(id)).withRel("blogger"));
    links.add(linkTo(methodOn(BlogController.class)
        .readTitleImage(id)).withRel("titleImage"));
    try {
      links.add(linkTo(methodOn(BlogController.class)
          .calculateVisitors(id)).withRel("visitors"));
    } catch (Throwable e) {
      e.printStackTrace();
    }
    
    return links;
  }
  
  public Link selfLink() {
    return linkTo(methodOn(BlogController.class)
        .readOne(id)).withSelfRel();
  }
}
