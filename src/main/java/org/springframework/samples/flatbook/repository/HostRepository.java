
package org.springframework.samples.flatbook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.flatbook.model.Host;

public interface HostRepository extends CrudRepository<Host, String> {

}
