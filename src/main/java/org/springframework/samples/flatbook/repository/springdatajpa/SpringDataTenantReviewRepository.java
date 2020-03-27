package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.repository.TenantReviewRepository;

public interface SpringDataTenantReviewRepository extends TenantReviewRepository, Repository<TenantReview, Integer> {
	
	@Override
	@Query("SELECT t FROM Tenant t JOIN t.reviews r WHERE r.id = ?1")
	Tenant findTenantOfTenantReviewById(int tenantReviewId) throws DataAccessException;
	
}
