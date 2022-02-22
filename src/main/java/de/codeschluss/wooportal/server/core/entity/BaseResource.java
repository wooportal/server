package de.codeschluss.wooportal.server.core.entity;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class BaseResource.
 * 
 * @author Valmir Etemi
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseResource extends BaseEntity {
  
  private static final long serialVersionUID = 1L;
  
  @SuppressWarnings("unchecked")
  public <B extends BaseEntity> Resource<B> toResource() {
    return (Resource<B>) new Resource<>(this, createResourceLinks());
  }

  public abstract List<Link> createResourceLinks();
  
  @JsonProperty("_embedded")
  @Transient
  @JsonDeserialize
  @JsonInclude(Include.NON_NULL)
  protected Map<String,Object> embeddings;
}
