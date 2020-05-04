package org.springframework.samples.flatbook.service;

import java.time.LocalDate;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.repository.TaskRepository;
import org.springframework.stereotype.Service;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TaskServiceTests {

	private static final String	FIRSTNAME_1	= "Ramon";
	private static final String	LASTNAME_1	= "Fernandez";
	private static final String	DNI_1		= "23330000A";
	private static final String	EMAIL_1		= "b@b.com";
	private static final String	USERNAME_1	= "ababa";
	private static final String	TELEPHONE_1	= "777789789";
	private static final String	FIRSTNAME_2	= "Dani";
	private static final String	LASTNAME_2	= "Sanchez";
	private static final String	DNI_2		= "23330000B";
	private static final String	EMAIL_2		= "a@a.com";
	private static final String	USERNAME_2	= "asasa";
	private static final String	TELEPHONE_2	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private static final String	DESCRIPTION		= "description";
	private static final String	TITLE		= "title";
	private static final Integer ID		= 1;
	private static final Integer ID2		= 2;

	@Mock
	private TaskRepository	taskRepositoryMocked;

	@Autowired
	private TaskRepository	taskRepository;

	private Task				task;
	private Task				task2;
	private Tenant              assignee;
	private Tenant				creator;

	private TaskService		taskService;
	private TaskService		taskServiceMocked;


	@BeforeEach
	void setupMock() {
		this.creator = new Tenant();
		this.creator.setPassword(PASSWORD);
		this.creator.setUsername(USERNAME_1);
		this.creator.setDni(DNI_1);
		this.creator.setEmail(EMAIL_1);
		this.creator.setEnabled(true);
		this.creator.setFirstName(FIRSTNAME_1);
		this.creator.setLastName(LASTNAME_1);
		this.creator.setPhoneNumber(TELEPHONE_1);

		this.assignee = new Tenant();
		this.assignee.setPassword(PASSWORD);
		this.assignee.setUsername(USERNAME_2);
		this.assignee.setDni(DNI_2);
		this.assignee.setEmail(EMAIL_2);
		this.assignee.setEnabled(true);
		this.assignee.setFirstName(FIRSTNAME_2);
		this.assignee.setLastName(LASTNAME_2);
		this.assignee.setPhoneNumber(TELEPHONE_2);

		this.task = new Task();
		this.task.setAsignee(this.assignee);
		this.task.setCreationDate(LocalDate.now());
		this.task.setDescription(DESCRIPTION);
		this.task.setCreator(this.creator);
		this.task.setStatus(TaskStatus.TODO);
		this.task.setTitle(TITLE);
		this.task.setId(ID);
		this.task.setFlat(new Flat());

		this.task2 = new Task();
		this.task2.setAsignee(this.assignee);
		this.task2.setCreationDate(LocalDate.now());
		this.task2.setDescription(DESCRIPTION);
		this.task2.setCreator(this.creator);
		this.task2.setStatus(TaskStatus.TODO);
		this.task2.setTitle(TITLE);
		this.task2.setId(ID2);
        this.task2.setFlat(new Flat());

		this.taskServiceMocked = new TaskService(this.taskRepositoryMocked);
		this.taskService = new TaskService(this.taskRepository);
	}

	@Test
	void shouldFindTaskById() {
		when(this.taskRepositoryMocked.findById(ID)).thenReturn(this.task);
		Task taskById = this.taskServiceMocked.findTaskById(ID);
		assertThat(taskById).hasNoNullFieldsOrProperties();
		assertThat(taskById).hasId(1);
		assertThat(taskById).hasAsignee(assignee);
        assertThat(taskById).hasCreator(creator);
		assertThat(taskById).hasDescription(DESCRIPTION);
		assertThat(taskById).hasStatus(TaskStatus.TODO);
	}

	@Test
	void shouldNotFindTask() {
		Task taskById = this.taskServiceMocked.findTaskById(2);
		Assertions.assertThat(taskById).isNull();
	}

	@Test
	void shouldSaveTask() {
		Mockito.lenient().doNothing().when(this.taskRepositoryMocked).save(ArgumentMatchers.isA(Task.class));
		this.taskServiceMocked.saveTask(this.task2);
		Mockito.verify(this.taskRepositoryMocked).save(this.task2);
	}

	@Test
	void shouldDeleteTask() {
		this.taskService.deleteTaskById(ID);
		Task t = this.taskService.findTaskById(ID);
		Assertions.assertThat(t).isNull();
	}

	@Test
    void shouldFindTasksByFlatId() {
        Set<Task> tasks = this.taskService.findTasksByFlatId(1);
        Assertions.assertThat(tasks).hasSize(2);
        Assertions.assertThat(tasks).extracting(Task::getDescription)
            .containsExactlyInAnyOrder("Nullam sit amet turpis elementum ligula vehicula consequat.",
                "Vestibulum quam sapien, varius ut, blandit non, interdum in, ante.");
    }

    @Test
    void shouldNotFindTasksByFlatId() {
        Set<Task> tasks = this.taskService.findTasksByFlatId(0);
        Assertions.assertThat(tasks).isEmpty();
    }
}
