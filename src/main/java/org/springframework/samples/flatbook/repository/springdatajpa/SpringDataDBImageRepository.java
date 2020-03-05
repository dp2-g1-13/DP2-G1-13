package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.repository.DBImageRepository;

public interface SpringDataDBImageRepository extends DBImageRepository, Repository<DBImage, Integer> {

}
