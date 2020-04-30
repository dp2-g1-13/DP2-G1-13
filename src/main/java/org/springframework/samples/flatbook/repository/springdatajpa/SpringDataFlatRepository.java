
package org.springframework.samples.flatbook.repository.springdatajpa;

import java.util.Collection;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.FlatRepository;

public interface SpringDataFlatRepository extends FlatRepository, Repository<Flat, Integer> {

	@Override
	@Query("SELECT host.flats FROM Host host WHERE host.username = :hostUsername")
	Set<Flat> findByHostUsername(@Param("hostUsername") String username);

	@Override
	@Query("SELECT tenants FROM Flat WHERE id = ?1")
	Collection<Tenant> findTenantsById(int id) throws DataAccessException;

	@Override
	@Query("SELECT f FROM Flat f join f.flatReviews fr WHERE fr.id = ?1")
	Flat findByReviewId(Integer reviewId) throws DataAccessException;

	@Override
	@Query("SELECT f FROM Flat f join f.flatReviews r group by f order by avg(r.rate) desc")
	Page<Flat> topBestReviewedFlats(Pageable pageable) throws DataAccessException;

	@Override
	@Query("SELECT f FROM Flat f join f.flatReviews r group by f order by avg(r.rate) asc")
	Page<Flat> topWorstReviewedFlats(Pageable pageable) throws DataAccessException;

	@Override
	@Query("SELECT count(f) FROM Flat f")
	Integer numberOfFlats() throws DataAccessException;

	@Override
	@Query("SELECT 1.0*count(*)/(select count(fl) from Flat fl) FROM Advertisement a join Flat f on a.flat=f")
	Double ratioOfFlatsWithAdvertisement() throws DataAccessException;

	@Override
	@Query("SELECT adv FROM Flat adv JOIN adv.requests req WHERE req.id = :request_id")
	Flat findFlatWithRequestId(@Param("request_id") int requestId) throws DataAccessException;
}
