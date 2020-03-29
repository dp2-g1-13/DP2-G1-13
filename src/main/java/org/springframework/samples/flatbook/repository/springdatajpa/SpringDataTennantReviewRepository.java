package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.TennantReview;
import org.springframework.samples.flatbook.repository.TennantReviewRepository;

public interface SpringDataTennantReviewRepository extends TennantReviewRepository, Repository<TennantReview, Integer> {

}
