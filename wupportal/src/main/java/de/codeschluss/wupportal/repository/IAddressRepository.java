package de.codeschluss.wupportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.codeschluss.wupportal.model.Address;

public interface IAddressRepository extends JpaRepository<Address, String> {
	

}
