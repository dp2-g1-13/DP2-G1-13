
package org.springframework.samples.flatbook.repository;

import java.util.Collection;
import java.util.Set;


import org.springframework.samples.flatbook.model.Task;

public interface TaskRepository {

	Collection<Task> findAll();

	Task findById(int id);

	void deleteById(int id);

	void save(Task task);

	Set<Task> findByFlatId(int id);

	Set<Task> findByParticipant(String username);

}
