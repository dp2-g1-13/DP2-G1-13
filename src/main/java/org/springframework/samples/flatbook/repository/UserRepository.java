package org.springframework.samples.flatbook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.flatbook.model.User;


public interface UserRepository extends  CrudRepository<User, String>{

}
