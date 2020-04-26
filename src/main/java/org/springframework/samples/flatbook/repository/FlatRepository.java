
package org.springframework.samples.flatbook.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tenant;

public interface FlatRepository {

	Set<Flat> findAll() throws DataAccessException;

	Flat findById(int id) throws DataAccessException;

	Set<Flat> findByHostUsername(String username) throws DataAccessException;

	Collection<Tenant> findTenantsById(int id) throws DataAccessException;

	void save(Flat flat) throws DataAccessException;

	Flat findByReviewId(Integer reviewId) throws DataAccessException;
}
