package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.model.mappers.RequestForm;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.validators.RequestFormValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RequestController {

    private static final String VIEWS_REQUESTS_CREATE_FORM = "requests/createRequestForm";

    private RequestService requestService;

    private PersonService personService;

    private AdvertisementService advertisementService;

    private AuthoritiesService authoritiesService;

    private HostService hostService;

    private TenantService tenantService;

    @Autowired
    public RequestController(RequestService requestService, PersonService personService, AdvertisementService advertisementService,
                             AuthoritiesService authoritiesService, HostService hostService, TenantService tenantService) {
        this.requestService = requestService;
        this.personService = personService;
        this.advertisementService = advertisementService;
        this.authoritiesService = authoritiesService;
        this.hostService = hostService;
        this.tenantService = tenantService;
    }

    @InitBinder("requestForm")
    public void initBinder(WebDataBinder dataBinder) {
        String username = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        dataBinder.addValidators(new RequestFormValidator(this.authoritiesService.findAuthorityById(username)));
    }

    @GetMapping("/advertisements/{advertisementId}/requests/new")
    public String initCreationForm(@PathVariable("advertisementId") int advertisementId, Map<String, Object> model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Tenant tenant = (Tenant) this.personService.findUserById(((User)auth.getPrincipal()).getUsername());
        Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
        if(tenant.getFlat() != null || !validateTenantDoesNotHaveRequestsToFlat(auth, advertisementId) || advertisement == null)
            throw new RuntimeException("Illegal access");
        RequestForm req = new RequestForm();
        model.put("requestForm", req);
        return VIEWS_REQUESTS_CREATE_FORM;
    }

    @PostMapping("/advertisements/{advertisementId}/requests/new")
    public String processCreationForm(@Valid RequestForm request, BindingResult result, @PathVariable("advertisementId") int advertisementId) {
        if(result.hasErrors()) {
            return VIEWS_REQUESTS_CREATE_FORM;
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Tenant tenant = (Tenant) this.personService.findUserById(((User)auth.getPrincipal()).getUsername());
            Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
            if(tenant.getFlat() != null || !validateTenantDoesNotHaveRequestsToFlat(auth, advertisementId) || advertisement == null)
                throw new RuntimeException("Illegal access");
            Request req = new Request();
            req.setDescription(request.getDescription());
            req.setCreationDate(LocalDateTime.now());
            req.setStatus(RequestStatus.PENDING);
            req.setStartDate(request.getStartDate());
            req.setFinishDate(request.getFinishDate());
            tenant.addRequest(req);
            advertisement.addRequest(req);
            this.requestService.saveRequest(req);
            return "redirect:/requests/list";
        }
    }

    @GetMapping("/advertisements/{advertisementId}/requests/{requestId}/accept")
    public String processAcceptRequest(@PathVariable("advertisementId") int advertisementId, @PathVariable("requestId") int requestId) {
        if(validateHostAcceptingOrRejectingRequest(requestId)) {
            Request request = this.requestService.findRequestById(requestId);
            request.setStatus(RequestStatus.ACCEPTED);
            Flat flat = this.advertisementService.findAdvertisementById(advertisementId).getFlat();
            Tenant tenant = this.tenantService.findTenantByRequestId(requestId);
            tenant.setFlat(flat);
            this.requestService.saveRequest(request);
            this.tenantService.saveTenant(tenant);
        }
        return "redirect:/advertisements/{advertisementId}/requests/list";
    }

    @GetMapping("/advertisements/{advertisementId}/requests/{requestId}/reject")
    public String processRejectRequest(@PathVariable("advertisementId") int advertisementId, @PathVariable("requestId") int requestId) {
        if(validateHostAcceptingOrRejectingRequest(requestId)) {
            Request request = this.requestService.findRequestById(requestId);
            request.setStatus(RequestStatus.REJECTED);
            this.requestService.saveRequest(request);
        }
        return "redirect:/advertisements/{advertisementId}/requests/list";
    }

    @GetMapping("/requests/list")
    public ModelAndView showRequestsOfTenant() {
        ModelAndView mav = new ModelAndView("requests/requestsList");
        String username = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<Request> requests = new ArrayList<>(this.requestService.findRequestsByTenantUsername(username));
        List<Integer> advIds = requests.stream()
            .map(x -> {
                Advertisement adv = this.advertisementService.findAdvertisementWithRequestId(x.getId());
                return adv == null? null : adv.getId();
            }).collect(Collectors.toList());
        mav.addObject("requests", requests);
        mav.addObject("advIds", advIds);
        return mav;
    }

    @GetMapping("/advertisements/{advertisementId}/requests/list")
    public ModelAndView showRequestsOfAdvertisement(@PathVariable("advertisementId") int advertisementId) {
        ModelAndView mav = new ModelAndView("requests/requestsList");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Advertisement adv = this.advertisementService.findAdvertisementById(advertisementId);
        if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("admin"))) {
            String username = ((User)auth.getPrincipal()).getUsername();
            Host creator = this.hostService.findHostByFlatId(adv.getFlat().getId());
            if(!username.equals(creator.getUsername()))
                throw new RuntimeException("Illegal access");
        }
        List<Request> requests = new ArrayList<>(adv.getRequests());
        requests.sort(Comparator.comparing(Request::getCreationDate));
        List<Tenant> tenants = requests.stream().map(x -> this.tenantService.findTenantByRequestId(x.getId()))
            .collect(Collectors.toList());
        mav.addObject("requests", requests);
        mav.addObject("tenants", tenants);
        mav.addObject("advId", advertisementId);
        return mav;
    }

    public boolean validateTenantDoesNotHaveRequestsToFlat(Authentication auth, int advertisementId) {
        return !this.requestService.isThereRequestOfTenantByAdvertisementId(((User)auth.getPrincipal()).getUsername(), advertisementId);
    }

    public boolean validateHostAcceptingOrRejectingRequest(int requestId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("admin"))) {
            String username = ((User) auth.getPrincipal()).getUsername();
            Host host = this.hostService.findHostOfAdvertisementByRequestId(requestId);
            if(!username.equals(host.getUsername()))
                throw new RuntimeException("Illegal access");
        }
        return true;
    }


}
