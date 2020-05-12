package org.springframework.samples.flatbook.repository.springdatajpa;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.repository.DBImageRepository;

import java.util.Set;

public interface SpringDataDBImageRepository extends DBImageRepository, Repository<DBImage, Integer> {

    @Override
    @Query("SELECT flat.images FROM Flat flat WHERE flat.id = :id")
    Set<DBImage> findManyByFlatId(@Param("id") int id);

}
