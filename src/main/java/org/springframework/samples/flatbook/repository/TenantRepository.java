
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.flatbook.model.Tenant;

public interface TenantRepository {

	Collection<Tenant> findAll() throws DataAccessException;

	Tenant findByUsername(String username) throws DataAccessException;

	Tenant findByRequestId(int requestId) throws DataAccessException;

	void save(Tenant tenant) throws DataAccessException;

	Tenant findByReviewId(int reviewId) throws DataAccessException;

	Page<Tenant> topBestReviewedTenants(Pageable pageable) throws DataAccessException;

	Page<Tenant> topWorstReviewedTenants(Pageable pageable) throws DataAccessException;
}
