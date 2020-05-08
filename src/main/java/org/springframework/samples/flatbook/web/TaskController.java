
package org.springframework.samples.flatbook.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.service.TaskService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {

	private static final String	VIEWS_TASKS_CREATE_OR_UPDATE_FORM	= "tasks/createOrUpdateTaskForm";
	private static final String	VIEWS_TASKS_LIST					= "tasks/tasksList";

	private final TaskService	taskService;
	private final TenantService	tenantService;

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);


	@Autowired
	public TaskController(final TaskService taskService, final TenantService tenantService) {
		this.taskService = taskService;
		this.tenantService = tenantService;
	}

	@GetMapping(value = "/tasks/new")
	public String initCreationForm(final Map<String, Object> model, final Principal principal) {
		Tenant tenant = this.tenantService.findTenantById(principal.getName());
		if (tenant.getFlat() != null) {
			Task task = new Task();
			task.setCreator(tenant);
			task.setCreationDate(LocalDate.now());
			task.setStatus(TaskStatus.TODO);
			task.setFlat(tenant.getFlat());
			model.put("task", task);
			model.put("roommates", new ArrayList<>(tenant.getFlat().getTenants()));
			return TaskController.VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
		} else {

		    logger.error("The tenant {} has null flat.", tenant.getUsername());

			throw new RuntimeException("You can't create a task if you dont live in a flat.");
		}
	}

	@PostMapping(value = "/tasks/new")
	public String processCreationForm(final Map<String, Object> model, @Valid final Task task, final BindingResult result,
		final Principal principal) {
		Tenant tenant = this.tenantService.findTenantById(principal.getName());
		if (tenant.getFlat() != null && (task.getAsignee() == null
			|| tenant.getFlat().getTenants().stream().anyMatch(x -> x.getUsername().equals(task.getAsignee().getUsername())))) {
			if (result.hasErrors()) {
				model.put("roommates", new ArrayList<>(tenant.getFlat().getTenants()));
				return TaskController.VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
			} else {
				task.setCreator(tenant);
				task.setCreationDate(LocalDate.now());
				task.setStatus(TaskStatus.TODO);
				task.setFlat(tenant.getFlat());
				this.taskService.saveTask(task);
				return "redirect:/tasks/list";
			}
		} else {

		    if(tenant.getFlat() == null) logger.error("Flat of tenant {} is null", tenant.getUsername());
		    else if(task.getAsignee() != null && tenant.getFlat().getTenants().stream().noneMatch(x -> x.getUsername().equals(task.getAsignee().getUsername())))
		        logger.error("The asignee is {} and these are the tenants of the flat with id {}: {}", task.getAsignee(), tenant.getFlat().getId(), tenant.getFlat().getTenants());

			throw new RuntimeException("You can't create a task if you dont live in a flat.");
		}
	}

	@GetMapping("/tasks/list")
	public ModelAndView showTaskList(final Principal principal) {
		ModelAndView mav = new ModelAndView(TaskController.VIEWS_TASKS_LIST);
		Tenant tenant = this.tenantService.findTenantById(principal.getName());
		if (tenant.getFlat() != null) {
			List<Task> tasks = new ArrayList<>(this.taskService.findTasksByFlatId(tenant.getFlat().getId()));
			tasks.sort(Comparator.comparing(Task::getStatus).thenComparing(Comparator.comparing(Task::getCreationDate).reversed()));
			mav.addObject("tasks", tasks);
			return mav;
		} else {

            logger.error("The tenant {} has null flat.", tenant.getUsername());

			throw new RuntimeException("You don't live in a flat.");
		}
	}

	@GetMapping(value = "/tasks/{taskId}/delete")
	public String processTaskRemoval(@PathVariable("taskId") final int taskId, final Principal principal) {
		Task task = this.taskService.findTaskById(taskId);
		Tenant tenant = this.tenantService.findTenantById(principal.getName());
		if (tenant.getFlat() != null && task != null && tenant.getFlat().equals(task.getFlat())) {
			this.taskService.deleteTaskById(taskId);
			return "redirect:/tasks/list";
		} else {

            if(tenant.getFlat() == null) logger.error("Flat of tenant {} is null", tenant.getUsername());
            else if(task == null) logger.error("Task with id {} is null", taskId);
            else if(tenant.getFlat().equals(task.getFlat())) logger.error("Tenant flat does not equal task flat. Tenant flat id: {}, task flat id: {}", tenant.getFlat().getId(), task.getFlat().getId());

            throw new IllegalArgumentException("Bad task id or you are cant delete this task.");
		}
	}

	@GetMapping(value = "/tasks/{taskId}/edit")
	public String initUpdateForm(@PathVariable("taskId") final int taskId, final ModelMap model, final Principal principal) {
		Task task = this.taskService.findTaskById(taskId);
		Tenant tenant = this.tenantService.findTenantById(principal.getName());
		if (tenant.getFlat() != null && task != null && tenant.getFlat().equals(task.getFlat())) {
			model.put("taskStatus", Arrays.asList(TaskStatus.values()));
			model.put("task", task);
			model.put("roommates", new ArrayList<>(tenant.getFlat().getTenants()));
			return TaskController.VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
		} else {

		    if(tenant.getFlat() == null) logger.error("Flat of tenant {} is null", tenant.getUsername());
		    else if(task == null) logger.error("Task with id {} is null", taskId);
            else if(tenant.getFlat().equals(task.getFlat())) logger.error("Tenant flat does not equal task flat. Tenant flat id: {}, task flat id: {}", tenant.getFlat().getId(), task.getFlat().getId());

			throw new IllegalArgumentException("Bad task id or you can not edit the task.");
		}
	}

	@PostMapping(value = "/tasks/{taskId}/edit")
	public String processUpdateForm(@Valid final Task task, final BindingResult result, @PathVariable("taskId") final int taskId,
		final ModelMap model, final Principal principal) {
		Task previusTask = this.taskService.findTaskById(taskId);
		Tenant tenant = this.tenantService.findTenantById(principal.getName());
		if (tenant.getFlat() != null && task != null && tenant.getFlat().equals(previusTask.getFlat())) {
			if (result.hasErrors()) {
				model.put("roommates", new ArrayList<>(tenant.getFlat().getTenants()));
				model.put("taskStatus", Arrays.asList(TaskStatus.values()));
				return TaskController.VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
			} else {
				task.setId(taskId);
				task.setCreator(previusTask.getCreator());
				task.setCreationDate(previusTask.getCreationDate());
				task.setFlat(tenant.getFlat());
				this.taskService.saveTask(task);
				return "redirect:/tasks/list";
			}
		} else {

            if(tenant.getFlat() == null) logger.error("Flat of tenant {} is null", tenant.getUsername());
            else if(task == null) logger.error("Task with id {} is null", taskId);
            else if(tenant.getFlat().equals(previusTask.getFlat())) logger.error("Tenant flat does not equal task flat. Tenant flat id: {}, task flat id: {}", tenant.getFlat().getId(), previusTask.getFlat().getId());

            throw new RuntimeException("Bad task id or you can not edit the task.");
		}
	}
}
