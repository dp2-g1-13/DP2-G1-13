
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.flatbook.model.Person;

public interface PersonRepository {

	Collection<Person> findAll() throws DataAccessException;

	Person findByUsername(String username) throws DataAccessException;

	Person findByDni(String dni) throws DataAccessException;

	void save(Person person) throws DataAccessException;

	Person findByEmail(String email) throws DataAccessException;

	Page<Person> topMostReportedUsers(Pageable pageableRequest) throws DataAccessException;

	Integer numberOfUsers() throws DataAccessException;
}
