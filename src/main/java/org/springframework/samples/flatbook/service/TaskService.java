package org.springframework.samples.flatbook.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public Task findTaskById(int taskId) throws DataAccessException {
        return this.taskRepository.findById(taskId);
    }
    
    @Transactional(readOnly = true)
    public Set<Task> findManyByTenantUsername(String username) throws DataAccessException {
        return this.taskRepository.findManyByTenantUsername(username);
    }
    
    @Transactional
    public void saveTask(Task task) throws DataAccessException {
        this.taskRepository.save(task);
    }
    
    @Transactional
	public void deleteTaskById(final int taskId) {
		this.taskRepository.deleteById(taskId);
	}
}
