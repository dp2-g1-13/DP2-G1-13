
package org.springframework.samples.flatbook.repository;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Advertisement;

public interface AdvertisementRepository {

	Advertisement findById(int id) throws DataAccessException;

	Boolean isAdvertisementWithFlatId(int flatId) throws DataAccessException;

	Advertisement findAdvertisementWithFlatId(int flatId) throws DataAccessException;

	Advertisement findAdvertisementWithRequestId(int requestId) throws DataAccessException;

	Set<Advertisement> findByCity(String city) throws DataAccessException;

	Set<Advertisement> findByCityAndPostalCode(String city, String postalCode) throws DataAccessException;

	Set<Advertisement> findByCityAndCountry(String city, String country) throws DataAccessException;

	Set<Advertisement> findByCityAndCountryAndPostalCode(String city, String country, String postalCode) throws DataAccessException;

	void save(Advertisement advertisement);

	void delete(Advertisement advertisement);

	Set<Advertisement> findByHost(String host) throws DataAccessException;
}
