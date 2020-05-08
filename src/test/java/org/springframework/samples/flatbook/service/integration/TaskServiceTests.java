package org.springframework.samples.flatbook.service.integration;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TaskServiceTests {

	private static final String	DESCRIPTION = "description";
	private static final String	TITLE		= "title";
	private static final Integer ID		    = 1;
	private static final Integer ID_WRONG	= 0;

	@Autowired
    private TaskService taskService;

    @Autowired
    private FlatService flatService;

	private Task				task;


	@BeforeEach
	void setup() {

		this.task = new Task();
		this.task.setCreationDate(LocalDate.now());
		this.task.setDescription(DESCRIPTION);
		this.task.setStatus(TaskStatus.TODO);
		this.task.setTitle(TITLE);
	}

	@Test
	void shouldFindTaskById() {
		Task taskById = this.taskService.findTaskById(ID);
		assertThat(taskById).hasNoNullFieldsOrPropertiesExcept("asignee");
		assertThat(taskById).hasId(1);
		assertThat(taskById).hasDescription("Nullam sit amet turpis elementum ligula vehicula consequat.");
		assertThat(taskById).hasStatus(TaskStatus.DONE);
		assertThat(taskById).hasCreationDate(LocalDate.of(2020 ,2, 21));
	}

	@Test
	void shouldNotFindTask() {
		Task taskById = this.taskService.findTaskById(ID_WRONG);
		Assertions.assertThat(taskById).isNull();
	}

	@Test
	void shouldSaveTask() {
	    Flat flat = this.flatService.findFlatById(ID);
	    task.setFlat(flat);
        Iterator<Tenant> iterator = flat.getTenants().iterator();
        Tenant creator = iterator.next();
        Tenant asignee = iterator.next();
        task.setCreator(creator);
	    task.setAsignee(asignee);
		this.taskService.saveTask(task);

		Task taskSaved = this.taskService.findTaskById(task.getId());
		assertThat(taskSaved).hasTitle(task.getTitle());
		assertThat(taskSaved).hasDescription(task.getDescription());
		assertThat(taskSaved).hasStatus(task.getStatus());
		assertThat(taskSaved).hasCreationDate(task.getCreationDate());
		assertThat(taskSaved).hasCreator(task.getCreator());
		assertThat(taskSaved).hasAsignee(task.getAsignee());
	}

	@Test
	void shouldDeleteTask() {
		this.taskService.deleteTaskById(ID);
		Task t = this.taskService.findTaskById(ID);
		Assertions.assertThat(t).isNull();
	}

	@Test
    void shouldFindTasksByFlatId() {
        Set<Task> tasks = this.taskService.findTasksByFlatId(ID);
        Assertions.assertThat(tasks).hasSize(2);
        Assertions.assertThat(tasks).extracting(Task::getDescription)
            .containsExactlyInAnyOrder("Nullam sit amet turpis elementum ligula vehicula consequat.",
                "Vestibulum quam sapien, varius ut, blandit non, interdum in, ante.");
    }

    @Test
    void shouldNotFindTasksByFlatId() {
        Set<Task> tasks = this.taskService.findTasksByFlatId(ID_WRONG);
        Assertions.assertThat(tasks).isEmpty();
    }
}
