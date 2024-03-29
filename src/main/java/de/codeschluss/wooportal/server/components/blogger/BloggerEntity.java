package de.codeschluss.wooportal.server.components.blogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.blog.BlogEntity;
import de.codeschluss.wooportal.server.components.user.UserController;
import de.codeschluss.wooportal.server.components.user.UserEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The persistent class for the bloggers database table.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "bloggers")
@Relation(collectionRelation = "data")
public class BloggerEntity extends BaseResource {

  private static final long serialVersionUID = 1L;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private ImageEntity avatar;
  
  @JsonSerialize
  @JsonDeserialize
  @Transient
  private String name;
  
  @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
  @JsonProperty(access = Access.READ_ONLY)
  private boolean approved;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JsonIgnore
  @ToString.Exclude
  @JoinColumn(nullable = false)
  private UserEntity user;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "blogger")
  @ToString.Exclude
  @JsonIgnore
  private List<BlogEntity> blogs;
  
  public String getName() {
    return getUser().getName();
  }
  
  public ImageEntity getAvatar() {
    return getUser().getAvatar();
  }

  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(UserController.class)
        .readBlogger(getUser().getId())).withSelfRel());
    links.add(linkTo(methodOn(UserController.class)
        .readAvatar(id)).withRel("avatar"));

    return links;
  }

}
