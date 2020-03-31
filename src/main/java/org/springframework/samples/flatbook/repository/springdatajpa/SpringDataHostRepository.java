
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.repository.HostRepository;

public interface SpringDataHostRepository extends HostRepository, Repository<Host, String> {

    @Override
    @Query("SELECT host FROM Host host JOIN host.flats f WHERE f.id = :flat_id")
    Host findByFlatId(@Param("flat_id") int flatId) throws DataAccessException;

    @Override
    @Query("SELECT h FROM Host h JOIN h.flats f JOIN Advertisement a ON f.id = a.flat.id JOIN a.requests req WHERE req.id = :request_id")
    Host findHostOfAdvertisementByRequestId(@Param("request_id") int requestId) throws DataAccessException;
}
