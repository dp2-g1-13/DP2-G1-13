package org.springframework.samples.flatbook.repository.springdatajpa;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.repository.TaskRepository;

public interface SpringDataTaskRepository extends TaskRepository, Repository<Task, Integer> {
	
	@Override
    @Query("SELECT t FROM Task t WHERE t.creator.username = :username")
    Set<Task> findManyByTenantUsername(@Param("username") String username) throws DataAccessException;
	
}
