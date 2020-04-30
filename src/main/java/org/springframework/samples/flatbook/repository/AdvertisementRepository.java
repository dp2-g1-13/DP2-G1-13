
package org.springframework.samples.flatbook.repository;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Advertisement;

public interface AdvertisementRepository {

    Set<Advertisement> findAll() throws DataAccessException;

	Advertisement findById(int id) throws DataAccessException;

	Boolean isAdvertisementWithFlatId(int flatId) throws DataAccessException;

	Advertisement findAdvertisementWithFlatId(int flatId) throws DataAccessException;

	Advertisement findAdvertisementWithRequestId(int requestId) throws DataAccessException;

	void save(Advertisement advertisement);

	void delete(Advertisement advertisement);

	Set<Advertisement> findByHost(String host) throws DataAccessException;

	Integer numberOfAdvertisements() throws DataAccessException;
}
