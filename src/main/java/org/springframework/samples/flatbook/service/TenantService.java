
package org.springframework.samples.flatbook.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantService {

	private TenantRepository tenantRepository;


	@Autowired
	public TenantService(final TenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
	}

	@Transactional(readOnly = true)
	public Tenant findTenantById(final String username) {
		return this.tenantRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Collection<Tenant> findAllTenants() {
		return this.tenantRepository.findAll();
	}

	@Transactional
	public void saveTenant(final Tenant tenant) {
		this.tenantRepository.save(tenant);
	}

	@Transactional(readOnly = true)
	public Tenant findTenantByRequestId(final int requestId) {
		return this.tenantRepository.findByRequestId(requestId);
	}

	@Transactional(readOnly = true)
	public Tenant findTenantByReviewId(final int reviewId) {
		return this.tenantRepository.findByReviewId(reviewId);
	}

}
