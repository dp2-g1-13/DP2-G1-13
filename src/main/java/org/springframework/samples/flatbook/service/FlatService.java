
package org.springframework.samples.flatbook.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.AddressRepository;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.samples.flatbook.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlatService {

	private FlatRepository			flatRepository;

	private DBImageRepository		dbImageRepository;

	private AddressRepository		addressRepository;

	private TenantRepository		tenantRepository;

	private AdvertisementRepository	advertisementRepository;

	private RequestRepository		requestRepository;

	private HostRepository			hostRepository;


	@Autowired
	public FlatService(final FlatRepository flatRepository, final DBImageRepository dbImageRepository, final AddressRepository addressRepository, final TenantRepository tenantRepository, final AdvertisementRepository advertisementRepository,
		final RequestRepository requestRepository, final HostRepository hostRepository) {
		this.flatRepository = flatRepository;
		this.dbImageRepository = dbImageRepository;
		this.addressRepository = addressRepository;
		this.tenantRepository = tenantRepository;
		this.advertisementRepository = advertisementRepository;
		this.requestRepository = requestRepository;
		this.hostRepository = hostRepository;
	}

	@Transactional(readOnly = true)
	public Flat findFlatById(final int flatId) {
		return this.flatRepository.findById(flatId);
	}

	@Transactional(readOnly = true)
	public Set<Flat> findAllFlats() {
		return this.flatRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Set<Flat> findFlatByHostUsername(final String username) {
		return this.flatRepository.findByHostUsername(username);
	}

	@Transactional
	public void saveFlat(final Flat flat) {
		this.flatRepository.save(flat);
		for (DBImage image : flat.getImages()) {
			this.dbImageRepository.save(image);
		}
	}

	@Transactional
	public void deleteFlat(final Flat flat) {
		for (Tenant tenant : flat.getTenants()) {
			tenant.setFlat(null);
			this.tenantRepository.save(tenant);
		}
		flat.setTenants(null);

		Advertisement adv = this.advertisementRepository.findAdvertisementWithFlatId(flat.getId());
		if (adv != null) {
			this.advertisementRepository.delete(adv);
		}

		for (Request request : flat.getRequests()) {
			Tenant tenant = this.tenantRepository.findByRequestId(request.getId());
			tenant.getRequests().remove(request);
			this.requestRepository.delete(request);
		}
		flat.setRequests(null);

		Host host = this.hostRepository.findByFlatId(flat.getId());
		host.getFlats().remove(flat);
		this.hostRepository.save(host);

		this.flatRepository.delete(flat);
		this.addressRepository.deleteById(flat.getAddress().getId());
	}

	public Flat findFlatByReviewId(final Integer reviewId) {
		return this.flatRepository.findByReviewId(reviewId);
	}

	public Flat findFlatWithRequestId(final int requestId) {
		return this.flatRepository.findFlatWithRequestId(requestId);
	}

}
