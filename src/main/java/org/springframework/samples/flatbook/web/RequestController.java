
package org.springframework.samples.flatbook.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.model.mappers.RequestForm;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.RequestService;
import org.springframework.samples.flatbook.service.TenantService;
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

@Controller
public class RequestController {

	private static final String		VIEWS_REQUESTS_CREATE_FORM	= "requests/createRequestForm";

	private RequestService			requestService;

	private AdvertisementService	advertisementService;

	private AuthoritiesService		authoritiesService;

	private HostService				hostService;

	private FlatService				flatService;

	private TenantService			tenantService;


	@Autowired
	public RequestController(final RequestService requestService, final AdvertisementService advertisementService, final AuthoritiesService authoritiesService, final HostService hostService, final TenantService tenantService,
		final FlatService flatService) {
		this.requestService = requestService;
		this.advertisementService = advertisementService;
		this.authoritiesService = authoritiesService;
		this.hostService = hostService;
		this.tenantService = tenantService;
		this.flatService = flatService;
	}

	@InitBinder("requestForm")
	public void initBinder(final WebDataBinder dataBinder) {
		String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		dataBinder.addValidators(new RequestFormValidator(this.authoritiesService.findAuthorityById(username)));
	}

	@GetMapping("/advertisements/{advertisementId}/requests/new")
	public String initCreationForm(@PathVariable("advertisementId") final int advertisementId, final Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Tenant tenant = this.tenantService.findTenantById(((User) auth.getPrincipal()).getUsername());
		Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
		this.validateTenant(auth, tenant, advertisement, advertisementId);
		RequestForm req = new RequestForm();
		model.put("requestForm", req);
		return RequestController.VIEWS_REQUESTS_CREATE_FORM;
	}

	@PostMapping("/advertisements/{advertisementId}/requests/new")
	public String processCreationForm(@Valid final RequestForm request, final BindingResult result, @PathVariable("advertisementId") final int advertisementId) {
		if (result.hasErrors()) {
			return RequestController.VIEWS_REQUESTS_CREATE_FORM;
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Tenant tenant = this.tenantService.findTenantById(((User) auth.getPrincipal()).getUsername());
			Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
			this.validateTenant(auth, tenant, advertisement, advertisementId);
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
	public String processAcceptRequest(@PathVariable("advertisementId") final int advertisementId, @PathVariable("requestId") final int requestId) {
		if (this.validateHostAcceptingOrRejectingRequest(requestId)) {
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
	public String processRejectRequest(@PathVariable("advertisementId") final int advertisementId, @PathVariable("requestId") final int requestId) {
		if (this.validateHostAcceptingOrRejectingRequest(requestId)) {
			Request request = this.requestService.findRequestById(requestId);
			request.setStatus(RequestStatus.REJECTED);
			this.requestService.saveRequest(request);
		}
		return "redirect:/advertisements/{advertisementId}/requests/list";
	}
	@GetMapping("/advertisements/{advertisementId}/requests/{requestId}/cancel")
	public String processCancelRequest(@PathVariable("advertisementId") final int advertisementId, @PathVariable("requestId") final int requestId) {
		if (this.validateHostAcceptingOrRejectingRequest(requestId)) {
			Request request = this.requestService.findRequestById(requestId);
			if (!request.getStatus().equals(RequestStatus.ACCEPTED) || !request.getStartDate().isAfter(LocalDate.now())) {
				throw new RuntimeException("Illegal access");
			}
			request.setFinishDate(LocalDate.now().plusDays(1));
			request.setStatus(RequestStatus.CANCELED);
			this.processCancelOrConclude(request, requestId);
		}
		return "redirect:/advertisements/{advertisementId}/requests/list";
	}

	@GetMapping("/advertisements/{advertisementId}/requests/{requestId}/conclude")
	public String processConcludeRequest(@PathVariable("advertisementId") final int advertisementId, @PathVariable("requestId") final int requestId) {
		if (this.validateHostAcceptingOrRejectingRequest(requestId)) {
			Request request = this.requestService.findRequestById(requestId);
			if (!request.getStatus().equals(RequestStatus.ACCEPTED) || request.getStartDate().isAfter(LocalDate.now())) {
				throw new RuntimeException("Illegal access");
			}
			request.setFinishDate(LocalDate.now().plusDays(1));
			request.setStatus(RequestStatus.FINISHED);
			this.processCancelOrConclude(request, requestId);
		}
		return "redirect:/advertisements/{advertisementId}/requests/list";
	}

	@GetMapping("/requests/list")
	public ModelAndView showRequestsOfTenant() {
		ModelAndView mav = new ModelAndView("requests/requestsList");
		String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		List<Request> requests = new ArrayList<>(this.requestService.findRequestsByTenantUsername(username));
		List<Integer> advIds = requests.stream().map(x -> {
			Advertisement adv = this.advertisementService.findAdvertisementWithRequestId(x.getId());
			return adv == null ? null : adv.getId();
		}).collect(Collectors.toList());
		mav.addObject("requests", requests);
		mav.addObject("advIds", advIds);
		return mav;
	}

	public void validateTenant(final Authentication auth, final Tenant tenant, final Advertisement advertisement, final int advertisementId) {
		if (tenant.getFlat() != null || this.requestService.isThereRequestOfTenantByAdvertisementId(((User) auth.getPrincipal()).getUsername(), advertisementId) || advertisement == null) {
			throw new RuntimeException("Illegal access");
		}
	}
	@GetMapping("/advertisements/{advertisementId}/requests/list")
	public ModelAndView showRequestsOfAdvertisement(@PathVariable("advertisementId") final int advertisementId) {
		ModelAndView mav = new ModelAndView("requests/requestsList");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Advertisement adv = this.advertisementService.findAdvertisementById(advertisementId);
		if (auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ADMIN"))) {
			String username = ((User) auth.getPrincipal()).getUsername();
			Host creator = this.hostService.findHostByFlatId(adv.getFlat().getId());
			if (!username.equals(creator.getUsername())) {
				throw new RuntimeException("Illegal access");
			}
		}
		List<Request> requests = new ArrayList<>(adv.getRequests());
		requests.sort(Comparator.comparing(Request::getCreationDate).reversed());
		List<Tenant> tenants = requests.stream().map(x -> this.tenantService.findTenantByRequestId(x.getId())).collect(Collectors.toList());
		mav.addObject("requests", requests);
		mav.addObject("tenants", tenants);
		mav.addObject("advId", advertisementId);
		return mav;
	}

	public boolean validateHostAcceptingOrRejectingRequest(final int requestId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ADMIN"))) {
			String username = ((User) auth.getPrincipal()).getUsername();
			Host host = this.hostService.findHostOfAdvertisementByRequestId(requestId);
			if (!username.equals(host.getUsername())) {
				throw new RuntimeException("Illegal access");
			}
		}
		return true;
	}

	private void processCancelOrConclude(final Request request, final int requestId) {
		Tenant tenant = this.tenantService.findTenantByRequestId(requestId);
		Flat flat = tenant.getFlat();
		flat.kickTenantOut(tenant);
		tenant.setFlat(null);
		this.flatService.saveFlat(flat);
		this.tenantService.saveTenant(tenant);
		this.requestService.saveRequest(request);
	}

}
