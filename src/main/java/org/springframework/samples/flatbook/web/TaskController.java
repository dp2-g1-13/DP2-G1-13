package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.TaskService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class TaskController {

    private static final String VIEWS_TASKS_CREATE_OR_UPDATE_FORM = "tasks/createOrUpdateTaskForm";
    private static final String VIEWS_TASKS_LIST = "tasks/tasksList";

    private final TaskService taskService;
    private final TenantService tenantService;
    private final FlatService flatService;

    @Autowired
    public TaskController(TaskService taskService, TenantService tenantService, FlatService flatService) {
        this.taskService = taskService;
        this.tenantService = tenantService;
        this.flatService = flatService;
    }

    @GetMapping(value = "/tasks/new")
    public String initCreationForm(Map<String, Object> model, Principal principal) {
    	Integer creatorFlatId = this.tenantService.findTenantById(principal.getName()).getFlat()==null ? 
				null : this.tenantService.findTenantById(principal.getName()).getFlat().getId();
    	if(creatorFlatId != null) {
    		Collection<Tenant> roommates = this.flatService.findTenantsById(creatorFlatId);
    		Task task = new Task();
        	task.setCreator(tenantService.findTenantById(principal.getName()));
        	task.setCreationDate(LocalDate.now());
        	task.setStatus(TaskStatus.TODO);
            model.put("task", task);
            model.put("roommates", roommates);
            return VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
    	}else {
    		throw new RuntimeException("You can't create a task if you dont live in a flat.");
    	}
    }

    @PostMapping(value = "/tasks/new")
    public String processCreationForm(Map<String, Object> model, @Valid Task task, BindingResult result, Principal principal) {
    	Integer creatorFlatId = this.tenantService.findTenantById(principal.getName()).getFlat()==null ? 
				null : this.tenantService.findTenantById(principal.getName()).getFlat().getId();
    	Collection<Tenant> roommates = this.flatService.findTenantsById(creatorFlatId);
    	if(creatorFlatId != null && roommates != null && (task.getAsignee() == null || roommates.contains(task.getAsignee()))) {
    		if(result.hasErrors()) {
    			model.put("roommates", roommates);
            	return VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
            } else {
            	task.setCreationDate(LocalDate.now());
            	task.setStatus(TaskStatus.TODO);
                this.taskService.saveTask(task);
                return "redirect:/tasks/list";
            }
    	}else {
    		throw new RuntimeException("Oops!");
    	}
    }
    
    @GetMapping("/tasks/list")
    public ModelAndView showTaskList(Principal principal) {
        ModelAndView mav = new ModelAndView(VIEWS_TASKS_LIST);
        Collection<Tenant> creators = this.flatService.findTenantsById(this.tenantService.findTenantById(principal.getName()).getFlat()==null ? 
				null : this.tenantService.findTenantById(principal.getName()).getFlat().getId());
        if(creators != null) {
        	 List<Task> tasks = new ArrayList<>();
        	 for(Tenant t:creators) {
        		 tasks.addAll(this.taskService.findManyByTenantUsername(t.getUsername()));
        	 }
        	 tasks.sort(Comparator.comparing(Task::getStatus).thenComparing(Comparator.comparing(Task::getCreationDate).reversed()));
             mav.addObject("tasks", tasks);
             return mav;
        }else {
        	throw new RuntimeException("You don't live in a flat.");
        }
    }

    @GetMapping(value = "/tasks/{taskId}/delete")
	public String processTaskRemoval(@PathVariable("taskId") final int taskId, Principal principal) {
    	Task task = this.taskService.findTaskById(taskId);
    	Integer creatorFlatId = this.tenantService.findTenantById(principal.getName()).getFlat()==null ? 
				null : this.tenantService.findTenantById(principal.getName()).getFlat().getId();
		Collection<Tenant> roommates = this.flatService.findTenantsById(creatorFlatId);
		if (creatorFlatId != null && task != null && roommates!=null && roommates.contains(task.getCreator())) {
			this.taskService.deleteTaskById(taskId);
			return "redirect:/tasks/list";
		} else {
			throw new IllegalArgumentException("Bad task id or you are not the creator of the task.");
		}
	}

    @GetMapping(value = "/tasks/{taskId}/edit")
	public String initUpdateForm(@PathVariable("taskId") final int taskId, final ModelMap model, Principal principal) {
		Task task = this.taskService.findTaskById(taskId);
		Integer creatorFlatId = this.tenantService.findTenantById(principal.getName()).getFlat()==null ? 
				null : this.tenantService.findTenantById(principal.getName()).getFlat().getId();
		Collection<Tenant> roommates = this.flatService.findTenantsById(creatorFlatId);
    	if (creatorFlatId != null && task != null && roommates!=null && roommates.contains(task.getCreator())) {
			model.put("taskStatus", Arrays.asList(TaskStatus.values()));
			model.put("task", task);
			model.put("roommates", roommates);
			return VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
		} else {
			throw new IllegalArgumentException("Bad task id or you can not edit the task.");
		}
	}

	@PostMapping(value = "/tasks/{taskId}/edit")
	public String processUpdateForm(@Valid final Task task, final BindingResult result, @PathVariable("taskId") final int taskId,  final ModelMap model, Principal principal) {
		Integer creatorFlatId = this.tenantService.findTenantById(principal.getName()).getFlat()==null ? 
				null : this.tenantService.findTenantById(principal.getName()).getFlat().getId();
		Collection<Tenant> roommates = this.flatService.findTenantsById(creatorFlatId);
		if (creatorFlatId != null && roommates!=null && roommates.contains(task.getCreator())) {
			if (result.hasErrors()) {
				model.put("roommates", roommates);
				model.put("taskStatus", Arrays.asList(TaskStatus.values()));
				return VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
			} else {
				task.setId(taskId);
				this.taskService.saveTask(task);
				return "redirect:/tasks/list";
			}
		}else {
    		throw new RuntimeException("Oops!");
    	}
	}
}
