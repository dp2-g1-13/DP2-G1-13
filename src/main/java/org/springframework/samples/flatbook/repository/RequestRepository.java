package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Request;

import java.util.Set;

public interface RequestRepository {

    Request findById(int id) throws DataAccessException;

    Set<Request> findManyByAdvertisementId(int advId) throws DataAccessException;

    Set<Request> findManyByTenantUsername(String username) throws DataAccessException;

    Boolean isThereRequestOfTenantByAdvertisementId(String tenantUser, int advId) throws DataAccessException;

    void save(Request request) throws DataAccessException;

}
