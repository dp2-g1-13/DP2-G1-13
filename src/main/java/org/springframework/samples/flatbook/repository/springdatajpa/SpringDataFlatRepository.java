package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.repository.FlatRepository;

import java.util.Collection;

public interface SpringDataFlatRepository extends FlatRepository, Repository<Flat, Integer> {

//    @Override
//    @Query("SELECT flat FROM Flat flat JOIN flat.host h WHERE h.id = :hostId")
//    Collection<Flat> findByHostId(@Param("hostId") int hostId);

    @Override
    @Query("SELECT flat FROM Flat flat JOIN flat.address a WHERE a.city = :city")
    Collection<Flat> findByCity(@Param("city") String city);

    @Override
    @Query("SELECT flat FROM Flat flat JOIN flat.address a WHERE a.city = :city AND a.postalCode = :postalCode")
    Collection<Flat> findByCityAndPostalCode(@Param("city") String city, @Param("postalCode") Integer postalCode);
}
