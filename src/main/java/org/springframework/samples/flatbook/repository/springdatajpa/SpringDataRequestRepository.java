
package org.springframework.samples.flatbook.repository.springdatajpa;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.repository.RequestRepository;

public interface SpringDataRequestRepository extends RequestRepository, Repository<Request, Integer> {

	@Override
	@Query("SELECT t.requests FROM Tenant t WHERE t.username = :username")
	Set<Request> findManyByTenantUsername(@Param("username") String username);

	@Override
	@Query("SELECT CASE WHEN count(req) > 0 THEN true ELSE false END FROM Tenant tenant JOIN tenant.requests req WHERE tenant.username = :username AND req.status = 'PENDING' AND req IN(SELECT r FROM Flat adv JOIN adv.requests r WHERE adv.id = :flat_id)")
	Boolean isThereRequestOfTenantByFlatId(@Param("username") String tenantUser, @Param("flat_id") int flatId);

	@Override
	@Query("SELECT count(r) FROM Request r")
	Integer numberOfRequests();

	@Override
	@Query("SELECT 1.0*count(r)/(select count(rq) FROM Request rq) from Request r where r.status='ACCEPTED'")
	Double ratioOfAcceptedRequests();

	@Override
	@Query("SELECT 1.0*count(r)/(select count(rq) FROM Request rq) from Request r where r.status='REJECTED'")
	Double ratioOfRejectedRequests();

	@Override
	@Query("SELECT 1.0*count(r)/(select count(rq) FROM Request rq) from Request r where r.status='CANCELED'")
	Double ratioOfCanceledRequests();

	@Override
	@Query("SELECT 1.0*count(r)/(select count(rq) FROM Request rq) from Request r where r.status='FINISHED'")
	Double ratioOfFinishedRequests();
}
