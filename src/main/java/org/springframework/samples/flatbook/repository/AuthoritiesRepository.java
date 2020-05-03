
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Authorities;

public interface AuthoritiesRepository {

	Collection<Authorities> findAll();

	Authorities findById(String username);

	void save(Authorities authorities);

}
