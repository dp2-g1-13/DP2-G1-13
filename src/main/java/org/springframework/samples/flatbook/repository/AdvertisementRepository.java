package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Advertisement;

import java.util.Set;

public interface AdvertisementRepository {

    Advertisement findById(int id) throws DataAccessException;

    Boolean isAdvertisementWithFlatId(int flatId) throws DataAccessException;

    Set<Advertisement> findByCity(String city) throws DataAccessException;

    Set<Advertisement> findByCityAndPostalCode(String city, Integer postalCode) throws DataAccessException;

    Set<Advertisement> findByCityAndCountry(String city, String country) throws DataAccessException;

    Set<Advertisement> findByCityAndCountryAndPostalCode(String city, String country, Integer postalCode) throws DataAccessException;

    Set<Advertisement> findByPricePerMonthLessThan(String city, double pricePerMonth) throws DataAccessException;

    void save(Advertisement advertisement);

    void delete(Advertisement advertisement);
}
