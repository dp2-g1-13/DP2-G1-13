package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.repository.RequestRepository;

import java.util.Set;

public interface SpringDataRequestRepository extends RequestRepository, Repository<Request, Integer> {

    @Override
    @Query("SELECT a.requests FROM Advertisement a WHERE a.id = :adv_id")
    Set<Request> findManyByAdvertisementId(@Param("adv_id") int advId) throws DataAccessException;

    @Override
    @Query("SELECT t.requests FROM Tennant t WHERE t.username = :username")
    Set<Request> findManyByTenantUsername(@Param("username") String username) throws DataAccessException;

    @Override
    @Query("SELECT CASE WHEN count(req) > 0 THEN true ELSE false END FROM Tennant tenant JOIN tenant.requests req WHERE tenant.username = :username AND req IN(SELECT r FROM Advertisement adv JOIN adv.requests r WHERE adv.id = :adv_id)")
    Boolean isThereRequestOfTenantByAdvertisementId(@Param("username")String tenantUser, @Param("adv_id")int advId) throws DataAccessException;
}
