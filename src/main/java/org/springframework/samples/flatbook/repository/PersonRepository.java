
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Person;

public interface PersonRepository {

	Collection<Person> findAll() throws DataAccessException;

	Person findByUsername(String username) throws DataAccessException;

	Person findByDni(String dni) throws DataAccessException;

	void save(Person flat) throws DataAccessException;

	Person findByEmail(String email) throws DataAccessException;
}
