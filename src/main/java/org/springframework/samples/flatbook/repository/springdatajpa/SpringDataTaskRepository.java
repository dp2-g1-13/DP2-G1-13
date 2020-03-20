package org.springframework.samples.flatbook.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.repository.TaskRepository;

public interface SpringDataTaskRepository extends TaskRepository, Repository<Task, Integer> {

	@Override
    @Query("SELECT tennants FROM Flat WHERE id = ?1")
	Collection<Tennant> findAsigneesByTennantId(int id) throws DataAccessException;
	
}
