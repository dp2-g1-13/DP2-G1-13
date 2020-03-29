package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tennant;

import java.util.Collection;

public interface FlatRepository {

    Collection<Flat> findAll() throws DataAccessException;

    Flat findById(int id) throws DataAccessException;

//    Collection<Flat> findByHostId(int hostId) throws DataAccessException;

    Collection<Tennant> findTennantsById(int id) throws DataAccessException;
    
    Collection<Flat> findByCity(String city) throws DataAccessException;

    Collection<Flat> findByCityAndPostalCode(String city, Integer postalCode) throws DataAccessException;

    void save(Flat flat) throws DataAccessException;

}
