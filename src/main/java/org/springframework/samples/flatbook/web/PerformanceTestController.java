
package org.springframework.samples.flatbook.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.model.pojos.PerformanceTestInfo;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.FlatReviewService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.RequestService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.cucumber.messages.internal.com.google.common.collect.Sets;

@RestController
@RequestMapping("performance")
public class PerformanceTestController {

	private static final String DESCRIPTION = "DescriptionDescriptionDescriptionDescriptionDescription";
	private final FlatService			flatService;
	private final RequestService		requestService;
	private final TenantService			tenantService;
	private final HostService			hostService;
	private final AdvertisementService	advertisementService;
	private final AuthoritiesService	authoritiesService;
	private final FlatReviewService		flatReviewService;


	@Autowired
	public PerformanceTestController(final FlatService flatService, final TenantService tenantService, final HostService hostService,
		final AdvertisementService advertisementService, final RequestService requestService, final FlatReviewService flatReviewService,
		final AuthoritiesService authoritiesService) {
		this.flatService = flatService;
		this.tenantService = tenantService;
		this.hostService = hostService;
		this.advertisementService = advertisementService;
		this.requestService = requestService;
		this.authoritiesService = authoritiesService;
		this.flatReviewService = flatReviewService;
	}

	@GetMapping(path = "/flats/prepare", produces = "application/json")
	public PerformanceTestInfo prepareFlat(@RequestParam(name = "hostId", required = true) final String hostId) {

		Flat flat = new Flat();

		flat.setImages(Stream.generate(DBImage::new).limit(6).collect(Collectors.toSet()));
		flat.getImages().forEach(x -> {
			x.setData("imagina".getBytes());
			x.setFilename("image");
			x.setFileType("jpg");
		});

		flat.setAddress(this.createAddress());

		flat.setAvailableServices("Wifi and breakfast");
		flat.setNumberBaths(2);
		flat.setDescription(DESCRIPTION);
		flat.setNumberRooms(2);
		flat.setSquareMeters(900);

		Host host = this.hostService.findHostById(hostId);

		host.addFlat(flat);
		this.flatService.saveFlat(flat);

		PerformanceTestInfo info = new PerformanceTestInfo();
		info.setFlatId(flat.getId());
		info.setHostId(hostId);

		info.setTenantId(this.createTenant(flat));

		return info;
	}

	private Address createAddress() {
		Address address = new Address();
		address.setCity("Sevilla");
		address.setCountry("Spain");
		address.setLatitude(37.3822261);
		address.setLongitude(-6.0123468);
		address.setPostalCode("41009");
		address.setLocation("Mar Caspio 7");
		return address;
	}

	private String createTenant(final Flat flat) {
		Random random = new Random();
		Tenant tenant = new Tenant();
		char letter = (char) (random.nextInt() * 100 % 25 + 65);
		String numbers = Stream.generate(() -> random.nextInt() * 9 + "").limit(8).reduce("", (x, y) -> x + y);
		tenant.setDni(numbers + letter);
		tenant.setEmail("email" + tenant.getDni() + "@user.com");
		tenant.setEnabled(true);
		tenant.setPassword("Is-Dp2-G1-13");
		tenant.setLastName("Joe");
		tenant.setUsername("user" + tenant.getDni());
		tenant.setFirstName("Momma");
		tenant.setPhoneNumber(numbers + "9");
		tenant.setFlat(flat);
		this.tenantService.saveTenant(tenant);
		this.authoritiesService.saveAuthorty(new Authorities(tenant.getUsername(), AuthoritiesType.TENANT));

		return tenant.getUsername();
	}

	@GetMapping(path = "/advertisements/prepare", produces = "application/json")
	public PerformanceTestInfo prepareAdvertisement(@RequestParam(name = "hostId", required = true) final String hostId) {
		PerformanceTestInfo info = this.prepareFlat(hostId);
		Advertisement advertisement = new Advertisement();

		advertisement.setCreationDate(LocalDate.now().plusDays(-1));
		advertisement.setDescription(DESCRIPTION);
		advertisement.setPricePerMonth(1000.0);
		advertisement.setRequirements("RequerementRequirmentRequerementRequeriment");
		advertisement.setTitle("Advertisement");
		advertisement.setFlat(this.flatService.findFlatById(info.getFlatId()));

		this.advertisementService.saveAdvertisement(advertisement);

		info.setAdvertisementId(advertisement.getId());
		return info;
	}

	@GetMapping(path = "/requests/prepare", produces = "application/json")
	public PerformanceTestInfo prepareRequest(@RequestParam(name = "hostId", required = true) final String hostId,
		@RequestParam(name = "status", required = true) final String status) {
		PerformanceTestInfo info = this.prepareFlat(hostId);

		Request request = new Request();

		request.setCreationDate(LocalDateTime.now().plusDays(-1));
		request.setDescription(DESCRIPTION);
		request.setFinishDate(LocalDate.now().plusDays(20));
		request.setStartDate(LocalDate.now().plusDays(-1));
		request.setStatus(RequestStatus.valueOf(status));

		Flat flat = this.flatService.findFlatById(info.getFlatId());
		flat.addRequest(request);

		Tenant tenant = this.tenantService.findTenantById(info.getTenantId());
		tenant.addRequest(request);

		this.requestService.saveRequest(request);
		this.flatService.saveFlat(flat);
		this.tenantService.saveTenant(tenant);

		info.setRequestId(request.getId());
		return info;
	}

	@GetMapping(path = "/reviews/prepare", produces = "application/json")
	public PerformanceTestInfo prepareReview(@RequestParam(name = "hostId", required = true) final String hostId) {
		PerformanceTestInfo info = this.prepareAdvertisement(hostId);

		FlatReview review = new FlatReview();
		Tenant tenant = this.tenantService.findTenantById(info.getTenantId());

		review.setCreationDate(LocalDate.now().plusDays(-1));
		review.setDescription(DESCRIPTION);
		review.setRate(2);
		review.setCreator(tenant);

		Flat flat = this.flatService.findFlatById(info.getFlatId());
		flat.setFlatReviews(Sets.newHashSet(review));

		this.flatReviewService.saveFlatReview(review);
		this.flatService.saveFlat(flat);

		info.setReviewId(review.getId());
		return info;
	}

}
