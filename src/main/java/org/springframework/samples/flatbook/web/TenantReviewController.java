package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.TenantReview;
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
import java.util.Set;

@Controller
public class TenantReviewController {

    private static final String VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM = "tenantReviews/createOrUpdateTenantReviewForm";

    private final PersonService personService;
    private final TenantService tenantService;
    private final HostService hostService;
    private final TenantReviewService tenantReviewService;

    @Autowired
    public TenantReviewController(HostService hostService, TenantReviewService tenantReviewService, TenantService tenantService, PersonService personService) {
        this.tenantReviewService = tenantReviewService;
        this.personService = personService;
        this.tenantService = tenantService;
        this.hostService =  hostService;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @GetMapping(value = "/tenant-reviews/{tenantId}/new")
    public String initCreationForm(Map<String, Object> model, Principal principal, @PathVariable("tenantId") final String tenantId) {
    	Tenant tToBeReviewed = tenantService.findTenantById(tenantId);
    	Boolean allowed = false;
    	switch(isUserATenant(principal.getName())) {
    		case 1:
    			Tenant tenant = tenantService.findTenantById(principal.getName());
    			if(tToBeReviewed!=null && tenant.getFlat()!=null && tenant.getFlat().getTenants().contains(tToBeReviewed) && tenant != tToBeReviewed) {
    				allowed = true;
    			}
    			break;
    		default:
    			Host host = hostService.findHostById(principal.getName());
    			if(tToBeReviewed!=null) {
    				for(Flat f: host.getFlats()) {
    	        		if(f.getTenants().contains(tToBeReviewed)) {
    	        			allowed = true;
    	        			break;
    	        		}
    	        	}
    			}
    	}
    	if(allowed) {
    		Person creator = personService.findUserById(principal.getName());
    		TenantReview tr = new TenantReview();
            tr.setCreationDate(LocalDate.now());
            tr.setCreator(creator);
            model.put("tenantReview", tr);
            return VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM;
    	}else {
    		throw new IllegalArgumentException("Bad tenant id or you can not make a review about this tenant.");
    	}
    }

    @PostMapping(value = "/tenant-reviews/{tenantId}/new")
    public String processCreationForm(@Valid TenantReview tr, BindingResult result, Principal principal, @PathVariable("tenantId") final String tenantId) {
        if(result.hasErrors()) {
           return VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM;
        } else {
        	tr.setCreationDate(LocalDate.now());
        	Tenant t = tenantService.findTenantById(tenantId);
        	Set<TenantReview> trs = t.getReviews();
        	trs.add(tr);
        	t.setReviews(trs);
            this.tenantReviewService.saveTenantReview(tr);
            return "redirect:/";
        }
    }
    
    @GetMapping(value = "/tenant-reviews/{tenantReviewId}/remove")
	public String processTenantReviewRemoval(@PathVariable("tenantReviewId") final int tenantReviewId, Principal principal) {
    	TenantReview tenantReview = this.tenantReviewService.findTenantReviewById(tenantReviewId);
    	Person creator = this.personService.findUserById(principal.getName());
		if (tenantReview != null && creator.equals(tenantReview.getCreator())) {
			Tenant reviewedTenant = this.tenantReviewService.findTenantOfTenantReviewById(tenantReviewId);
			Set<TenantReview> reviews = reviewedTenant.getReviews();
			reviews.remove(tenantReview);
			reviewedTenant.setReviews(reviews);
			this.tenantReviewService.deleteTenantReviewById(tenantReviewId);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad tenant review id or you are not the creator of the review.");
		}
	}
    
    private int isUserATenant(String username) {
    	Tenant tenant = tenantService.findTenantById(username);
    	Host host = hostService.findHostById(username);
    	int res = 0;
    	if(tenant != null && host == null) {
    		res = 1;
    	}
		return res;
    }
}
