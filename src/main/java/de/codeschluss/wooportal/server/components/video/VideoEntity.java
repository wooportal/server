package de.codeschluss.wooportal.server.components.video;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.image.ImageEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * The Class VideoEntity.
 *  
 * @author Valmir Etemi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "videos")
@Relation(collectionRelation = "data")
public class VideoEntity extends BaseResource {

  private static final long serialVersionUID = 1L;
  
  private String url;
  
  @ManyToOne(cascade = CascadeType.REMOVE)
  @ToString.Exclude
  @JsonIgnore
  @JoinColumn(nullable = true)
  private ImageEntity thumbnail;
  
  @ManyToOne
  @ToString.Exclude
  @JsonIgnore
  @JoinColumn(nullable = false)
  private OrganisationEntity organisation;
  
  @Transient
  @JsonSerialize
  @JsonDeserialize
  private String thumbnailUrl;
  
  @Transient
  @JsonSerialize
  @JsonDeserialize
  private String thumbnailCaption;
  
  @Override
  public List<Link> createResourceLinks() {
    return new ArrayList<Link>();
  }

}