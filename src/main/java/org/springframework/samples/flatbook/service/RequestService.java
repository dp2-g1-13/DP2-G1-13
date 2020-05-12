
package org.springframework.samples.flatbook.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

	private RequestRepository requestRepository;


	@Autowired
	public RequestService(final RequestRepository requestRepository) {
		this.requestRepository = requestRepository;
	}

	@Transactional(readOnly = true)
	public Request findRequestById(final int id) {
		return this.requestRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Set<Request> findRequestsByTenantUsername(final String username) {
		return this.requestRepository.findManyByTenantUsername(username);
	}

	@Transactional(readOnly = true)
	public Boolean isThereRequestOfTenantByFlatId(final String tenantUser, final int advId) {
		return this.requestRepository.isThereRequestOfTenantByFlatId(tenantUser, advId);
	}

	@Transactional
	public void saveRequest(final Request request) {
		this.requestRepository.save(request);
	}
}
