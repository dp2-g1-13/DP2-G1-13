package org.springframework.samples.flatbook.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.utils.ReviewUtils;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = AdvertisementController.class,
	includeFilters = {@ComponentScan.Filter(value = ReviewUtils.class, type = FilterType.ASSIGNABLE_TYPE)},
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration= SecurityConfiguration.class)
public class AdvertisementControllerTests {

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

    @BeforeEach
    void setup() {
        Address address = new Address();
        address.setCountry(TEST_COUNTRY_FLAT);
        address.setCity(TEST_CITY_FLAT);
        address.setPostalCode(TEST_POSTAL_CODE_FLAT);
        address.setAddress("Plaza Nueva");
        address.setLatitude(37.3822261);
        address.setLongitude(-6.0123468);

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
        flat.setId(TEST_FLAT_ID);
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
        advertisement.setId(TEST_ADVERTISEMENT_ID);
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(985.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Set<Advertisement> advertisements = new HashSet<>();
        advertisements.add(advertisement);

        Host host = new Host();
        host.setUsername(TEST_HOST_USERNAME);
        host.setEnabled(true);

        Tenant tenant = new Tenant();
        tenant.setUsername(TEST_TENANT_USERNAME);
        tenant.setEnabled(true);
        
        Set<Advertisement> allAdverts = new HashSet<>();
        allAdverts.add(advertisement);

        given(this.flatService.findFlatById(TEST_FLAT_ID)).willReturn(flat);
        given(this.advertisementService.isAdvertisementWithFlatId(TEST_FLAT_ID)).willReturn(false);
        given(this.hostService.findHostByFlatId(TEST_FLAT_ID)).willReturn(host);
        given(this.advertisementService.findAdvertisementById(TEST_ADVERTISEMENT_ID)).willReturn(advertisement);
        given(this.dbImageService.getImagesByFlatId(TEST_FLAT_ID)).willReturn(images);
        given(this.personService.findUserById(TEST_HOST_USERNAME)).willReturn(host);
        given(this.personService.findUserById(TEST_TENANT_USERNAME)).willReturn(tenant);
        given(this.requestService.isThereRequestOfTenantByFlatId(TEST_TENANT_USERNAME, TEST_FLAT_ID)).willReturn(true);
        given(this.authoritiesService.findAuthorityById(TEST_TENANT_USERNAME)).willReturn(AuthoritiesType.TENANT);
        given(this.authoritiesService.findAuthorityById(TEST_HOST_USERNAME)).willReturn(AuthoritiesType.HOST);
        given(this.advertisementService.findAllAdvertisements()).willReturn(allAdverts);
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/advertisements/new", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("advertisements/createOrUpdateAdvertisementForm"))
            .andExpect(model().attributeExists("advertisementForm"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testInitCreationFormThrowsExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/advertisements/new", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/flats/{flatId}/advertisements/new", TEST_FLAT_ID)
            .with(csrf())
            .param("title", "Sample title")
            .param("description", "Sample description")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "985.50"))
            .andExpect(status().is3xxRedirection());
        then(this.advertisementService).should().saveAdvertisement(Mockito.isA(Advertisement.class));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessCreationFormWithErrors() throws Exception {
        mockMvc.perform(post("/flats/{flatId}/advertisements/new", TEST_FLAT_ID)
            .with(csrf())
            .param("title", "Sample title")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "-35.50"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("advertisementForm"))
            .andExpect(model().attributeHasFieldErrors("advertisementForm", "description"))
            .andExpect(model().attributeHasFieldErrors("advertisementForm", "pricePerMonth"))
            .andExpect(view().name("advertisements/createOrUpdateAdvertisementForm"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testProcessCreationFormThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(post("/flats/{flatId}/advertisements/new", TEST_FLAT_ID)
            .with(csrf())
            .param("title", "Sample title")
            .param("description", "Sample description")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "985.50"))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testInitUpdateForm() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/edit", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("advertisementForm"))
            .andExpect(model().attribute("advertisementForm", hasProperty("title", is("Sample title"))))
            .andExpect(model().attribute("advertisementForm", hasProperty("description", is("Sample description"))))
            .andExpect(model().attribute("advertisementForm", hasProperty("requirements", is("Sample requirements"))))
            .andExpect(model().attribute("advertisementForm", hasProperty("pricePerMonth", is(985.50))))
            .andExpect(view().name("advertisements/createOrUpdateAdvertisementForm"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testInitUpdateFormThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/edit", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessUpdateFormSuccess() throws Exception {
        mockMvc.perform(post("/advertisements/{advertisementId}/edit", TEST_ADVERTISEMENT_ID)
            .with(csrf())
            .param("title", "A different title")
            .param("description", "A different description")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "670.99"))
            .andExpect(status().is3xxRedirection());
        then(this.advertisementService).should().saveAdvertisement(Mockito.isA(Advertisement.class));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessUpdateFormWithErrors() throws Exception {
        mockMvc.perform(post("/flats/{flatId}/advertisements/new", TEST_FLAT_ID)
            .with(csrf())
            .param("title", "")
            .param("description", "A different description")
            .param("pricePerMonth", "670.99"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("advertisementForm"))
            .andExpect(model().attributeHasFieldErrors("advertisementForm", "title"))
            .andExpect(model().attributeHasFieldErrors("advertisementForm", "requirements"))
            .andExpect(view().name("advertisements/createOrUpdateAdvertisementForm"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testProcessUpdateFormThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(post("/advertisements/{advertisementId}/edit", TEST_ADVERTISEMENT_ID)
            .with(csrf())
            .param("title", "A different title")
            .param("description", "A different description")
            .param("requirements", "Sample requirements")
            .param("pricePerMonth", "670.99"))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessDeleteAdvertisementSuccess() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/delete", TEST_ADVERTISEMENT_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/"));
        then(this.advertisementService).should().deleteAdvertisement(Mockito.isA(Advertisement.class));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testProcessDeleteAdvertisementThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/delete", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testShowAdvertisement() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("advertisement"))
            .andExpect(model().attributeExists("images"))
            .andExpect(model().attributeExists("host"))
            .andExpect(view().name("advertisements/advertisementDetails"));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENANT"})
    @Test
    void testShowAdvertisementAsTenant() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("advertisement"))
            .andExpect(model().attributeExists("images"))
            .andExpect(model().attributeExists("host"))
            .andExpect(model().attributeExists("requestMade"))
            .andExpect(model().attributeExists("hasFlat"))
            .andExpect(view().name("advertisements/advertisementDetails"));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENANT"})
    @Test
    void testProcessFindFormSuccess() throws Exception {
        mockMvc.perform(get("/advertisements")
            .param("city", TEST_CITY_FLAT + ", " + TEST_COUNTRY_FLAT)
            .param("postalCode", TEST_POSTAL_CODE_FLAT))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("selections"))
            .andExpect(view().name("advertisements/advertisementsList"));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENANT"})
    @Test
    void testProcessFindFormWithErrors() throws Exception {
        mockMvc.perform(get("/advertisements"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("address","city"))
            .andExpect(view().name("welcome"));
    }
}
