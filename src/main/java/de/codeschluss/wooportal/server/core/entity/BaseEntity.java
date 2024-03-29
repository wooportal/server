package de.codeschluss.wooportal.server.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class BaseEntity.
 * 
 * @author Valmir Etemi
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(columnDefinition = "CHAR")
  @JsonProperty(access = Access.READ_ONLY)
  protected String id;

  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  @Column(
      nullable = false, 
      columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", 
      insertable = false, 
      updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  protected Date modified;

  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  @Column(
      nullable = false, 
      columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", 
      insertable = false, 
      updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  protected Date created;

  public BaseEntity() {
    this.id = UUID.randomUUID().toString();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }  
    if (getClass() != obj.getClass()) {
      return false;
    }
    BaseEntity other = (BaseEntity) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }
}
