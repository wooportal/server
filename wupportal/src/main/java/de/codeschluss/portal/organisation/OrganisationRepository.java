package de.codeschluss.portal.organisation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import de.codeschluss.portal.base.FilteredJpaRepository;

public interface OrganisationRepository extends FilteredJpaRepository<OrganisationEntity, String> {

	@Query("Select o from OrganisationEntity o where o.mail like %?1% or o.name like %?1% or o.phone like %?1% or o.website like %?1% or o.address.houseNumber like %?1% or o.address.place like %?1% or o.address.place like %?1%")
	Optional<List<OrganisationEntity>> findFiltered(String filter, Sort sort);
	
	@Query("Select o from OrganisationEntity o where o.mail like %?1% or o.name like %?1% or o.phone like %?1% or o.website like %?1% or o.address.houseNumber like %?1% or o.address.place like %?1% or o.address.suburb like %?1%")
	Optional<Page<OrganisationEntity>> findFiltered(String filter, Pageable pageable);
	
	boolean existsByName(String name);
	

}
