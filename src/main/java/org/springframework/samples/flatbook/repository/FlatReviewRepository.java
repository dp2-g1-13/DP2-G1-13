package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.FlatReview;

import java.util.Collection;

public interface FlatReviewRepository {

    Collection<FlatReview> findAll() throws DataAccessException;

    FlatReview findById(int id) throws DataAccessException;
    
    void deleteById(int id) throws DataAccessException;
    
    void save(FlatReview flatReview) throws DataAccessException;
}
