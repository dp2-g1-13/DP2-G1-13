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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class TenantReviewController {

    private static final String VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM = "users/reviews/createOrUpdateTenantReviewForm";
    private static final String VIEWS_TENANT_REVIEW_LIST= "users/reviews/tenantReviewsList";
    
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
    			return "redirect:/tenants/"+tenantId+"/reviews/list";
    		}
    	}else {
    		throw new IllegalArgumentException("Bad tenant id or you can not make a review about this tenant.");
    	}
    }
    
    @GetMapping(value = "/tenants/{tenantId}/reviews/{tenantReviewId}/edit")
	public String initUpdateForm(@PathVariable("tenantReviewId") final int tenantReviewId, @PathVariable("tenantId") final String tenantId, final ModelMap model, Principal principal) {
    	TenantReview tenantReview = this.tenantReviewService.findTenantReviewById(tenantReviewId);
    	Person creator = this.personService.findUserById(principal.getName());
		if (creator != null && tenantReview != null && creator.equals(tenantReview.getCreator())) {
			model.put("tenantReview", tenantReview);
			return VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM;
		} else {
			throw new IllegalArgumentException("Bad tenant review id or you can not edit it.");
		}
	}

	@PostMapping(value = "/tenants/{tenantId}/reviews/{tenantReviewId}/edit")
	public String processUpdateForm(@Valid final TenantReview tenantReview, final BindingResult result, @PathVariable("tenantReviewId") final int tenantReviewId, @PathVariable("tenantId") final String tenantId,  final ModelMap model, Principal principal) {
    	Person creator = this.personService.findUserById(principal.getName());
    	Tenant reviewedTenant = this.tenantService.findTenantById(tenantId);
		if (creator != null && reviewedTenant != null && creator.equals(tenantReview.getCreator())) {
			if (result.hasErrors()) {
				return VIEWS_TENANTREVIEWS_CREATE_OR_UPDATE_FORM;
			} else {
				tenantReview.setId(tenantReviewId);
				tenantReview.setModifiedDate(LocalDate.now());
				this.tenantReviewService.saveTenantReview(tenantReview);
				this.tenantService.saveTenant(reviewedTenant);
				return "redirect:/tenants/"+tenantId+"/reviews/list";
			}
		}else {
    		throw new RuntimeException("Oops!");
    	}
	}
    
    @GetMapping("/tenants/{tenantId}/reviews/list")
    public ModelAndView showFlatReviewList(Principal principal, @PathVariable("tenantId") final String tenantId) {
        ModelAndView mav = new ModelAndView(VIEWS_TENANT_REVIEW_LIST);
    	Tenant tenant = this.tenantService.findTenantById(tenantId);
    	boolean allowed = false;
    	if(principal != null) {
    		allowed = isAllowed(principal.getName(), tenant);
    	}
    	if(tenant != null) {
        	 List<TenantReview> trs = new ArrayList<>(tenant.getReviews());
        	 trs.sort(Comparator.comparing(TenantReview::getCreationDate).reversed());
             mav.addObject("tenantReviews", trs);
             mav.addObject("canCreate", allowed);
             return mav;
        }else {
        	throw new IllegalArgumentException("Bad tenant id.");
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
			return "redirect:/tenants/"+tenantId+"/reviews/list";
		} else {
			throw new IllegalArgumentException("Bad tenant review id or you are not the creator of the review.");
		}
	}

	private boolean isAllowed(String username, Tenant tToBeReviewed) {
		Boolean allowed = false;
		if (tToBeReviewed != null && 
				tToBeReviewed.getReviews().stream().noneMatch(r -> r.getCreator().getUsername().equals(username))) {
			switch (authoritiesService.findAuthorityById(username)) {
			case TENANT:
				Tenant tenant = tenantService.findTenantById(username);
				if (tenant.getFlat() != null && tenant.getFlat().getTenants().contains(tToBeReviewed)
						&& tenant != tToBeReviewed) {
					allowed = true;
				}
				break;
			case HOST:
				Host host = hostService.findHostById(username);
				for (Flat f : host.getFlats()) {
					if (f.getTenants().contains(tToBeReviewed)) {
						allowed = true;
						break;
					}
				}
			default:
			}
		}
		return allowed;
	}
}
