
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Authorities;

public interface AuthoritiesRepository {

	Collection<Authorities> findAll() throws DataAccessException;

	Authorities findById(String username) throws DataAccessException;

	void save(Authorities authorities) throws DataAccessException;

}
