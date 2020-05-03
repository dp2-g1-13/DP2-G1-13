package org.springframework.samples.flatbook.web;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.samples.flatbook.model.pojos.GeocodeResult;
import org.springframework.samples.flatbook.model.pojos.Geometry;
import org.springframework.samples.flatbook.model.pojos.Location;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.RequestService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.service.apis.GeocodeAPIService;
import org.springframework.samples.flatbook.web.utils.ReviewUtils;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AdvertisementController.class,
	includeFilters = {@ComponentScan.Filter(value = ReviewUtils.class, type = FilterType.ASSIGNABLE_TYPE)},
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration= SecurityConfiguration.class)
public class AdvertisementControllerTests {

    private static final double LATITUDE = 37.3822261;
	private static final double LONGITUDE = -6.0123468;
	private static final Integer TEST_ADVERTISEMENT_ID = 1;
    private static final String TEST_HOST_USERNAME = "spring";
    private static final String TEST_TENANT_USERNAME = "spring-tenant";
    private static final Integer TEST_FLAT_ID = 1;
    private static final String TEST_CITY_FLAT = "Seville";
    private static final String TEST_COUNTRY_FLAT = "Spain";
    private static final String TEST_POSTAL_CODE_FLAT = "41010";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlatService flatService;

    @MockBean
    private DBImageService dbImageService;

    @MockBean
    private PersonService personService;

    @MockBean
    private HostService hostService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private AuthoritiesService authoritiesService;
    
    @MockBean
    private TenantService tenantService;
    
    @MockBean
    private RequestService requestService;
    
    @MockBean
    private GeocodeAPIService geocodeAPIService;

    @BeforeEach
    void setup() {
        Address address = new Address();
        address.setCountry(AdvertisementControllerTests.TEST_COUNTRY_FLAT);
        address.setCity(AdvertisementControllerTests.TEST_CITY_FLAT);
        address.setPostalCode(AdvertisementControllerTests.TEST_POSTAL_CODE_FLAT);
        address.setAddress("Plaza Nueva");
        address.setLatitude(LATITUDE);
        address.setLongitude(LONGITUDE);
        
        GeocodeResponse response = new GeocodeResponse();
        List<GeocodeResult> resultList = new ArrayList<>();
        GeocodeResult result = new GeocodeResult();
        Geometry geometry = new Geometry();
        Location location = new Location();
        location.setLat(address.getLatitude());
        location.setLng(address.getLongitude());
        geometry.setLocation(location);
        result.setGeometry(geometry);
        resultList.add(result);
        response.setResults(resultList);
        response.setStatus("OK");

        DBImage image = new DBImage();
        image.setFilename("a");
        image.setFileType("b");
        image.setData(new byte[]{1, 2, 3});
        Set<DBImage> images = new HashSet<>();
        images.add(image);

        Request request = new Request();
        request.setStatus(RequestStatus.PENDING);
        request.setDescription("This is a sample description");
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Set<Request> requests = new HashSet<>();
        requests.add(request);
        
        Set<FlatReview> fr = new HashSet<>();
        Set<Tenant> tenants = new HashSet<>();
        Flat flat = new Flat();
        flat.setId(AdvertisementControllerTests.TEST_FLAT_ID);
        flat.setDescription("this is a sample description with more than 30 characters");
        flat.setSquareMeters(100);
        flat.setNumberRooms(3);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and TV");
        flat.setAddress(address);
        flat.setImages(images);
        flat.setRequests(requests);
        flat.setFlatReviews(fr);
        flat.setTenants(tenants);

        Advertisement advertisement = new Advertisement();
        advertisement.setId(AdvertisementControllerTests.TEST_ADVERTISEMENT_ID);
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(985.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Set<Advertisement> advertisements = new HashSet<>();
        advertisements.add(advertisement);

        Host host = new Host();
        host.setUsername(AdvertisementControllerTests.TEST_HOST_USERNAME);
        host.setEnabled(true);

        Tenant tenant = new Tenant();
        tenant.setUsername(AdvertisementControllerTests.TEST_TENANT_USERNAME);
        tenant.setEnabled(true);
        
        Set<Advertisement> allAdverts = new HashSet<>();
        allAdverts.add(advertisement);

        BDDMockito.given(this.flatService.findFlatById(AdvertisementControllerTests.TEST_FLAT_ID)).willReturn(flat);
        BDDMockito.given(this.advertisementService.isAdvertisementWithFlatId(AdvertisementControllerTests.TEST_FLAT_ID)).willReturn(false);
        BDDMockito.given(this.hostService.findHostByFlatId(AdvertisementControllerTests.TEST_FLAT_ID)).willReturn(host);
        BDDMockito.given(this.advertisementService.findAdvertisementById(AdvertisementControllerTests.TEST_ADVERTISEMENT_ID)).willReturn(advertisement);
        BDDMockito.given(this.dbImageService.getImagesByFlatId(AdvertisementControllerTests.TEST_FLAT_ID)).willReturn(images);
        BDDMockito.given(this.personService.findUserById(AdvertisementControllerTests.TEST_HOST_USERNAME)).willReturn(host);
        BDDMockito.given(this.personService.findUserById(AdvertisementControllerTests.TEST_TENANT_USERNAME)).willReturn(tenant);
        BDDMockito.given(this.requestService.isThereRequestOfTenantByFlatId(AdvertisementControllerTests.TEST_TENANT_USERNAME, AdvertisementControllerTests.TEST_FLAT_ID)).willReturn(true);
        BDDMockito.given(this.authoritiesService.findAuthorityById(AdvertisementControllerTests.TEST_TENANT_USERNAME)).willReturn(AuthoritiesType.TENANT);
        BDDMockito.given(this.authoritiesService.findAuthorityById(AdvertisementControllerTests.TEST_HOST_USERNAME)).willReturn(AuthoritiesType.HOST);
        try {
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(address.getAddress() + ", " + address.getCity())).willReturn(response);
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(AdvertisementControllerTests.TEST_CITY_FLAT + ", " + AdvertisementControllerTests.TEST_COUNTRY_FLAT + AdvertisementControllerTests.TEST_POSTAL_CODE_FLAT)).willReturn(response);
		} catch (UnsupportedEncodingException e) {
		}
        BDDMockito.given(this.advertisementService.findAllAdvertisements()).willReturn(allAdverts);
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testInitCreationForm() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/advertisements/new", AdvertisementControllerTests.TEST_FLAT_ID))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("advertisements/createOrUpdateAdvertisementForm"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("advertisementForm"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testInitCreationFormThrowsExceptionWithWrongHost() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/advertisements/new", AdvertisementControllerTests.TEST_FLAT_ID))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/flats/{flatId}/advertisements/new", AdvertisementControllerTests.TEST_FLAT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .param("title", "Sample title")
            .param("description", "Sample description")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "985.50"))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        BDDMockito.then(this.advertisementService).should().saveAdvertisement(ArgumentMatchers.isA(Advertisement.class));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessCreationFormWithErrors() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/flats/{flatId}/advertisements/new", AdvertisementControllerTests.TEST_FLAT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .param("title", "Sample title")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "-35.50"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasErrors("advertisementForm"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("advertisementForm", "description"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("advertisementForm", "pricePerMonth"))
            .andExpect(MockMvcResultMatchers.view().name("advertisements/createOrUpdateAdvertisementForm"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testProcessCreationFormThrowExceptionWithWrongHost() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/flats/{flatId}/advertisements/new", AdvertisementControllerTests.TEST_FLAT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .param("title", "Sample title")
            .param("description", "Sample description")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "985.50"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testInitUpdateForm() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/edit", AdvertisementControllerTests.TEST_ADVERTISEMENT_ID))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeExists("advertisementForm"))
            .andExpect(MockMvcResultMatchers.model().attribute("advertisementForm", Matchers.hasProperty("title", Matchers.is("Sample title"))))
            .andExpect(MockMvcResultMatchers.model().attribute("advertisementForm", Matchers.hasProperty("description", Matchers.is("Sample description"))))
            .andExpect(MockMvcResultMatchers.model().attribute("advertisementForm", Matchers.hasProperty("requirements", Matchers.is("Sample requirements"))))
            .andExpect(MockMvcResultMatchers.model().attribute("advertisementForm", Matchers.hasProperty("pricePerMonth", Matchers.is(985.50))))
            .andExpect(MockMvcResultMatchers.view().name("advertisements/createOrUpdateAdvertisementForm"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testInitUpdateFormThrowExceptionWithWrongHost() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/edit", AdvertisementControllerTests.TEST_ADVERTISEMENT_ID))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessUpdateFormSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/advertisements/{advertisementId}/edit", AdvertisementControllerTests.TEST_ADVERTISEMENT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .param("title", "A different title")
            .param("description", "A different description")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "670.99"))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        BDDMockito.then(this.advertisementService).should().saveAdvertisement(ArgumentMatchers.isA(Advertisement.class));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessUpdateFormWithErrors() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/flats/{flatId}/advertisements/new", AdvertisementControllerTests.TEST_FLAT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .param("title", "")
            .param("description", "A different description")
            .param("pricePerMonth", "670.99"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasErrors("advertisementForm"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("advertisementForm", "title"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("advertisementForm", "requirements"))
            .andExpect(MockMvcResultMatchers.view().name("advertisements/createOrUpdateAdvertisementForm"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testProcessUpdateFormThrowExceptionWithWrongHost() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/advertisements/{advertisementId}/edit", AdvertisementControllerTests.TEST_ADVERTISEMENT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .param("title", "A different title")
            .param("description", "A different description")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "670.99"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessDeleteAdvertisementSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/delete", AdvertisementControllerTests.TEST_ADVERTISEMENT_ID))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
        BDDMockito.then(this.advertisementService).should().deleteAdvertisement(ArgumentMatchers.isA(Advertisement.class));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testProcessDeleteAdvertisementThrowExceptionWithWrongHost() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/delete", AdvertisementControllerTests.TEST_ADVERTISEMENT_ID))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testShowAdvertisement() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}", AdvertisementControllerTests.TEST_ADVERTISEMENT_ID))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeExists("advertisement"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("images"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("host"))
            .andExpect(MockMvcResultMatchers.view().name("advertisements/advertisementDetails"));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENANT"})
    @Test
    void testShowAdvertisementAsTenant() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}", AdvertisementControllerTests.TEST_ADVERTISEMENT_ID))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeExists("advertisement"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("images"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("host"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("requestMade"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("hasFlat"))
            .andExpect(MockMvcResultMatchers.view().name("advertisements/advertisementDetails"));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENANT"})
    @Test
    void testProcessFindFormSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements")
            .param("city", AdvertisementControllerTests.TEST_CITY_FLAT + ", " + AdvertisementControllerTests.TEST_COUNTRY_FLAT)
            .param("postalCode", AdvertisementControllerTests.TEST_POSTAL_CODE_FLAT))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
            .andExpect(MockMvcResultMatchers.view().name("advertisements/advertisementsList"));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENANT"})
    @Test
    void testProcessFindFormWithErrors() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("address","city"))
            .andExpect(MockMvcResultMatchers.view().name("welcome"));
    }
}
