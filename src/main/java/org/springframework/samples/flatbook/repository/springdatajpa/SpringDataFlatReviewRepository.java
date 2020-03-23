package org.springframework.samples.flatbook.repository.springdatajpa;

import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.repository.FlatReviewRepository;

public interface SpringDataFlatReviewRepository extends FlatReviewRepository, Repository<FlatReview, Integer> {
	
	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM Review WHERE id = ?1")
	void removeFlatReviewById(int flatReviewId) throws DataAccessException;
	
}
