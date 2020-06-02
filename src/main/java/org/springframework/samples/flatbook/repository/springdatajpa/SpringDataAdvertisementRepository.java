
package org.springframework.samples.flatbook.repository.springdatajpa;

import java.util.Set;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;

public interface SpringDataAdvertisementRepository extends AdvertisementRepository, Repository<Advertisement, Integer> {

    @Override
    @Query("SELECT DISTINCT a FROM Host h JOIN h.flats f JOIN Advertisement a ON f.id = a.flat.id LEFT JOIN FETCH a.flat flat LEFT JOIN FETCH flat.address ad WHERE h.enabled = TRUE")
    Set<Advertisement> findAllOfEnabledHosts();

    @Override
    @Query("SELECT DISTINCT a FROM Advertisement a LEFT JOIN FETCH a.flat f LEFT JOIN FETCH f.address add LEFT JOIN FETCH f.images i LEFT JOIN FETCH f.flatReviews fr LEFT JOIN FETCH f.tenants t WHERE a.id = :id")
    Advertisement findByIdWithFullFlatData(@Param("id")int id);

    @Override
	@Query("SELECT a FROM Host h join h.flats f join Advertisement a ON f.id = a.flat.id WHERE h.username = :host")
	Set<Advertisement> findByHost(@Param("host") String host);

	@Override
	@Query("SELECT CASE WHEN count(adv) > 0 THEN true ELSE false END FROM Advertisement adv WHERE adv.flat.id = :flat_id")
	Boolean isAdvertisementWithFlatId(@Param("flat_id") int flatId);

	@Override
	@Query("SELECT adv FROM Advertisement adv WHERE adv.flat.id = :flat_id")
	Advertisement findAdvertisementWithFlatId(@Param("flat_id") int flatId);

	@Override
	@Query("SELECT count(f) FROM Advertisement f")
	Integer numberOfAdvertisements();
}
