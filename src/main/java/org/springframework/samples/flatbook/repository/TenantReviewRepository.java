package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.TenantReview;

import java.util.Collection;

public interface TenantReviewRepository {

    Collection<TenantReview> findAll() throws DataAccessException;

    TenantReview findById(int id) throws DataAccessException;
    
    void deleteById(int id) throws DataAccessException;
    
    void save(TenantReview tenantReview) throws DataAccessException;
    
    Tenant findTenantOfTenantReviewById(int tenantReviewId) throws DataAccessException;

}
