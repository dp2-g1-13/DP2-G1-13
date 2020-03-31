package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Task;

import java.util.Collection;

public interface TaskRepository {

    Collection<Task> findAll() throws DataAccessException;

    Task findById(int id) throws DataAccessException;
    
    void deleteById(int id) throws DataAccessException;
    
    void save(Task task) throws DataAccessException;

}
