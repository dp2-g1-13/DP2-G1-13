package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.service.FlatReviewService;
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
public class FlatReviewController {

    private static final String VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM = "flatReviews/createOrUpdateFlatReviewForm";

    private final TennantService tennantService;
    private final FlatReviewService flatReviewService;

    @Autowired
    public FlatReviewController(FlatReviewService flatReviewService, TennantService tennantService) {
        this.flatReviewService = flatReviewService;
        this.tennantService = tennantService;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @GetMapping(value = "/flat-reviews/new")
    public String initCreationForm(Map<String, Object> model, Principal principal) {
    	Tennant user = tennantService.findTennantById(principal.getName());
    	FlatReview fr = new FlatReview();
        fr.setCreator(user);
        fr.setCreationDate(LocalDate.now());
        model.put("flatReview", fr);
        return VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/flat-reviews/new")
    public String processCreationForm(@Valid FlatReview fr, BindingResult result, Principal principal) {
        if(result.hasErrors()) {
           return VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
        } else {
        	fr.setCreationDate(LocalDate.now());
        	Flat f = tennantService.findTennantById(principal.getName()).getFlat();
        	Set<FlatReview> frs = f.getFlatReviews();
        	frs.add(fr);
        	f.setFlatReviews(frs);
            this.flatReviewService.saveFlatReview(fr);
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
    
    @GetMapping(value = "/flat-reviews/{flatReviewId}/remove")
	public String processFlatReviewRemoval(@PathVariable("flatReviewId") final int flatReviewId, Principal principal) {
    	FlatReview flatReview = this.flatReviewService.findFlatReviewById(flatReviewId);
    	Tennant creator = this.tennantService.findTennantById(principal.getName());
		if (flatReview != null && creator.equals(flatReview.getCreator())) {
			this.flatReviewService.deleteFlatReviewById(flatReviewId);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad flat review id or you are not the creator of the review.");
		}
	}
}
