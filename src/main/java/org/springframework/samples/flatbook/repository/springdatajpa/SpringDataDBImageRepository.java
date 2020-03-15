package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.repository.DBImageRepository;

import java.util.Collection;

public interface SpringDataDBImageRepository extends DBImageRepository, Repository<DBImage, Integer> {

    @Override
    @Query("SELECT image FROM DBImage image WHERE image.flat.id = :id")
    Collection<DBImage> findManyByFlatId(@Param("id") int id) throws DataAccessException;

}
