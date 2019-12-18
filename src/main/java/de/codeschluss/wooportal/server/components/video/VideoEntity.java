package de.codeschluss.wooportal.server.components.video;

import de.codeschluss.wooportal.server.core.entity.BaseResource;
import de.codeschluss.wooportal.server.core.i18n.annotations.Localized;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@Localized
@Table(name = "videos")
public class VideoEntity extends BaseResource {

  private static final long serialVersionUID = 1L;

  private String url;

  @Override
  public List<Link> createResourceLinks() {
    return new ArrayList<Link>();
  }

}
