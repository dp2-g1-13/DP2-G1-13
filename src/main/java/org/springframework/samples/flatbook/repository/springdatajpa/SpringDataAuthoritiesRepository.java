
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;

public interface SpringDataAuthoritiesRepository extends AuthoritiesRepository, Repository<Authorities, String> {

}
