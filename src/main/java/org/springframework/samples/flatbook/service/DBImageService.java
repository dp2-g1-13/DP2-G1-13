
package org.springframework.samples.flatbook.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DBImageService {

	private DBImageRepository dbImageRepository;


	@Autowired
	public DBImageService(final DBImageRepository dbImageRepository) {
		this.dbImageRepository = dbImageRepository;
	}

	@Transactional(readOnly = true)
	public DBImage getImageById(final int imageId) {
		return this.dbImageRepository.findById(imageId);
	}

	@Transactional(readOnly = true)
	public Set<DBImage> getImagesByFlatId(final int flatId) {
		return this.dbImageRepository.findManyByFlatId(flatId);
	}

	@Transactional
	public void deleteImage(final DBImage image) throws DataAccessException {
		this.dbImageRepository.delete(image);
	}

}
