package org.springframework.samples.flatbook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.flatbook.model.Authorities;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{

}
