package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.model.TennantReview;
import org.springframework.samples.flatbook.repository.TennantReviewRepository;

public interface SpringDataTennantReviewRepository extends TennantReviewRepository, Repository<TennantReview, Integer> {
	
	@Override
	@Query("SELECT t FROM Tennant t JOIN t.reviews r WHERE r.id = ?1")
	Tennant findTennantOfTennantReviewById(int tennantReviewId) throws DataAccessException;
	
}
