package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.model.TennantReview;
import org.springframework.samples.flatbook.service.AuthoritiesService;
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

@Controller
public class TennantReviewController {

    private static final String VIEWS_TENNANTREVIEWS_CREATE_OR_UPDATE_FORM = "users/reviews/createOrUpdateTennantReviewForm";

    private final PersonService personService;
    private final TennantService tennantService;
    private final HostService hostService;
    private final TennantReviewService tennantReviewService;
    private final AuthoritiesService authoritiesService;

    @Autowired
    public TennantReviewController(AuthoritiesService authoritiesService, HostService hostService, TennantReviewService tennantReviewService, TennantService tennantService, PersonService personService) {
        this.tennantReviewService = tennantReviewService;
        this.personService = personService;
        this.tennantService = tennantService;
        this.hostService =  hostService;
        this.authoritiesService = authoritiesService;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @GetMapping(value = "/tennants/{tennantId}/reviews/new")
    public String initCreationForm(Map<String, Object> model, Principal principal, @PathVariable("tennantId") final String tennantId) {
    	Tennant tToBeReviewed = tennantService.findTennantById(tennantId);
    	if(isAllowed(principal.getName(), tToBeReviewed)) {
    		Person creator = personService.findUserById(principal.getName());
    		TennantReview tr = new TennantReview();
            tr.setCreator(creator);
            tr.setCreationDate(LocalDate.now());
            model.put("tennantReview", tr);
            return VIEWS_TENNANTREVIEWS_CREATE_OR_UPDATE_FORM;
    	}else {
    		throw new IllegalArgumentException("Bad tennant id or you can not make a review about this tennant.");
    	}
    }

    @PostMapping(value = "/tennants/{tennantId}/reviews/new")
    public String processCreationForm(@Valid TennantReview tr, BindingResult result, Principal principal, @PathVariable("tennantId") final String tennantId) {
        if(result.hasErrors()) {
        	return VIEWS_TENNANTREVIEWS_CREATE_OR_UPDATE_FORM;
        } else {
        	tr.setCreationDate(LocalDate.now());
        	Tennant t = tennantService.findTennantById(tennantId);
        	t.getReviews().add(tr);
            this.tennantReviewService.saveTennantReview(tr);
            this.tennantService.saveTennant(t);
            return "redirect:/";
        }
    }
    
    @GetMapping(value = "/tennants/{tennantId}/reviews/{tennantReviewId}/remove")
	public String processTennantReviewRemoval(@PathVariable("tennantReviewId") final int tennantReviewId, @PathVariable("tennantId") final String tennantId, Principal principal) {
    	TennantReview tennantReview = this.tennantReviewService.findTennantReviewById(tennantReviewId);
    	Person creator = this.personService.findUserById(principal.getName());
    	Tennant reviewedTennant = this.tennantService.findTennantById(tennantId);
    	if (reviewedTennant != null && creator.equals(tennantReview.getCreator()) && tennantReview != null) {
			reviewedTennant.getReviews().remove(tennantReview);
			this.tennantReviewService.deleteTennantReviewById(tennantReviewId);
			this.tennantService.saveTennant(reviewedTennant);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad tennant review id or you are not the creator of the review.");
		}
	}
    
    private Boolean isAllowed(String username, Tennant tToBeReviewed) {
		Boolean allowed = false;
    	switch(authoritiesService.findAuthorityById(username)) {
    		case TENNANT:
    			Tennant tennant = tennantService.findTennantById(username);
    			if(tToBeReviewed!=null && tennant.getFlat()!=null && tennant.getFlat().getTennants().contains(tToBeReviewed) && tennant != tToBeReviewed) {
    				allowed = true;
    			}
    			break;
    		case HOST:
    			Host host = hostService.findHostById(username);
    			if(tToBeReviewed!=null) {
    				for(Flat f: host.getFlats()) {
    	        		if(f.getTennants().contains(tToBeReviewed)) {
    	        			allowed = true;
    	        			break;
    	        		}
    	        	}
    			}
    		default:
    	}
		return allowed;
	}
}
