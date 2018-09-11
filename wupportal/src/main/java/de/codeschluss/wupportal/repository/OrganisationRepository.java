package de.codeschluss.wupportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.codeschluss.wupportal.model.Organisation;

public interface OrganisationRepository extends JpaRepository<Organisation, String>{

}