
package org.springframework.samples.flatbook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.flatbook.model.Person;

public interface UserRepository extends CrudRepository<Person, String> {

}
