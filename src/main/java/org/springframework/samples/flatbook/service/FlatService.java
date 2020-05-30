
package org.springframework.samples.flatbook.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.repository.*;
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

	private TaskRepository          taskRepository;

	private FlatReviewRepository    flatReviewRepository;


	@Autowired
	public FlatService(final FlatRepository flatRepository, final DBImageRepository dbImageRepository, final AddressRepository addressRepository, final TenantRepository tenantRepository, final AdvertisementRepository advertisementRepository,
		final RequestRepository requestRepository, final HostRepository hostRepository, final TaskRepository taskRepository, final FlatReviewRepository flatReviewRepository) {
		this.flatRepository = flatRepository;
		this.dbImageRepository = dbImageRepository;
		this.addressRepository = addressRepository;
		this.tenantRepository = tenantRepository;
		this.advertisementRepository = advertisementRepository;
		this.requestRepository = requestRepository;
		this.hostRepository = hostRepository;
		this.taskRepository = taskRepository;
		this.flatReviewRepository = flatReviewRepository;
	}

	@Transactional(readOnly = true)
	public Flat findFlatById(final int flatId) {
		return this.flatRepository.findById(flatId);
	}

    @Transactional(readOnly = true)
    public Flat findFlatByIdWithFullData(final int flatId) {
	    return this.flatRepository.findByIdWithFullData(flatId);
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
        Set<Task> tasks = this.taskRepository.findByFlatId(flat.getId());
        for(Task task : tasks) {
            this.taskRepository.deleteById(task.getId());
        }

		for (Tenant tenant : flat.getTenants()) {
			tenant.setFlat(null);
			this.tenantRepository.save(tenant);
		}
		flat.setTenants(null);

		flat.getImages().forEach(x -> this.dbImageRepository.delete(x));

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

		flat.getFlatReviews().forEach(x -> this.flatReviewRepository.deleteById(x.getId()));
		flat.setFlatReviews(null);

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
