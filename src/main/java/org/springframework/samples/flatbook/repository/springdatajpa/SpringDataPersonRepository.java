
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.repository.PersonRepository;

public interface SpringDataPersonRepository extends PersonRepository, Repository<Person, String> {

	@Override
	@Query("SELECT p FROM Person p join Report r on r.receiver=p where p.enabled=true group by p order by count(r.id) desc")
	Page<Person> topMostReportedUsers(Pageable pageableRequest) throws DataAccessException;

	@Override
	@Query("SELECT count(f) FROM Person f")
	Integer numberOfUsers() throws DataAccessException;
}
