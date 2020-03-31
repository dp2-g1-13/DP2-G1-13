
package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Authorities;

public interface AuthoritiesRepository {

	public Authorities findById(String username) throws DataAccessException;

	public void save(Authorities authorities) throws DataAccessException;

}
