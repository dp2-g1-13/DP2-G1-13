
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.repository.PersonRepository;

public interface SpringDataPersonRepository extends PersonRepository, Repository<Person, String> {

}
