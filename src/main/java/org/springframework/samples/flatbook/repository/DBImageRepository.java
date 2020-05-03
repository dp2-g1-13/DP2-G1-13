package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.DBImage;

import java.util.Set;

/**
 * Repository class for all DBImage domain objects
 *
 */
public interface DBImageRepository {
    /**
     * Retrieves an Image from the data store by id.
     * @param id the id to search for
     * @return the Image if found
     */
    DBImage findById(int id);

    /**
     * Save an <code>Image</code> to the data store, either inserting or updating it.
     * @param image the Image to save
     */
    void save(DBImage image);

    void delete(DBImage image);

    Set<DBImage> findManyByFlatId(int id);
}
