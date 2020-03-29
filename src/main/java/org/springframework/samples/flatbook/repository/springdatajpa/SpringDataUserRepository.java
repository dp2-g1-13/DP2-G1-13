
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.User;
import org.springframework.samples.flatbook.repository.UserRepository;

public interface SpringDataUserRepository extends UserRepository, Repository<User, String> {

}
