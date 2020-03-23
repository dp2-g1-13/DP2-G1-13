package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.model.TennantReview;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.TennantReviewService;
import org.springframework.samples.flatbook.service.TennantService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Controller
public class TennantReviewController {

    private static final String VIEWS_TENNANTREVIEWS_CREATE_OR_UPDATE_FORM = "tennantReviews/createOrUpdateTennantReviewForm";

    private final PersonService personService;
    private final TennantService tennantService;
    private final HostService hostService;
    private final TennantReviewService tennantReviewService;

    @Autowired
    public TennantReviewController(HostService hostService, TennantReviewService tennantReviewService, TennantService tennantService, PersonService personService) {
        this.tennantReviewService = tennantReviewService;
        this.personService = personService;
        this.tennantService = tennantService;
        this.hostService =  hostService;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @GetMapping(value = "/tennant-reviews/{tennantId}/new")
    public String initCreationForm(Map<String, Object> model, Principal principal, @PathVariable("tennantId") final String tennantId) {
    	Tennant tToBeReviewed = tennantService.findTennantById(tennantId);
    	Tennant tennant = tennantService.findTennantById(principal.getName());
    	Host host = hostService.findHostById(principal.getName());
    	if(tennant == tToBeReviewed) {
    		throw new IllegalArgumentException("You can not review yourself.");
    	}else if(tennant == null && host!=null && tToBeReviewed!=null) {
    		Boolean allowed = false;
        	for(Flat f: host.getFlats()) {
        		if(f.getTennants().contains(tToBeReviewed)) {
        			allowed = true;
        			break;
        		}
        	}
        	if(allowed) {
        		Person creator = personService.findUserById(principal.getName());
        		TennantReview tr = new TennantReview();
                tr.setCreationDate(LocalDate.now());
                tr.setCreator(creator);
                model.put("tennantReview", tr);
                return VIEWS_TENNANTREVIEWS_CREATE_OR_UPDATE_FORM;
        	}else {
        		throw new IllegalArgumentException("You can not make a review about this tennant.");
        	}
    	}else if (host == null && tennant != null && tToBeReviewed!=null && tennant.getFlat().getTennants().contains(tToBeReviewed)) {
    		Person creator = personService.findUserById(principal.getName());
    		TennantReview tr = new TennantReview();
            tr.setCreationDate(LocalDate.now());
            tr.setCreator(creator);
            model.put("tennantReview", tr);
            return VIEWS_TENNANTREVIEWS_CREATE_OR_UPDATE_FORM;
		} else {
			throw new IllegalArgumentException("Bad tennant id or you can not make a review about this tennant.");
		}
    }

    @PostMapping(value = "/tennant-reviews/{tennantId}/new")
    public String processCreationForm(@Valid TennantReview tr, BindingResult result, Principal principal, @PathVariable("tennantId") final String tennantId) {
        if(result.hasErrors()) {
           return VIEWS_TENNANTREVIEWS_CREATE_OR_UPDATE_FORM;
        } else {
        	tr.setCreationDate(LocalDate.now());
        	Tennant t = tennantService.findTennantById(tennantId);
        	Set<TennantReview> trs = t.getReviews();
        	trs.add(tr);
        	t.setReviews(trs);
            this.tennantReviewService.saveTennantReview(tr);
            return "redirect:/";
        }
    }

//    @GetMapping(value = "/tasks/{taskId}")
//    public ModelAndView showTask(@PathVariable("taskId") int taskId, Principal principal, @ModelAttribute("roommates") Collection<Tennant> roommates) {
//    	Task task = this.taskService.findTaskById(taskId);
//    	Tennant logged = this.tennantService.findTennantById(principal.getName());
//    	if (task != null && roommates.contains(logged)) {
//    		ModelAndView mav = new ModelAndView("tasks/taskDetails");
//    		mav.addObject(this.taskService.findTaskById(taskId));
//    		return mav;
//		} else {
//			throw new IllegalArgumentException("Bad task id or you can not see the task.");
//		}
//    }
    
    @GetMapping(value = "/tennant-reviews/{tennantReviewId}/remove")
	public String processTennantReviewRemoval(@PathVariable("tennantReviewId") final int tennantReviewId, Principal principal) {
    	TennantReview tennantReview = this.tennantReviewService.findTennantReviewById(tennantReviewId);
    	Person creator = this.tennantService.findTennantById(principal.getName());
		if (tennantReview != null && creator.equals(tennantReview.getCreator())) {
			this.tennantReviewService.deleteTennantReviewById(tennantReviewId);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad flat review id or you are not the creator of the review.");
		}
	}
}
