
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.TenantRepository;

public interface SpringDataTenantRepository extends TenantRepository, Repository<Tenant, String> {

	@Override
	@Query("SELECT tenant FROM Tenant tenant JOIN tenant.requests req WHERE req.id = :request_id")
	Tenant findByRequestId(@Param("request_id") int requestId);

	@Override
	@Query("SELECT tenant FROM Tenant tenant JOIN tenant.reviews r WHERE r.id = ?1")
	Tenant findByReviewId(int reviewId);

	@Override
	@Query("SELECT t FROM Tenant t join t.reviews r where t.enabled=true group by t order by avg(r.rate) desc, t.username asc")
	Page<Tenant> topBestReviewedTenants(Pageable pageable);

	@Override
	@Query("SELECT t FROM Tenant t join t.reviews r where t.enabled=true group by t order by avg(r.rate) asc, t.username asc")
	Page<Tenant> topWorstReviewedTenants(Pageable pageable);
}
