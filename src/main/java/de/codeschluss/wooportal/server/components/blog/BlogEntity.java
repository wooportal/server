package de.codeschluss.wooportal.server.components.blog;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.codeschluss.wooportal.server.components.activity.ActivityEntity;
import de.codeschluss.wooportal.server.components.blog.translations.BlogTranslatablesEntity;
import de.codeschluss.wooportal.server.components.blogger.BloggerEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;

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
@Entity
@Table(name = "blogs")
@Relation(collectionRelation = "data")
@GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
)
public class BlogEntity extends BaseResource {
  private static final long serialVersionUID = 1L;
  
  @ManyToOne
  @JsonIgnore
  @JoinColumn(nullable = true)
  private ActivityEntity activity;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String author;
  
  @ManyToOne
  @JsonIgnore
  @JoinColumn(nullable = false)
  private BloggerEntity blogger;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String content;
  
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
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
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.REMOVE)
  @ToString.Exclude
  @JsonIgnore
  protected Set<BlogTranslatablesEntity> translatables;
  
  public String getAuthor() {
    return this.getBlogger().getUser().getName();
  }

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(BlogController.class)
        .readOne(id)).withSelfRel());
    links.add(linkTo(methodOn(BlogController.class)
        .readActivity(id)).withRel("activity"));
    links.add(linkTo(methodOn(BlogController.class)
        .readImages(id)).withRel("images"));
    
    return links;
  }
}
