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
    	Boolean allowed = false;
    	switch(isUserATennant(principal.getName())) {
    		case 1:
    			Tennant tennant = tennantService.findTennantById(principal.getName());
    			if(tToBeReviewed!=null && tennant.getFlat()!=null && tennant.getFlat().getTennants().contains(tToBeReviewed) && tennant != tToBeReviewed) {
    				allowed = true;
    			}
    			break;
    		default:
    			Host host = hostService.findHostById(principal.getName());
    			if(tToBeReviewed!=null) {
    				for(Flat f: host.getFlats()) {
    	        		if(f.getTennants().contains(tToBeReviewed)) {
    	        			allowed = true;
    	        			break;
    	        		}
    	        	}
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
    
    @GetMapping(value = "/tennant-reviews/{tennantReviewId}/remove")
	public String processTennantReviewRemoval(@PathVariable("tennantReviewId") final int tennantReviewId, Principal principal) {
    	TennantReview tennantReview = this.tennantReviewService.findTennantReviewById(tennantReviewId);
    	Person creator = this.personService.findUserById(principal.getName());
		if (tennantReview != null && creator.equals(tennantReview.getCreator())) {
			Tennant reviewedTennant = this.tennantReviewService.findTennantOfTennantReviewById(tennantReviewId);
			Set<TennantReview> reviews = reviewedTennant.getReviews();
			reviews.remove(tennantReview);
			reviewedTennant.setReviews(reviews);
			this.tennantReviewService.deleteTennantReviewById(tennantReviewId);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad tennant review id or you are not the creator of the review.");
		}
	}
    
    private int isUserATennant(String username) {
    	Tennant tennant = tennantService.findTennantById(username);
    	Host host = hostService.findHostById(username);
    	int res = 0;
    	if(tennant != null && host == null) {
    		res = 1;
    	}
		return res;
    }
}
