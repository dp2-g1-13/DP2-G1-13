
package org.springframework.samples.flatbook.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

	private TaskRepository taskRepository;


	@Autowired
	public TaskService(final TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Transactional(readOnly = true)
	public Task findTaskById(final int taskId) {
		return this.taskRepository.findById(taskId);
	}

	@Transactional(readOnly = true)
	public Set<Task> findTasksByFlatId(final int id) {
		return this.taskRepository.findByFlatId(id);
	}

	@Transactional
	public void saveTask(final Task task) {
		this.taskRepository.save(task);
	}

	@Transactional
	public void deleteTaskById(final int taskId) {
		this.taskRepository.deleteById(taskId);
	}
}
