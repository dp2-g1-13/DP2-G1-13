
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.repository.TennantRepository;

public interface SpringDataTennantRepository extends TennantRepository, Repository<Tennant, String> {

    @Override
    @Query("SELECT tenant FROM Tennant tenant JOIN tenant.requests req WHERE req.id = :request_id")
    Tennant findByRequestId(@Param("request_id")int requestId) throws DataAccessException;
}
