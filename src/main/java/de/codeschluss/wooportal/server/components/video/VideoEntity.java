package de.codeschluss.wooportal.server.components.video;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.Link;

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
public class VideoEntity extends BaseResource {

  private static final long serialVersionUID = 1L;

  private String url;
  
  @ManyToOne
  @ToString.Exclude
  @JsonIgnore
  @JoinColumn(nullable = false)
  private OrganisationEntity organisation;

  @Override
  public List<Link> createResourceLinks() {
    return new ArrayList<Link>();
  }

}
