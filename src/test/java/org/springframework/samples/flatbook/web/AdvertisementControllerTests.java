package org.springframework.samples.flatbook.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.service.*;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdvertisementController.class,
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

    @Autowired
    private AdvertisementController advertisementController;

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
    private RequestService requestService;

    @BeforeEach
    void setup() {
        Address address = new Address();
        address.setCountry(TEST_COUNTRY_FLAT);
        address.setCity(TEST_CITY_FLAT);
        address.setPostalCode(TEST_POSTAL_CODE_FLAT);
        address.setAddress("Plaza Nueva");

        DBImage image = new DBImage();
        image.setFilename("a");
        image.setFileType("b");
        image.setData(new byte[]{1, 2, 3});
        Set<DBImage> images = new HashSet<>();
        images.add(image);

        Flat flat = new Flat();
        flat.setId(TEST_FLAT_ID);
        flat.setDescription("this is a sample description with more than 30 characters");
        flat.setSquareMeters(100);
        flat.setNumberRooms(3);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and TV");
        flat.setAddress(address);
        flat.setImages(images);

        Request request = new Request();
        request.setStatus(RequestStatus.PENDING);
        request.setDescription("This is a sample description");
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Set<Request> requests = new HashSet<>();
        requests.add(request);

        Advertisement advertisement = new Advertisement();
        advertisement.setId(TEST_ADVERTISEMENT_ID);
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(985.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);
        advertisement.setRequests(requests);

        Set<Advertisement> advertisements = new HashSet<>();
        advertisements.add(advertisement);

        Host host = new Host();
        host.setUsername(TEST_HOST_USERNAME);

        Tennant tenant = new Tennant();
        tenant.setUsername(TEST_TENANT_USERNAME);

        given(this.flatService.findFlatById(TEST_FLAT_ID)).willReturn(flat);
        given(this.advertisementService.isAdvertisementWithFlatId(TEST_FLAT_ID)).willReturn(false);
        given(this.hostService.findHostByFlatId(TEST_FLAT_ID)).willReturn(host);
        given(this.advertisementService.findAdvertisementById(TEST_ADVERTISEMENT_ID)).willReturn(advertisement);
        given(this.dbImageService.getImagesByFlatId(TEST_FLAT_ID)).willReturn(images);
        given(this.personService.findUserById(TEST_HOST_USERNAME)).willReturn(host);
        given(this.personService.findUserById(TEST_TENANT_USERNAME)).willReturn(tenant);
        given(this.requestService.isThereRequestOfTenantByAdvertisementId(TEST_TENANT_USERNAME, TEST_ADVERTISEMENT_ID)).willReturn(true);
        given(this.advertisementService.findAdvertisementsByCityAndCountryAndPostalCode(TEST_CITY_FLAT, TEST_COUNTRY_FLAT, TEST_POSTAL_CODE_FLAT)).willReturn(advertisements);
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
        then(this.advertisementService).should().saveAdvertisement(isA(Advertisement.class));
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
        then(this.advertisementService).should().saveAdvertisement(isA(Advertisement.class));
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
        then(this.advertisementService).should().deleteAdvertisement(isA(Advertisement.class));
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

    @WithMockUser(value = "spring-tenant", roles = {"TENNANT"})
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

    @WithMockUser(value = "spring-tenant", roles = {"TENNANT"})
    @Test
    void testProcessFindFormSuccess() throws Exception {
        mockMvc.perform(get("/advertisements")
            .param("city", TEST_CITY_FLAT + ", " + TEST_COUNTRY_FLAT)
            .param("postalCode", TEST_POSTAL_CODE_FLAT))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("selections"))
            .andExpect(view().name("advertisements/advertisementsList"));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENNANT"})
    @Test
    void testProcessFindFormWithErrors() throws Exception {
        mockMvc.perform(get("/advertisements"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("address","city"))
            .andExpect(view().name("welcome"));
    }
}
