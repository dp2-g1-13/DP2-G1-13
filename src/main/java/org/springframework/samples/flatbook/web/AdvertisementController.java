
package org.springframework.samples.flatbook.web;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.dtos.AdvertisementForm;
import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.samples.flatbook.model.pojos.Location;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.RequestService;
import org.springframework.samples.flatbook.service.apis.GeocodeAPIService;
import org.springframework.samples.flatbook.service.exceptions.BadRequestException;
import org.springframework.samples.flatbook.utils.ReviewUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdvertisementController {

    private static final String		VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM	= "advertisements/createOrUpdateAdvertisementForm";
	private static final String 	EXCEPTION_MESSAGE = "Illegal access";
	private static final String 	WELCOME_PAGE = "welcome";
    public static final String ADVERTISEMENTS_LIST = "advertisements/advertisementsList";
    public static final String ADVERTISEMENT_DETAILS = "advertisements/advertisementDetails";

    private AdvertisementService	advertisementService;
	private AuthoritiesService	    authoritiesService;
	private FlatService				flatService;
	private DBImageService			dbImageService;
	private HostService				hostService;
	private RequestService			requestService;
	private PersonService			personService;
	private GeocodeAPIService		geocodeAPIService;


	@Autowired
	public AdvertisementController(final AdvertisementService advertisementService, final DBImageService dbImageService,
		final FlatService flatService, final HostService hostService, final RequestService requestService, final PersonService personService,
		final GeocodeAPIService geocodeAPIService, final AuthoritiesService authoritiesService) {
		this.advertisementService = advertisementService;
		this.dbImageService = dbImageService;
		this.flatService = flatService;
		this.hostService = hostService;
		this.requestService = requestService;
		this.personService = personService;
		this.geocodeAPIService = geocodeAPIService;
		this.authoritiesService	   =  authoritiesService;
	}

	@GetMapping(value = "/flats/{flatId}/advertisements/new")
	public String initCreationForm(@PathVariable("flatId") final int flatId, final Map<String, Object> model) {
		Flat flat = this.flatService.findFlatById(flatId);
		if (flat == null || !this.validateHost(flatId) || this.advertisementService.isAdvertisementWithFlatId(flat.getId())) {
			throw new BadRequestException(EXCEPTION_MESSAGE);
		}
		AdvertisementForm advertisement = new AdvertisementForm();
		model.put("advertisementForm", advertisement);
		return AdvertisementController.VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/flats/{flatId}/advertisements/new")
	public String processCreationForm(final ModelMap model, @Valid final AdvertisementForm adv, final BindingResult result,
		@PathVariable("flatId") final int flatId) {
		if (result.hasErrors()) {
			return AdvertisementController.VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
		} else {
			Flat flat = this.flatService.findFlatById(flatId);
			if (flat == null || !this.validateHost(flatId) || this.advertisementService.isAdvertisementWithFlatId(flat.getId())) {
				throw new BadRequestException(EXCEPTION_MESSAGE);
			}
			Advertisement advertisement = new Advertisement(adv);
			advertisement.setFlat(flat);
			this.advertisementService.saveAdvertisement(advertisement);
			return "redirect:/advertisements/" + advertisement.getId();
		}
	}

	@GetMapping(value = "/advertisements/{advertisementId}/edit")
	public String initUpdateForm(@PathVariable("advertisementId") final int advertisementId, final Map<String, Object> model) {
		Advertisement adv = this.advertisementService.findAdvertisementById(advertisementId);
		if (adv == null || !this.validateHost(adv.getFlat().getId())) {
			throw new BadRequestException(EXCEPTION_MESSAGE);
		}
		AdvertisementForm af = new AdvertisementForm(adv);
		model.put("advertisementForm", af);
		return AdvertisementController.VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/advertisements/{advertisementId}/edit")
	public String processUpdateForm(@Valid final AdvertisementForm adv, final BindingResult result,
		@PathVariable("advertisementId") final int advertisementId, final Map<String, Object> model) {
		if (result.hasErrors()) {
			return AdvertisementController.VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
		} else {
			Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
			if (advertisement == null || !this.validateHost(advertisement.getFlat().getId())) {
				throw new BadRequestException(EXCEPTION_MESSAGE);
			}
			Advertisement newAdvertisement = new Advertisement(adv);
			newAdvertisement.setFlat(advertisement.getFlat());
			newAdvertisement.setCreationDate(advertisement.getCreationDate());
			newAdvertisement.setId(advertisement.getId());
			this.advertisementService.saveAdvertisement(newAdvertisement);
			return "redirect:/advertisements/" + newAdvertisement.getId();
		}
	}

	@GetMapping(value = "/advertisements/{advertisementId}/delete")
	public String processDeleteAdvertisement(@PathVariable("advertisementId") final int advertisementId, final Map<String, Object> model) {
		Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
		if (advertisement == null || !this.validateHost(advertisement.getFlat().getId())) {
			throw new BadRequestException(EXCEPTION_MESSAGE);
		}
		this.advertisementService.deleteAdvertisement(advertisement);
		return "redirect:/flats/list";
	}

	@GetMapping(value = "/advertisements/{advertisementId}")
	public ModelAndView showAdvertisement(@PathVariable("advertisementId") final int advertisementId, final Principal principal) {
		ModelAndView mav = new ModelAndView(ADVERTISEMENT_DETAILS);
		Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
		Host host = this.hostService.findHostByFlatId(advertisement.getFlat().getId());

		if (!host.isEnabled()) {
			throw new BadRequestException(EXCEPTION_MESSAGE);
		}

		mav.addObject(advertisement);
		mav.addObject("flat", advertisement.getFlat());

		Collection<DBImage> images = this.dbImageService.getImagesByFlatId(advertisement.getFlat().getId());
		mav.addObject("images", images);

		mav.addObject("host", host.getUsername());

		List<FlatReview> reviews = new ArrayList<>(advertisement.getFlat().getFlatReviews());
		reviews.removeIf(x -> !x.getCreator().isEnabled());
		reviews.sort(Comparator.comparing(FlatReview::getCreationDate).reversed());
		mav.addObject("reviews", reviews);
		mav.addObject("flatId", advertisement.getFlat().getId());
		mav.addObject("canCreateReview",
			principal != null && ReviewUtils.isAllowedToReviewAFlat(principal.getName(), advertisement.getFlat().getId(), this.flatService, this.authoritiesService));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ROLE_ANONYMOUS"))) {
			Person person = this.personService.findUserById(((User) auth.getPrincipal()).getUsername());
			if (person instanceof Tenant) {
				mav.addObject("requestMade",
					this.requestService.isThereRequestOfTenantByFlatId(person.getUsername(), advertisement.getFlat().getId()));
				mav.addObject("hasFlat", ((Tenant) person).getFlat() != null);
			}
		}
		return mav;
	}

	@GetMapping(value = "/advertisements")
	public String processFindForm(final Address address, final BindingResult result, final Map<String, Object> model)
		throws UnsupportedEncodingException {
		if (address.getCity() == null || address.getCity().equals("")) {
			result.rejectValue("city", "cityNotNull", "The field 'city' can't be null.");
			return WELCOME_PAGE;
		}

		GeocodeResponse geocode = this.geocodeAPIService
			.getGeocodeData(address.getCity() + " " + (address.getPostalCode() != null ? address.getPostalCode() : ""));
		if (geocode.getStatus().equals("ZERO_RESULTS")) {
			result.rejectValue("city", "", "The address does not exist. Try again.");
			return WELCOME_PAGE;
		} else if (!geocode.getStatus().equals("OK")) {
			result.reject("An external error has occurred. Please try again later.");
			return WELCOME_PAGE;
		}

		Location location = geocode.getResults().get(0).getGeometry().getLocation();

		List<Advertisement> results = this.advertisementService.findAllAdvertisements().stream().filter(
			x -> this.hostService.findHostByFlatId(x.getFlat().getId()).isEnabled() && this.haversineFormula(x.getFlat().getAddress().getLatitude(),
				x.getFlat().getAddress().getLongitude(), location.getLat(), location.getLng()) < 30000)
			.sorted(Comparator.comparing(x -> this.haversineFormula(x.getFlat().getAddress().getLatitude(), x.getFlat().getAddress().getLongitude(),
				location.getLat(), location.getLng())))
			.collect(Collectors.toList());

		if (results.isEmpty()) {
			result.rejectValue("postalCode", "advNotFound", "Not found.");
			return WELCOME_PAGE;
		} else {
			model.put("selections", results);
			model.put("latitude", location.getLat());
			model.put("longitude", location.getLng());
			return ADVERTISEMENTS_LIST;
		}
	}

	public Boolean validateHost(final int flatId) {
		Boolean userIsHost = true;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals(AuthoritiesType.ADMIN.toString()))) {
			String username = ((User) auth.getPrincipal()).getUsername();
			Host host = this.hostService.findHostByFlatId(flatId);
			userIsHost = username.equals(host.getUsername()) && host.isEnabled();
		}
		return userIsHost;
	}

	private Double haversineFormula(final Double latitudeAd, final Double longitudeAd, final Double latitudeQuery, final Double longitudeQuery) {
		double earthRadius = 6371e3;
		double lat1 = Math.toRadians(latitudeAd);
		double lat2 = Math.toRadians(latitudeQuery);
		double dlat = lat2 - lat1;
		double dlon = Math.toRadians(longitudeQuery - longitudeAd);

		double haversine = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));

		return earthRadius * c;
	}

}
