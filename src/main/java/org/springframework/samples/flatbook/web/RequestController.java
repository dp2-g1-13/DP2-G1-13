
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
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.dtos.RequestForm;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.RequestService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.service.exceptions.BadRequestException;
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

	private static final String		EXCEPTION_MESSAGE			= "Illegal access";

	private static final String		REQUESTS_LIST_URL			= "redirect:/flats/{flatId}/requests/list";

	private RequestService			requestService;

	private AdvertisementService	advertisementService;

	private AuthoritiesService		authoritiesService;

	private HostService				hostService;

	private FlatService				flatService;

	private TenantService			tenantService;


	@Autowired
	public RequestController(final RequestService requestService, final AdvertisementService advertisementService,
		final AuthoritiesService authoritiesService, final HostService hostService, final TenantService tenantService,
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

	@GetMapping("/flats/{flatId}/requests/new")
	public String initCreationForm(@PathVariable("flatId") final int flatId, final Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Tenant tenant = this.tenantService.findTenantById(((User) auth.getPrincipal()).getUsername());
		Flat flat = this.flatService.findFlatById(flatId);
		if (!this.validateTenant(auth, tenant, flat, flatId)) {
			throw new BadRequestException(RequestController.EXCEPTION_MESSAGE);
		}
		RequestForm req = new RequestForm();
		model.put("requestForm", req);
		return RequestController.VIEWS_REQUESTS_CREATE_FORM;
	}

	@PostMapping("/flats/{flatId}/requests/new")
	public String processCreationForm(@Valid final RequestForm request, final BindingResult result, @PathVariable("flatId") final int flatId) {
		if (result.hasErrors()) {
			return RequestController.VIEWS_REQUESTS_CREATE_FORM;
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Tenant tenant = this.tenantService.findTenantById(((User) auth.getPrincipal()).getUsername());
			Flat flat = this.flatService.findFlatById(flatId);
			if (!this.validateTenant(auth, tenant, flat, flatId)) {
				throw new BadRequestException(RequestController.EXCEPTION_MESSAGE);
			}
			Request req = new Request();
			req.setDescription(request.getDescription());
			req.setCreationDate(LocalDateTime.now());
			req.setStatus(RequestStatus.PENDING);
			req.setStartDate(request.getStartDate());
			req.setFinishDate(request.getFinishDate());
			tenant.addRequest(req);
			flat.addRequest(req);
			this.requestService.saveRequest(req);
			return "redirect:/requests/list";
		}
	}

	@GetMapping("/flats/{flatId}/requests/{requestId}/accept")
	public String processAcceptRequest(@PathVariable("flatId") final int flatId, @PathVariable("requestId") final int requestId) {
		Request request = this.requestService.findRequestById(requestId);
		if (this.validateHostAcceptingOrRejectingRequest(flatId) && request.getStatus().equals(RequestStatus.PENDING)) {
			request.setStatus(RequestStatus.ACCEPTED);
			Flat flat = this.flatService.findFlatById(flatId);
			Tenant tenant = this.tenantService.findTenantByRequestId(requestId);
			tenant.setFlat(flat);
			this.requestService.saveRequest(request);
			this.tenantService.saveTenant(tenant);
		} else {
			throw new BadRequestException(RequestController.EXCEPTION_MESSAGE);
		}
		return RequestController.REQUESTS_LIST_URL;
	}

	@GetMapping("/flats/{flatId}/requests/{requestId}/reject")
	public String processRejectRequest(@PathVariable("flatId") final int flatId, @PathVariable("requestId") final int requestId) {
		Request request = this.requestService.findRequestById(requestId);
		if (this.validateHostAcceptingOrRejectingRequest(flatId) && request.getStatus().equals(RequestStatus.PENDING)) {
			request.setStatus(RequestStatus.REJECTED);
			this.requestService.saveRequest(request);
		} else {
			throw new BadRequestException(RequestController.EXCEPTION_MESSAGE);
		}
		return RequestController.REQUESTS_LIST_URL;
	}

	@GetMapping("/flats/{flatId}/requests/{requestId}/cancel")
	public String processCancelRequest(@PathVariable("flatId") final int flatId, @PathVariable("requestId") final int requestId) {
		Request request = this.requestService.findRequestById(requestId);
		if (!this.validateHostAcceptingOrRejectingRequest(flatId) || !request.getStatus().equals(RequestStatus.ACCEPTED)
			|| !request.getStartDate().isAfter(LocalDate.now())) {
			throw new BadRequestException(RequestController.EXCEPTION_MESSAGE);
		}
		request.setFinishDate(LocalDate.now().plusDays(1));
		request.setStatus(RequestStatus.CANCELED);
		this.processCancelOrConclude(request, requestId);

		return RequestController.REQUESTS_LIST_URL;
	}

	@GetMapping("/flats/{flatId}/requests/{requestId}/conclude")
	public String processConcludeRequest(@PathVariable("flatId") final int flatId, @PathVariable("requestId") final int requestId) {
		Request request = this.requestService.findRequestById(requestId);
		if (!this.validateHostAcceptingOrRejectingRequest(flatId) || !request.getStatus().equals(RequestStatus.ACCEPTED)
			|| request.getStartDate().isAfter(LocalDate.now())) {
			throw new BadRequestException(RequestController.EXCEPTION_MESSAGE);
		}
		request.setFinishDate(LocalDate.now().plusDays(1));
		request.setStatus(RequestStatus.FINISHED);
		this.processCancelOrConclude(request, requestId);
		return RequestController.REQUESTS_LIST_URL;
	}

	@GetMapping("/requests/list")
	public ModelAndView showRequestsOfTenant() {
		ModelAndView mav = new ModelAndView("requests/requestsList");
		String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		List<Request> requests = new ArrayList<>(this.requestService.findRequestsByTenantUsername(username));
		List<Integer> advIds = requests.stream().map(x -> {
			Flat flat = this.flatService.findFlatWithRequestId(x.getId());
			Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(flat.getId());
			return adv == null ? null : adv.getId();
		}).collect(Collectors.toList());
		mav.addObject("requests", requests);
		mav.addObject("advIds", advIds);
		return mav;
	}

	@GetMapping("/flats/{flatId}/requests/list")
	public ModelAndView showRequestsOfFlat(@PathVariable("flatId") final int flatId) {
		if (!this.validateHostAcceptingOrRejectingRequest(flatId)) {
			throw new BadRequestException(RequestController.EXCEPTION_MESSAGE);
		}
		ModelAndView mav = new ModelAndView("requests/requestsList");
		Flat flat = this.flatService.findFlatById(flatId);
		List<Request> requests = new ArrayList<>(flat.getRequests());
		requests.removeIf(x -> !this.tenantService.findTenantByRequestId(x.getId()).isEnabled());
		requests.sort(Comparator.comparing(Request::getCreationDate).reversed());
		List<Tenant> tenants = requests.stream().map(x -> this.tenantService.findTenantByRequestId(x.getId())).collect(Collectors.toList());
		mav.addObject("requests", requests);
		mav.addObject("tenants", tenants);
		mav.addObject("flatId", flatId);
		return mav;
	}

	private boolean validateTenant(final Authentication auth, final Tenant tenant, final Flat flat, final int flatId) {
		return tenant.getFlat() != null || this.requestService.isThereRequestOfTenantByFlatId(((User) auth.getPrincipal()).getUsername(), flatId)
			|| flat == null;
	}

	private boolean validateHostAcceptingOrRejectingRequest(final int flatId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = ((User) auth.getPrincipal()).getUsername();
		return username.equals(this.hostService.findHostByFlatId(flatId).getUsername());
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
