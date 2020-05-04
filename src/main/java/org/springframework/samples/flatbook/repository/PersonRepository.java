
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.flatbook.model.Person;

public interface PersonRepository {

	Collection<Person> findAll();

	Person findByUsername(String username);

	Person findByDni(String dni);

	void save(Person person);

	Person findByEmail(String email);

	Page<Person> topMostReportedUsers(Pageable pageableRequest);

	Integer numberOfUsers();
}
