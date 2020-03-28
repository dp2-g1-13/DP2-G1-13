package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;

import java.util.Set;

public interface SpringDataAdvertisementRepository extends AdvertisementRepository, Repository<Advertisement, Integer> {

    @Override
    @Query("SELECT CASE WHEN count(adv) > 0 THEN true ELSE false END FROM Advertisement adv WHERE adv.flat.id = :flat_id")
    Boolean isAdvertisementWithFlatId(@Param("flat_id") int flatId) throws DataAccessException;

    @Override
    @Query("SELECT adv FROM Advertisement adv WHERE adv.flat.id = :flat_id")
    Advertisement findAdvertisementWithFlatId(@Param("flat_id") int flatId) throws DataAccessException;

    @Override
    @Query("SELECT adv FROM Advertisement adv JOIN adv.requests req WHERE req.id = :request_id")
    Advertisement findAdvertisementWithRequestId(@Param("request_id") int requestId) throws DataAccessException;

    @Override
    @Query("SELECT adv FROM Advertisement adv WHERE adv.flat.address.city = :city")
    Set<Advertisement> findByCity(@Param("city") String city) throws DataAccessException;

    @Override
    @Query("SELECT adv FROM Advertisement adv WHERE adv.flat.address.city = :city AND adv.flat.address.postalCode = :postal_code")
    Set<Advertisement> findByCityAndPostalCode(@Param("city") String city, @Param("postal_code") String postalCode) throws DataAccessException;

    @Override
    @Query("SELECT adv FROM Advertisement adv WHERE adv.flat.address.city = :city AND adv.pricePerMonth < :price_per_month")
    Set<Advertisement> findByPricePerMonthLessThan(@Param("city") String city, @Param("price_per_month") double pricePerMonth) throws DataAccessException;

    @Override
    @Query("SELECT adv FROM Advertisement adv WHERE adv.flat.address.city = :city AND adv.flat.address.country = :country")
    Set<Advertisement> findByCityAndCountry(@Param("city") String city, @Param("country") String country) throws DataAccessException;

    @Override
    @Query("SELECT adv FROM Advertisement adv WHERE adv.flat.address.city = :city AND adv.flat.address.country = :country AND adv.flat.address.postalCode = :postal_code")
    Set<Advertisement> findByCityAndCountryAndPostalCode(@Param("city")String city, @Param("country") String country, @Param("postal_code") String postalCode) throws DataAccessException;
}
