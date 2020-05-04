package org.springframework.samples.flatbook.repository;


import org.springframework.samples.flatbook.model.DBImage;

import java.util.Set;

public interface DBImageRepository {

    DBImage findById(int id);

    void save(DBImage image);

    void delete(DBImage image);

    Set<DBImage> findManyByFlatId(int id);
}
