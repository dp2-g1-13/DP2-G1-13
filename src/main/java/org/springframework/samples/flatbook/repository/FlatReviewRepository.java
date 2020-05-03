package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.FlatReview;

import java.util.Collection;

public interface FlatReviewRepository {

    Collection<FlatReview> findAll();

    FlatReview findById(int id);

    void deleteById(int id);

    void save(FlatReview flatReview);
}
