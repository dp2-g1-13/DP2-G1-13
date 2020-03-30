package org.springframework.samples.flatbook.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.formatters.PersonFormatter;
import org.springframework.samples.flatbook.web.formatters.TenantFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TenantReviewController.class,
includeFilters = {@ComponentScan.Filter(value = TenantFormatter.class, type = FilterType.ASSIGNABLE_TYPE), @ComponentScan.Filter(value = PersonFormatter.class, type = FilterType.ASSIGNABLE_TYPE)},
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class TenantReviewControllerTests {

	private static final Integer TEST_TENANTREVIEW_ID = 1;
	private static final Integer TEST_FLAT_ID = 1;
    private static final String TEST_REVIEWED_USERNAME = "reviewed";
    private static final String TEST_CREATOR_USERNAME = "creator";
    private static final String TEST_NOTALLOWED_USERNAME = "notallowed";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private HostService hostService;

    @MockBean
    private TenantReviewService tenantReviewService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @BeforeEach
    void setup() {

    	Flat flat = new Flat();
    	flat.setId(TEST_FLAT_ID);

    	Tenant reviewed = new Tenant();
        reviewed.setUsername(TEST_REVIEWED_USERNAME);
        Set<Tenant> tenants = new HashSet<>();
        reviewed.setFlat(flat);
        tenants.add(reviewed);
        flat.setTenants(tenants);
        Set<TenantReview> rews = new HashSet<>();
        reviewed.setReviews(rews);

        Host creator = new Host();
        creator.setUsername(TEST_CREATOR_USERNAME);
        Set<Flat> hostFlats = new HashSet<>();
        hostFlats.add(flat);
        creator.setFlats(hostFlats);

        LocalDate creationDate = LocalDate.now();

        TenantReview review = new TenantReview();
    	review.setCreationDate(creationDate);
    	review.setCreator(creator);
    	review.setDescription("description");
    	review.setRate(4);
    	review.setId(TEST_TENANTREVIEW_ID);

        Tenant notAllowed = new Tenant();
        notAllowed.setUsername(TEST_NOTALLOWED_USERNAME);

        given(this.tenantService.findTenantById(TEST_REVIEWED_USERNAME)).willReturn(reviewed);
        given(this.tenantService.findTenantById(TEST_NOTALLOWED_USERNAME)).willReturn(notAllowed);
        given(this.hostService.findHostById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.authoritiesService.findAuthorityById(TEST_CREATOR_USERNAME)).willReturn(AuthoritiesType.HOST);
        given(this.authoritiesService.findAuthorityById(TEST_NOTALLOWED_USERNAME)).willReturn(AuthoritiesType.TENANT);
        given(this.personService.findUserById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.personService.findUserById(TEST_REVIEWED_USERNAME)).willReturn(reviewed);
        given(this.personService.findUserById(TEST_NOTALLOWED_USERNAME)).willReturn(notAllowed);
        given(this.tenantReviewService.findTenantReviewById(TEST_TENANTREVIEW_ID)).willReturn(review);
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"HOST"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/tenants/{tenantId}/reviews/new", TEST_REVIEWED_USERNAME))
            .andExpect(status().isOk())
            .andExpect(view().name("users/reviews/createOrUpdateTenantReviewForm"))
            .andExpect(model().attributeExists("tenantReview"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"HOST"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/tenants/{tenantId}/reviews/new", TEST_REVIEWED_USERNAME)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description")
        	.param("rate", "4"))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"HOST"})
    @Test
    void testProcessCreationFormHasErrors() throws Exception {

        mockMvc.perform(post("/tenants/{tenantId}/reviews/new", TEST_REVIEWED_USERNAME)
            .with(csrf())
            .param("creationDate", "01/01/2005")
        	.param("description", "description")
        	.param("rate", "6"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("tenantReview"))
            .andExpect(model().attributeHasFieldErrors("tenantReview", "rate"))
            .andExpect(model().attributeHasFieldErrors("tenantReview", "creator"))
            .andExpect(view().name("users/reviews/createOrUpdateTenantReviewForm"));
    }

    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormNotAllowedUser() throws Exception {

        mockMvc.perform(post("/tenants/{tenantId}/reviews/new", TEST_REVIEWED_USERNAME)
            .with(csrf())
            .param("creationDate", "01/01/2005")
        	.param("creator", TEST_NOTALLOWED_USERNAME)
        	.param("description", "description")
        	.param("rate", "4"))
        	.andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"HOST"})
    @Test
    void testProcessTenantReviewRemovalSucess() throws Exception {
        mockMvc.perform(get("/tenants/{tenantId}/reviews/{tenantReviewId}/remove", TEST_REVIEWED_USERNAME, TEST_TENANTREVIEW_ID))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessTenantReviewRemovalNotAllowed() throws Exception {
        mockMvc.perform(get("/tenants/{tenantId}/reviews/{tenantReviewId}/remove", TEST_REVIEWED_USERNAME, TEST_TENANTREVIEW_ID))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }
}
