package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.TennantReview;

import java.util.Collection;

public interface TennantReviewRepository {

    Collection<TennantReview> findAll() throws DataAccessException;

    TennantReview findById(int id) throws DataAccessException;
    
    void removeTennantReviewById(int id) throws DataAccessException;
    
    void save(TennantReview tennantReview) throws DataAccessException;

}
