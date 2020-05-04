package org.springframework.samples.flatbook.repository;


import org.springframework.samples.flatbook.model.TenantReview;

import java.util.Collection;

public interface TenantReviewRepository {

    Collection<TenantReview> findAll();

    TenantReview findById(int id);

    void deleteById(int id);

    void save(TenantReview tenantReview);
}
