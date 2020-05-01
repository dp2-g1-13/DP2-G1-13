
package org.springframework.samples.flatbook.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Task;

public interface TaskRepository {

	Collection<Task> findAll() throws DataAccessException;

	Task findById(int id) throws DataAccessException;

	void deleteById(int id) throws DataAccessException;

	void save(Task task) throws DataAccessException;

	Set<Task> findByFlatId(int id) throws DataAccessException;

	Set<Task> findByParticipant(String username) throws DataAccessException;

}
