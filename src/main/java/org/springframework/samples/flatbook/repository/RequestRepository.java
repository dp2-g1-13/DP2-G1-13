
package org.springframework.samples.flatbook.repository;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Request;

public interface RequestRepository {

	Request findById(int id) throws DataAccessException;

	Set<Request> findManyByTenantUsername(String username) throws DataAccessException;

	Boolean isThereRequestOfTenantByAdvertisementId(String tenantUser, int advId) throws DataAccessException;

	void save(Request request) throws DataAccessException;

	Double ratioOfAcceptedRequests() throws DataAccessException;

	Double ratioOfRejectedRequests() throws DataAccessException;

	Integer numberOfRequests() throws DataAccessException;

}
