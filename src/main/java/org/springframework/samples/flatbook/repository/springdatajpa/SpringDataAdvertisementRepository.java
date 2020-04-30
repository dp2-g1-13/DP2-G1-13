
package org.springframework.samples.flatbook.repository.springdatajpa;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;

public interface SpringDataAdvertisementRepository extends AdvertisementRepository, Repository<Advertisement, Integer> {

	@Override
	@Query("SELECT a FROM Host h join h.flats f join Advertisement a ON f.id = a.flat.id WHERE h.username = :host")
	Set<Advertisement> findByHost(@Param("host") String host) throws DataAccessException;

	@Override
	@Query("SELECT CASE WHEN count(adv) > 0 THEN true ELSE false END FROM Advertisement adv WHERE adv.flat.id = :flat_id")
	Boolean isAdvertisementWithFlatId(@Param("flat_id") int flatId) throws DataAccessException;

	@Override
	@Query("SELECT adv FROM Advertisement adv WHERE adv.flat.id = :flat_id")
	Advertisement findAdvertisementWithFlatId(@Param("flat_id") int flatId) throws DataAccessException;

	@Override
	@Query("SELECT count(f) FROM Advertisement f")
	Integer numberOfAdvertisements() throws DataAccessException;
}
