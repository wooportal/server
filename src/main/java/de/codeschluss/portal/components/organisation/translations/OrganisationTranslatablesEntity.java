package de.codeschluss.portal.components.organisation.translations;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.codeschluss.portal.components.organisation.OrganisationEntity;
import de.codeschluss.portal.core.common.BaseEntity;
import de.codeschluss.portal.core.translations.annotations.Translatable;
import de.codeschluss.portal.core.translations.language.LanguageEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the activities database table.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Translatable
@Table(name = "organisation_translatables")
public class OrganisationTranslatablesEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;
  
  @Lob
  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JsonIgnore
  private LanguageEntity language;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "parent_id")
  private OrganisationEntity parent;

}