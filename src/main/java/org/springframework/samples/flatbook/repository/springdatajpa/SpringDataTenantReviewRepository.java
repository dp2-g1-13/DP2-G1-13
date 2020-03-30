package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.repository.TenantReviewRepository;

public interface SpringDataTenantReviewRepository extends TenantReviewRepository, Repository<TenantReview, Integer> {

}
