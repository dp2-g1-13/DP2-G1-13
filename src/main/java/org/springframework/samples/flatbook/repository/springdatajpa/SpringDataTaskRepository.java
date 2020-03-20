package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.repository.TaskRepository;

public interface SpringDataTaskRepository extends TaskRepository, Repository<Task, Integer> {
	
}
