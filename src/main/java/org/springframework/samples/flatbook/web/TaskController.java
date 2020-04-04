package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.TaskService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
import java.util.List;
import java.util.Map;

@Controller
public class TaskController {

    private static final String VIEWS_TASKS_CREATE_OR_UPDATE_FORM = "tasks/createOrUpdateTaskForm";

    private final TaskService taskService;
    private final TenantService tenantService;
    private final FlatService flatService;

    @Autowired
    public TaskController(TaskService taskService, TenantService tenantService, FlatService flatService) {
        this.taskService = taskService;
        this.tenantService = tenantService;
        this.flatService = flatService;
    }

    private Integer getCreatorFlatId(String creatorUsername) {
		Integer res = null;
		try {
			res = this.tenantService.findTenantById(creatorUsername).getFlat().getId();
			return res;
		} catch (NullPointerException e) {
			return res;
		}
	}

    private Collection<Tenant> getCreatorRoommates(Integer creatorFlatId) {
    	Collection<Tenant> res = null;
		try {
			res = this.flatService.findTenantsById(creatorFlatId);
			return res;
		} catch (NullPointerException e) {
			return res;
		}
	}

    @GetMapping(value = "/tasks/new")
    public String initCreationForm(Map<String, Object> model, Principal principal) {
    	Integer creatorFlatId = getCreatorFlatId(principal.getName());
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
    	Integer creatorFlatId = getCreatorFlatId(principal.getName());
    	Collection<Tenant> roommates = getCreatorRoommates(creatorFlatId);
    	if(creatorFlatId != null && roommates!=null && roommates.contains(task.getAsignee())) {
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
    public ModelAndView showTaskList() {
        ModelAndView mav = new ModelAndView("tasks/tasksList");
        String username = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Collection<Tenant> creators = getCreatorRoommates(getCreatorFlatId(username));
        if(creators != null) {
        	 List<Task> tasks = new ArrayList<>();
        	 for(Tenant t:creators) {
        		 tasks.addAll(this.taskService.findManyByTenantUsername(t.getUsername()));
        	 }
             mav.addObject("tasks", tasks);
             return mav;
        }else {
        	throw new RuntimeException("You don't live in a flat.");
        }
       
    }

    @GetMapping(value = "/tasks/{taskId}/remove")
	public String processTaskRemoval(@PathVariable("taskId") final int taskId, Principal principal) {
    	Task task = this.taskService.findTaskById(taskId);
    	Tenant creator = this.tenantService.findTenantById(principal.getName());
		if (task != null && creator.equals(task.getCreator())) {
			this.taskService.deleteTaskById(taskId);
			return "redirect:/tasks/list";
		} else {
			throw new IllegalArgumentException("Bad task id or you are not the creator of the task.");
		}
	}

    @GetMapping(value = "/tasks/{taskId}/edit")
	public String initUpdateForm(@PathVariable("taskId") final int taskId, final ModelMap model, Principal principal) {
		Task task = this.taskService.findTaskById(taskId);
		Integer creatorFlatId = getCreatorFlatId(principal.getName());
		Collection<Tenant> roommates = getCreatorRoommates(creatorFlatId);
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
		Integer creatorFlatId = getCreatorFlatId(principal.getName());
		Collection<Tenant> roommates = getCreatorRoommates(creatorFlatId);
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
