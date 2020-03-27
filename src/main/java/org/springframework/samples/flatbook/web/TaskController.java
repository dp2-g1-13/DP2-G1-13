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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
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
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("roommates")
	public Collection<Tenant> populateRoommates(Principal principal) {
		return this.flatService.findTenantsById(this.tenantService.findTenantById(principal.getName()).getFlat().getId());
	}
    
    @GetMapping(value = "/tasks/new")
    public String initCreationForm(Map<String, Object> model, Principal principal) {
    	Task task = new Task();
    	task.setCreator(tenantService.findTenantById(principal.getName()));
    	task.setCreationDate(LocalDate.now());
    	task.setStatus(TaskStatus.TODO);
        model.put("task", task);
        return VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/tasks/new")
    public String processCreationForm(@Valid Task task, BindingResult result) {
        if(result.hasErrors()) {
           return VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
        } else {
        	task.setCreationDate(LocalDate.now());
            this.taskService.saveTask(task);
            return "redirect:/";
        }
    }

    @GetMapping(value = "/tasks/{taskId}")
    public ModelAndView showTask(@PathVariable("taskId") int taskId, Principal principal, @ModelAttribute("roommates") Collection<Tenant> roommates) {
    	Task task = this.taskService.findTaskById(taskId);
    	Tenant logged = this.tenantService.findTenantById(principal.getName());
    	if (task != null && roommates.contains(logged)) {
    		ModelAndView mav = new ModelAndView("tasks/taskDetails");
    		mav.addObject(this.taskService.findTaskById(taskId));
    		return mav;
		} else {
			throw new IllegalArgumentException("Bad task id or you can not see the task.");
		}
    }
    
    @GetMapping(value = "/tasks/{taskId}/remove")
	public String processTaskRemoval(@PathVariable("taskId") final int taskId, Principal principal) {
    	Task task = this.taskService.findTaskById(taskId);
    	Tenant creator = this.tenantService.findTenantById(principal.getName());
		if (task != null && creator.equals(task.getCreator())) {
			this.taskService.deleteTaskById(taskId);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad task id or you are not the creator of the task.");
		}
	}
    
    @GetMapping(value = "/tasks/{taskId}/edit")
	public String initUpdateForm(@PathVariable("taskId") final int taskId, final ModelMap model, Principal principal, @ModelAttribute("roommates") Collection<Tenant> roommates) {
		Task task = this.taskService.findTaskById(taskId);
		Tenant logged = this.tenantService.findTenantById(principal.getName());
    	if (task != null && roommates.contains(logged)) {
			model.put("taskStatus", Arrays.asList(TaskStatus.values()));
			model.put("task", task);
			return VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
		} else {
			throw new IllegalArgumentException("Bad task id or you can not edit the task.");
		}
	}

	@PostMapping(value = "/tasks/{taskId}/edit")
	public String processUpdateForm(@Valid final Task task, @PathVariable("taskId") final int taskId, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("task", task);
			return VIEWS_TASKS_CREATE_OR_UPDATE_FORM;
		} else {
			task.setId(taskId);
			this.taskService.saveTask(task);
			return "redirect:/";
		}
	}
}
