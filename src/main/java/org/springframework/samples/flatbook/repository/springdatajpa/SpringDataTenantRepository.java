
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.TenantRepository;

public interface SpringDataTenantRepository extends TenantRepository, Repository<Tenant, String> {

    @Override
    @Query("SELECT tenant FROM Tenant tenant JOIN tenant.requests req WHERE req.id = :request_id")
    Tenant findByRequestId(@Param("request_id")int requestId) throws DataAccessException;
}
