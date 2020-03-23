package org.springframework.samples.flatbook.repository.springdatajpa;

import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.TennantReview;
import org.springframework.samples.flatbook.repository.TennantReviewRepository;

public interface SpringDataTennantReviewRepository extends TennantReviewRepository, Repository<TennantReview, Integer> {
	
	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM Review WHERE id = ?1")
	void removeTennantReviewById(int tennantReviewId) throws DataAccessException;
	
}
