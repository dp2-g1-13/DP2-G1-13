
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.flatbook.model.Tenant;

public interface TenantRepository {

	Collection<Tenant> findAll();

	Tenant findByUsername(String username);

    Tenant findByUsernameWithFlat(String username);

    Tenant findByUsernameWithFlatAndTenantList(String username);

	Tenant findByRequestId(int requestId);

	void save(Tenant tenant);

	Tenant findByReviewId(int reviewId);

	Page<Tenant> topBestReviewedTenants(Pageable pageable);

	Page<Tenant> topWorstReviewedTenants(Pageable pageable);
}
