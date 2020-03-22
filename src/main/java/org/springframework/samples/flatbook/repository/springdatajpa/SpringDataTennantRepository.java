
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.repository.TennantRepository;

public interface SpringDataTennantRepository extends TennantRepository, Repository<Tennant, String> {

}
