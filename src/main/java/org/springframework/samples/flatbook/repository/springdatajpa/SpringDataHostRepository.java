
package org.springframework.samples.flatbook.repository.springdatajpa;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.repository.HostRepository;

public interface SpringDataHostRepository extends HostRepository, Repository<Host, String> {

	@Override
	@Query("SELECT host FROM Host host JOIN host.flats f WHERE f.id = :flat_id")
	Host findByFlatId(@Param("flat_id") int flatId);

	@Override
	@Query("SELECT h FROM Host h JOIN h.flats f JOIN f.requests req WHERE req.id = :request_id and h.enabled=true")
	Host findHostOfFlatByRequestId(@Param("request_id") int requestId);

	@Override
	@Query("SELECT h FROM Host h join h.flats f join f.flatReviews r where h.enabled=true group by h order by avg(r.rate) desc, h.username asc")
	Page<Host> topBestReviewedHosts(Pageable pageRequest);

	@Override
	@Query("SELECT h FROM Host h join h.flats f join f.flatReviews r where h.enabled=true group by h order by avg(r.rate) asc, h.username asc")
	Page<Host> topWorstReviewedHosts(Pageable pageRequest);
}
