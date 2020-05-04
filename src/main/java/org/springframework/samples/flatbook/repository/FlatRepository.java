
package org.springframework.samples.flatbook.repository;

import java.util.Set;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.flatbook.model.Flat;

public interface FlatRepository {

	Set<Flat> findAll();

	Flat findById(int id);

	Set<Flat> findByHostUsername(String username);

	void save(Flat flat);

    void delete(Flat flat);
    void deleteById(int id);

	Flat findByReviewId(Integer reviewId);

	Page<Flat> topBestReviewedFlats(Pageable pageable);

	Page<Flat> topWorstReviewedFlats(Pageable pageable);

	Integer numberOfFlats();

	Flat findFlatWithRequestId(int requestId);

}
