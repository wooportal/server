package de.codeschluss.wooportal.server.components.organisation.visitors;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.hateoas.core.Relation;
import de.codeschluss.wooportal.server.components.organisation.OrganisationEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "organisation_visitors")
@Relation(collectionRelation = "data")
public class OrganisationVisitorEntity extends VisitorEntity<OrganisationEntity> {

  private static final long serialVersionUID = 1L;
}