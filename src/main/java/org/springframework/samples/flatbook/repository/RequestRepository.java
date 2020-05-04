
package org.springframework.samples.flatbook.repository;

import java.util.Set;


import org.springframework.samples.flatbook.model.Request;

public interface RequestRepository {

	Request findById(int id);

	Set<Request> findManyByTenantUsername(String username);

	Boolean isThereRequestOfTenantByFlatId(String tenantUser, int advId);

	void save(Request request);

	void delete(Request request);

	Double ratioOfAcceptedRequests();

	Double ratioOfRejectedRequests();

	Double ratioOfCanceledRequests();

	Double ratioOfFinishedRequests();

	Integer numberOfRequests();

}
