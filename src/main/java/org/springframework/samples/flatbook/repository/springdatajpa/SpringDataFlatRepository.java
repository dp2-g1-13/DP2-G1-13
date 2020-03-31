package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.FlatRepository;

import java.util.Collection;
import java.util.Set;

public interface SpringDataFlatRepository extends FlatRepository, Repository<Flat, Integer> {

    @Override
    @Query("SELECT host.flats FROM Host host WHERE host.username = :hostUsername")
    Set<Flat> findByHostUsername(@Param("hostUsername") String username);


    @Override
    @Query("SELECT tenants FROM Flat WHERE id = ?1")
    Collection<Tenant> findTenantsById(int id) throws DataAccessException;
}
