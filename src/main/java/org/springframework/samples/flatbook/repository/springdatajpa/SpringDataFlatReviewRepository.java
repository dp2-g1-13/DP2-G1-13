package org.springframework.samples.flatbook.repository.springdatajpa;


import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.repository.FlatReviewRepository;

public interface SpringDataFlatReviewRepository extends FlatReviewRepository, Repository<FlatReview, Integer> {
	
	@Override
	@Query("SELECT f FROM Flat f JOIN f.flatReviews fr WHERE fr.id = ?1")
	Flat findFlatOfFlatReviewById(int flatReviewId) throws DataAccessException;
	
}
