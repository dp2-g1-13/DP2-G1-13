
package org.springframework.samples.flatbook.service;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlatService {

	private FlatRepository		flatRepository;

	private DBImageRepository	dbImageRepository;


	@Autowired
	public FlatService(final FlatRepository flatRepository, final DBImageRepository dbImageRepository) {
		this.flatRepository = flatRepository;
		this.dbImageRepository = dbImageRepository;
	}

	@Transactional(readOnly = true)
	public Flat findFlatById(final int flatId) throws DataAccessException {
		return this.flatRepository.findById(flatId);
	}

	@Transactional(readOnly = true)
	public Collection<Tenant> findTenantsById(final int id) throws DataAccessException {
		return this.flatRepository.findTenantsById(id);
	}

	@Transactional(readOnly = true)
	public Set<Flat> findAllFlats() throws DataAccessException {
		return this.flatRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Set<Flat> findFlatByHostUsername(final String username) throws DataAccessException {
		return this.flatRepository.findByHostUsername(username);
	}

	@Transactional
	public void saveFlat(final Flat flat) throws DataAccessException {
		this.flatRepository.save(flat);
		for (DBImage image : flat.getImages()) {
			this.dbImageRepository.save(image);
		}
	}

	public Flat findFlatByReviewId(final Integer reviewId) {
		return this.flatRepository.findByReviewId(reviewId);
	}

	public Flat findFlatWithRequestId(final int requestId) throws DataAccessException {
		return this.flatRepository.findFlatWithRequestId(requestId);
	}

}
