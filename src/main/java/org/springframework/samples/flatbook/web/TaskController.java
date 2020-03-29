package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.TaskService;
import org.springframework.samples.flatbook.service.TennantService;
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
    private final TennantService tennantService;
    private final FlatService flatService;

    @Autowired
    public TaskController(TaskService taskService, TennantService tennantService, FlatService flatService) {
        this.taskService = taskService;
        this.tennantService = tennantService;
        this.flatService = flatService;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("roommates")
	public Collection<Tennant> populateRoommates(Principal principal) {
		return this.flatService.findTennantsById(this.tennantService.findTennantById(principal.getName()).getFlat().getId());
	}
    
    @GetMapping(value = "/tasks/new")
    public String initCreationForm(Map<String, Object> model, Principal principal) {
    	Task task = new Task();
    	task.setCreator(tennantService.findTennantById(principal.getName()));
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
    public ModelAndView showTask(@PathVariable("taskId") int taskId, Principal principal, @ModelAttribute("roommates") Collection<Tennant> roommates) {
    	Task task = this.taskService.findTaskById(taskId);
    	Tennant logged = this.tennantService.findTennantById(principal.getName());
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
    	Tennant creator = this.tennantService.findTennantById(principal.getName());
		if (task != null && creator.equals(task.getCreator())) {
			this.taskService.deleteTaskById(taskId);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad task id or you are not the creator of the task.");
		}
	}
    
    @GetMapping(value = "/tasks/{taskId}/edit")
	public String initUpdateForm(@PathVariable("taskId") final int taskId, final ModelMap model, Principal principal, @ModelAttribute("roommates") Collection<Tennant> roommates) {
		Task task = this.taskService.findTaskById(taskId);
		Tennant logged = this.tennantService.findTennantById(principal.getName());
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
