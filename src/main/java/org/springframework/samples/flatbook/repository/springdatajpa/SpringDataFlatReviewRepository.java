package org.springframework.samples.flatbook.repository.springdatajpa;


import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.repository.FlatReviewRepository;

public interface SpringDataFlatReviewRepository extends FlatReviewRepository, Repository<FlatReview, Integer> {
	
}
