package de.codeschluss.wooportal.server.components.images.organisation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.codeschluss.wooportal.server.components.organisation.OrganisationController;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.core.entity.BaseResource;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.core.Relation;
import org.springframework.util.Base64Utils;

/**
 * The persistent class for the organisation_images database table.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "organisation_images")
@Relation(collectionRelation = "data")
public class OrganisationImageEntity extends BaseResource {
  
  private static final long serialVersionUID = 1L;
  
  private String caption;
  
  @Lob
  @Column(columnDefinition = "MEDIUMBLOB", nullable = false)
  @JsonIgnore
  private byte[] image;
  
  @Transient
  @JsonSerialize
  @JsonDeserialize
  private String imageData;
  
  @Column(name = "mime_type", nullable = false)
  private String mimeType;
  
  @ManyToOne
  @JsonIgnore
  private OrganisationEntity organisation;
  
  @Override
  public List<Link> createResourceLinks() {
    List<Link> links = new ArrayList<Link>();

    links.add(linkTo(methodOn(OrganisationController.class)
        .readImages(getOrganisation().getId())).withSelfRel());

    return links;
  }
  
  public String getImageData() {
	  if (this.imageData != null) {
		  return this.imageData;
	  }
	  if (this.image != null) {
		  return Base64Utils.encodeToString(image);  
	  }
	  return null;
  }

}
