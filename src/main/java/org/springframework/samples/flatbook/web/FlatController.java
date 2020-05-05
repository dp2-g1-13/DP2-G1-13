
package org.springframework.samples.flatbook.web;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.samples.flatbook.service.FlatReviewService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.RequestService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.service.apis.GeocodeAPIService;
import org.springframework.samples.flatbook.utils.ReviewUtils;
import org.springframework.samples.flatbook.web.validators.FlatValidator;
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
public class FlatController {

	private static final String			VIEWS_FLATS_CREATE_OR_UPDATE_FORM	= "flats/createOrUpdateFlatForm";

	private final FlatService			flatService;
	private final DBImageService		dbImageService;
	private final PersonService			personService;
	private final HostService			hostService;
	private final AdvertisementService	advertisementService;
	private final GeocodeAPIService		geocodeAPIService;


	@Autowired
	public FlatController(final FlatService flatService, final DBImageService dbImageService, final PersonService personService, final HostService hostService, final AdvertisementService advertisementService, final TenantService tenantService,
		final FlatReviewService flatReviewService, final RequestService requestService, final GeocodeAPIService geocodeAPIService) {
		this.flatService = flatService;
		this.dbImageService = dbImageService;
		this.personService = personService;
		this.hostService = hostService;
		this.advertisementService = advertisementService;
		this.geocodeAPIService = geocodeAPIService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("flat")
	public void initBinder(final WebDataBinder dataBinder, final HttpServletRequest http) {
		if (http.getRequestURI().split("[/]")[http.getRequestURI().split("[/]").length - 1].equals("new")) {
			dataBinder.addValidators(new FlatValidator());
		}
	}

	@GetMapping(value = "/flats/new")
	public String initCreationForm(final Map<String, Object> model) {
		Flat flat = new Flat();
		model.put("flat", flat);
		return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/flats/new")
	public String processCreationForm(@Valid final Flat flat, final BindingResult result) throws UnsupportedEncodingException {
		if (result.hasErrors()) {
			return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
		} else {
			if (flat.getImages().size() < 6) {
				result.rejectValue("images", "", "a minimum of 6 images is required.");
				return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
			}
			Address address = flat.getAddress();
			GeocodeResponse geocode = this.geocodeAPIService.getGeocodeData(address.getAddress() + ", " + address.getCity());
			if (geocode.getStatus().equals("ZERO_RESULTS")) {
				result.rejectValue("address.address", "", "The address does not exist. Try again.");
				return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
			} else if (!geocode.getStatus().equals("OK")) {
				result.reject("An external error has occurred. Please try again later.");
				return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
			}
			address.setLatitude(geocode.getResults().get(0).getGeometry().getLocation().getLat());
			address.setLongitude(geocode.getResults().get(0).getGeometry().getLocation().getLng());
			flat.setAddress(address);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Host host = (Host) this.personService.findUserById(((User) auth.getPrincipal()).getUsername());
			host.addFlat(flat);
			this.flatService.saveFlat(flat);

			return "redirect:/flats/" + flat.getId();
		}
	}

	@GetMapping(value = "/flats/{flatId}/edit")
	public String initUpdateForm(@PathVariable("flatId") final int flatId, final Map<String, Object> model) {
		if (!this.validateHost(flatId)) {
			RuntimeException e = new RuntimeException("Illegal access");
			model.put("exception", e);
			return "exception";
		}
		Flat flat = this.flatService.findFlatById(flatId);
		model.put("flat", flat);
		model.put("images", flat.getImages());
		return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/flats/{flatId}/edit")
	public String processUpdateForm(@Valid final Flat flat, final BindingResult result, @PathVariable("flatId") final int flatId, final Map<String, Object> model) {
		if (result.hasErrors()) {
			return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
		} else {
			if (!this.validateHost(flatId)) {
				RuntimeException e = new RuntimeException("Illegal access");
				model.put("exception", e);
				return "exception";
			}
			Set<DBImage> newImages = flat.getImages().stream().filter(x -> !x.getFileType().equals("application/octet-stream")).collect(Collectors.toSet());
			Flat oldFlat = this.flatService.findFlatById(flatId);
			if (oldFlat.getImages().size() + newImages.size() < 6) {
				result.rejectValue("images", "", "a minimum of 6 images is required.");
				return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
			}
			Set<DBImage> images = oldFlat.getImages();
			images.addAll(newImages);
			flat.setImages(images);
			flat.setId(flatId);
			this.flatService.saveFlat(flat);

			return "redirect:/flats/{flatId}";
		}
	}

	@GetMapping(value = "/flats/{flatId}/images/{imageId}/delete")
	public String processDeleteImage(@PathVariable("flatId") final int flatId, @PathVariable("imageId") final int imageId, final Map<String, Object> model) {
		if (!this.validateHost(flatId)) {
			RuntimeException e = new RuntimeException("Illegal access");
			model.put("exception", e);
			return "exception";
		}
		Flat flat = this.flatService.findFlatById(flatId);
		if (flat.getImages().size() == 6) {
			RuntimeException e = new RuntimeException("Illegal access");
			model.put("exception", e);
			return "exception";
		} else {
			DBImage image = this.dbImageService.getImageById(imageId);
			flat.deleteImage(image);
			this.dbImageService.deleteImage(image);
			return "redirect:/flats/{flatId}/edit";
		}
	}

	@GetMapping(value = "/flats/{flatId}")
	public ModelAndView showFlat(@PathVariable("flatId") final int flatId, final Principal principal) {
		if (!this.validateHost(flatId) && !this.validateTenant(flatId)) {
			throw new RuntimeException("Illegal access");
		}
		ModelAndView mav = new ModelAndView("flats/flatDetails");
		Flat flat = this.flatService.findFlatById(flatId);
		mav.addObject(flat);

		Host host = this.hostService.findHostByFlatId(flat.getId());
		mav.addObject("host", host.getUsername());
		mav.addObject("images", flat.getImages());

		Boolean existAd = this.advertisementService.isAdvertisementWithFlatId(flat.getId());
		mav.addObject("existAd", existAd);

		List<FlatReview> reviews = new ArrayList<>(flat.getFlatReviews());
		reviews.removeIf(x -> !x.getCreator().isEnabled());
		reviews.sort(Comparator.comparing(FlatReview::getCreationDate).reversed());
		mav.addObject("reviews", reviews);
		mav.addObject("flatId", flat.getId());
		mav.addObject("canCreateReview", principal != null && ReviewUtils.isAllowedToReviewAFlat(principal.getName(), flat.getId()));
		return mav;
	}

	@GetMapping(value = "/flats/list")
	public ModelAndView showFlatsOfHost() {
		ModelAndView mav = new ModelAndView("flats/flatsOfHost");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = ((User) auth.getPrincipal()).getUsername();
		List<Flat> flats = new ArrayList<>(this.flatService.findFlatByHostUsername(username));
		List<Integer> advIds = flats.stream().map(x -> {
			Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(x.getId());
			return adv == null ? null : adv.getId();
		}).collect(Collectors.toList());
		mav.addObject("flats", flats);
		mav.addObject("advIds", advIds);
		return mav;
	}

	@GetMapping(value = "/flats/{flatId}/delete")
	public String processDeleteFlat(@PathVariable("flatId") final int flatId) {
		if (!this.validateHost(flatId)) {
			throw new RuntimeException("Illegal access");
		}
		Flat flat = this.flatService.findFlatById(flatId);
		this.flatService.deleteFlat(flat);

		return "redirect:/flats/list";
	}

	public boolean validateHost(final int flatId) {
		Boolean userIsHost = true;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ADMIN"))) {
			String username = ((User) auth.getPrincipal()).getUsername();
			String hostUsername = this.hostService.findHostByFlatId(flatId).getUsername();
			userIsHost = username.equals(hostUsername);
		}
		return userIsHost;
	}

	private boolean validateTenant(final int flatId) {
		boolean userIsTenantOfFlat = true;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ADMIN"))) {
			String username = ((User) auth.getPrincipal()).getUsername();
			Flat flat = this.flatService.findFlatById(flatId);
			userIsTenantOfFlat = flat.getTenants().stream().anyMatch(x -> x.getUsername().equals(username));
		}
		return userIsTenantOfFlat;
	}

}
