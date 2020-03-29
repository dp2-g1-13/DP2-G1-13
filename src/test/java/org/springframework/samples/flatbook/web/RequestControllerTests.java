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
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.validators.RequestFormValidator;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RequestController.class,
    includeFilters = @ComponentScan.Filter(value = RequestFormValidator.class, type = FilterType.ASSIGNABLE_TYPE),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration= SecurityConfiguration.class)
public class RequestControllerTests {

    private static final Integer TEST_REQUEST_ID = 1;
    private static final String TEST_HOST_USERNAME = "spring";
    private static final String TEST_HOST_WRONG_USERNAME = "spring-wrong";
    private static final String TEST_TENANT_USERNAME = "spring-tenant";
    private static final String TEST_TENANT_WRONG_USERNAME = "spring-tenant-wrong";
    private static final Integer TEST_ADVERTISEMENT_ID = 1;
    private static final Integer TEST_FLAT_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestController requestController;

    @MockBean
    private RequestService requestService;

    @MockBean
    private PersonService personService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @MockBean
    private HostService hostService;

    @MockBean
    private TennantService tenantService;

    @BeforeEach
    void setup() {
        Flat flat = new Flat();
        flat.setId(1);
        flat.setDescription("this is a sample description with more than 30 characters");
        flat.setSquareMeters(100);
        flat.setNumberRooms(3);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and TV");

        Request request = new Request();
        request.setId(TEST_REQUEST_ID);
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

//        Set<Advertisement> advertisements = new HashSet<>();
//        advertisements.add(advertisement);

        Host host = new Host();
        host.setUsername(TEST_HOST_USERNAME);

        Tennant tenant1 = new Tennant();
        tenant1.setUsername(TEST_TENANT_USERNAME);
        tenant1.setRequests(requests);

        Tennant tenant2 = new Tennant();
        tenant2.setUsername(TEST_TENANT_WRONG_USERNAME);
        tenant2.setRequests(requests);

        given(this.personService.findUserById(TEST_TENANT_USERNAME)).willReturn(tenant1);
        given(this.personService.findUserById(TEST_TENANT_WRONG_USERNAME)).willReturn(tenant2);
        given(this.advertisementService.findAdvertisementById(TEST_ADVERTISEMENT_ID)).willReturn(advertisement);
        given(this.requestService.isThereRequestOfTenantByAdvertisementId(TEST_TENANT_USERNAME, TEST_ADVERTISEMENT_ID)).willReturn(false);
        given(this.requestService.isThereRequestOfTenantByAdvertisementId(TEST_TENANT_WRONG_USERNAME, TEST_ADVERTISEMENT_ID)).willReturn(true);
        given(this.authoritiesService.findAuthorityById(TEST_TENANT_USERNAME)).willReturn(AuthoritiesType.TENNANT);
        given(this.authoritiesService.findAuthorityById(TEST_TENANT_WRONG_USERNAME)).willReturn(AuthoritiesType.TENNANT);
        given(this.authoritiesService.findAuthorityById(TEST_HOST_USERNAME)).willReturn(AuthoritiesType.HOST);
        given(this.authoritiesService.findAuthorityById(TEST_HOST_WRONG_USERNAME)).willReturn(AuthoritiesType.HOST);
        given(this.requestService.findRequestById(TEST_REQUEST_ID)).willReturn(request);
        given(this.tenantService.findTenantByRequestId(TEST_REQUEST_ID)).willReturn(tenant1);
        given(this.hostService.findHostOfAdvertisementByRequestId(TEST_REQUEST_ID)).willReturn(host);
        given(this.requestService.findRequestsByTenantUsername(TEST_TENANT_USERNAME)).willReturn(requests);
        given(this.advertisementService.findAdvertisementWithRequestId(TEST_REQUEST_ID)).willReturn(advertisement);
        given(this.hostService.findHostByFlatId(TEST_FLAT_ID)).willReturn(host);
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENNANT"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/requests/new", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("requestForm"))
            .andExpect(view().name("requests/createRequestForm"));

    }

    @WithMockUser(value = "spring-tenant-wrong", roles = {"TENNANT"})
    @Test
    void testInitCreationFormThrowExceptionWithTenantWithExistingPendingRequest() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/requests/new", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));

    }

    @WithMockUser(value = "spring-tenant", roles = {"TENNANT"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/advertisements/{advertisementId}/requests/new", TEST_ADVERTISEMENT_ID)
            .with(csrf())
            .param("description", "Sample description")
            .param("startDate", "01/05/2022")
            .param("finishDate", "01/05/2023"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/requests/list"));
        then(requestService).should().saveRequest(isA(Request.class));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENNANT"})
    @Test
    void testProcessCreationFormWithErrors() throws Exception {
        mockMvc.perform(post("/advertisements/{advertisementId}/requests/new", TEST_ADVERTISEMENT_ID)
            .with(csrf())
            .param("description", "Sample description")
            .param("startDate", "01/05/2018")
            .param("finishDate", "01/01/2018"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("requestForm"))
            .andExpect(model().attributeHasFieldErrors("requestForm", "startDate"))
            .andExpect(model().attributeHasFieldErrors("requestForm", "finishDate"))
            .andExpect(view().name("requests/createRequestForm"));
    }

    @WithMockUser(value = "spring-tenant-wrong", roles = {"TENNANT"})
    @Test
    void testProcessCreationFormThrowExceptionWithTenantWithExistingPendingRequest() throws Exception {
        mockMvc.perform(post("/advertisements/{advertisementId}/requests/new", TEST_ADVERTISEMENT_ID)
            .with(csrf())
            .param("description", "Sample description")
            .param("startDate", "01/05/2022")
            .param("finishDate", "01/05/2023"))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessAcceptRequest() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/requests/{requestId}/accept", TEST_ADVERTISEMENT_ID, TEST_REQUEST_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/advertisements/{advertisementId}/requests/list"));
        then(requestService).should().saveRequest(isA(Request.class));
        then(tenantService).should().saveTenant(isA(Tennant.class));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testProcessAcceptRequestThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/requests/{requestId}/accept", TEST_ADVERTISEMENT_ID, TEST_REQUEST_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessRejectRequest() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/requests/{requestId}/reject", TEST_ADVERTISEMENT_ID, TEST_REQUEST_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/advertisements/{advertisementId}/requests/list"));
        then(requestService).should().saveRequest(isA(Request.class));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testProcessRejectRequestThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/requests/{requestId}/reject", TEST_ADVERTISEMENT_ID, TEST_REQUEST_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = "spring-tenant", roles = {"TENNANT"})
    @Test
    void testShowRequestsOfTenant() throws Exception {
        mockMvc.perform(get("/requests/list"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("requests"))
            .andExpect(model().attributeExists("advIds"))
            .andExpect(view().name("requests/requestsList"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testShowRequestsOfAdvertisement() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/requests/list", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("requests"))
            .andExpect(model().attributeExists("tenants"))
            .andExpect(model().attributeExists("advId"))
            .andExpect(view().name("requests/requestsList"));
    }

    @WithMockUser(value = "spring-wrong", roles = {"HOST"})
    @Test
    void testShowRequestsOfAdvertisementThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/advertisements/{advertisementId}/requests/list", TEST_ADVERTISEMENT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }
}
