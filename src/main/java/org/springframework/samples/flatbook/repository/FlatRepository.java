package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tenant;

import java.util.Collection;
import java.util.Set;

public interface FlatRepository {

    Set<Flat> findAll() throws DataAccessException;

    Flat findById(int id) throws DataAccessException;

    Set<Flat> findByHostUsername(String username) throws DataAccessException;

    Collection<Tenant> findTenantsById(int id) throws DataAccessException;

    void save(Flat flat) throws DataAccessException;
}
