package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.TenantReviewService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class TenantReviewController {

    private static final String VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM = "users/reviews/createOrUpdateTenantReviewForm";

    private final PersonService personService;
    private final TenantService tenantService;
    private final HostService hostService;
    private final TenantReviewService tenantReviewService;
    private final AuthoritiesService authoritiesService;

    @Autowired
    public TenantReviewController(AuthoritiesService authoritiesService, HostService hostService, TenantReviewService tenantReviewService, TenantService tenantService, PersonService personService) {
        this.tenantReviewService = tenantReviewService;
        this.personService = personService;
        this.tenantService = tenantService;
        this.hostService =  hostService;
        this.authoritiesService = authoritiesService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/tenants/{tenantId}/reviews/new")
    public String initCreationForm(Map<String, Object> model, Principal principal, @PathVariable("tenantId") final String tenantId) {
    	Tenant tToBeReviewed = tenantService.findTenantById(tenantId);
    	if(isAllowed(principal.getName(), tToBeReviewed)) {
    		Person creator = personService.findUserById(principal.getName());
    		TenantReview tr = new TenantReview();
            tr.setCreator(creator);
            tr.setCreationDate(LocalDate.now());
            model.put("tenantReview", tr);
            return VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM;
    	}else {
    		throw new IllegalArgumentException("Bad tenant id or you can not make a review about this tenant.");
    	}
    }

    @PostMapping(value = "/tenants/{tenantId}/reviews/new")
    public String processCreationForm(@Valid TenantReview tr, BindingResult result, Principal principal, @PathVariable("tenantId") final String tenantId) {
    	Tenant tToBeReviewed = tenantService.findTenantById(tenantId);
    	if(isAllowed(principal.getName(), tToBeReviewed)) {
    		if(result.hasErrors()) {
    			return VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM;
    		} else {
    			tr.setCreationDate(LocalDate.now());
    			tToBeReviewed.getReviews().add(tr);
    			this.tenantReviewService.saveTenantReview(tr);
    			this.tenantService.saveTenant(tToBeReviewed);
    			return "redirect:/";
    		}
    	}else {
    		throw new IllegalArgumentException("Bad tenant id or you can not make a review about this tenant.");
    	}
    }

    @GetMapping(value = "/tenants/{tenantId}/reviews/{tenantReviewId}/remove")
	public String processTenantReviewRemoval(@PathVariable("tenantReviewId") final int tenantReviewId, @PathVariable("tenantId") final String tenantId, Principal principal) {
    	TenantReview tenantReview = this.tenantReviewService.findTenantReviewById(tenantReviewId);
    	Person creator = this.personService.findUserById(principal.getName());
    	Tenant reviewedTenant = this.tenantService.findTenantById(tenantId);
    	if (tenantReview != null && reviewedTenant != null && creator.equals(tenantReview.getCreator())) {
			reviewedTenant.getReviews().remove(tenantReview);
			this.tenantReviewService.deleteTenantReviewById(tenantReviewId);
			this.tenantService.saveTenant(reviewedTenant);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad tenant review id or you are not the creator of the review.");
		}
	}

    private Boolean isAllowed(String username, Tenant tToBeReviewed) {
		Boolean allowed = false;
    	switch(authoritiesService.findAuthorityById(username)) {
    		case TENANT:
    			Tenant tenant = tenantService.findTenantById(username);
    			if(tToBeReviewed!=null && tenant.getFlat()!=null && tenant.getFlat().getTenants().contains(tToBeReviewed) && tenant != tToBeReviewed) {
    				allowed = true;
    			}
    			break;
    		case HOST:
    			Host host = hostService.findHostById(username);
    			if(tToBeReviewed!=null) {
    				for(Flat f: host.getFlats()) {
    	        		if(f.getTenants().contains(tToBeReviewed)) {
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
