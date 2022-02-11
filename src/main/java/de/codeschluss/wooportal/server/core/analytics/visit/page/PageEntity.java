package de.codeschluss.wooportal.server.core.analytics.visit.page;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.codeschluss.wooportal.server.core.analytics.visit.annotations.Visitable;
import de.codeschluss.wooportal.server.core.analytics.visit.page.visitors.PageVisitorEntity;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@Visitable
@Entity
@Table(name = "pages")
public class PageEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
  @ToString.Exclude
  @JsonIgnore
  protected Set<PageVisitorEntity> visits;
  
  private String name;

}
