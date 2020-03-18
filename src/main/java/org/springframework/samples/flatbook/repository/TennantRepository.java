
package org.springframework.samples.flatbook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.flatbook.model.Tennant;

public interface TennantRepository extends CrudRepository<Tennant, String> {

}
