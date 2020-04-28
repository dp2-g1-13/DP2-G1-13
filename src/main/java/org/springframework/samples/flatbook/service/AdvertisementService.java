
package org.springframework.samples.flatbook.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdvertisementService {

	private AdvertisementRepository advertisementRepository;


	@Autowired
	public AdvertisementService(final AdvertisementRepository advertisementRepository) {
		this.advertisementRepository = advertisementRepository;
	}

	@Transactional(readOnly = true)
	public Advertisement findAdvertisementById(final int id) throws DataAccessException {
		return this.advertisementRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Boolean isAdvertisementWithFlatId(final int id) throws DataAccessException {
		return this.advertisementRepository.isAdvertisementWithFlatId(id);
	}

	@Transactional(readOnly = true)
	public Advertisement findAdvertisementWithFlatId(final int id) throws DataAccessException {
		return this.advertisementRepository.findAdvertisementWithFlatId(id);
	}

	@Transactional(readOnly = true)
	public Advertisement findAdvertisementWithRequestId(final int requestId) throws DataAccessException {
		return this.advertisementRepository.findAdvertisementWithRequestId(requestId);
	}

	@Transactional(readOnly = true)
	public Set<Advertisement> findAllAdvertisements() throws DataAccessException {
		return this.advertisementRepository.findAll();
	}

	@Transactional
	public void saveAdvertisement(final Advertisement advertisement) throws DataAccessException {
		this.advertisementRepository.save(advertisement);
	}

	@Transactional
	public void deleteAdvertisement(final Advertisement advertisement) throws DataAccessException {
		this.advertisementRepository.delete(advertisement);
	}

	@Transactional
	public Set<Advertisement> findAdvertisementsByHost(final String host) throws DataAccessException {
		return this.advertisementRepository.findByHost(host);
	}
}
