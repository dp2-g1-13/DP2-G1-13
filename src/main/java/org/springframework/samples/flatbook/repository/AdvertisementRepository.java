
package org.springframework.samples.flatbook.repository;

import java.util.Set;


import org.springframework.samples.flatbook.model.Advertisement;

public interface AdvertisementRepository {

	Set<Advertisement> findAll();

	Advertisement findById(int id);

	Boolean isAdvertisementWithFlatId(int flatId);

	Advertisement findAdvertisementWithFlatId(int flatId);

	void save(Advertisement advertisement);

	void delete(Advertisement advertisement);

	Set<Advertisement> findByHost(String host);

	Integer numberOfAdvertisements();
}
