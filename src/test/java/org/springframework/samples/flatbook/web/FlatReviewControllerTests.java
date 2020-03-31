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
import org.springframework.samples.flatbook.service.*;
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

@WebMvcTest(controllers = FlatReviewController.class,
includeFilters = {@ComponentScan.Filter(value = TenantFormatter.class, type = FilterType.ASSIGNABLE_TYPE)},
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class FlatReviewControllerTests {

	private static final Integer TEST_FLATREVIEW_ID = 1;
	private static final Integer TEST_FLAT_ID = 1;
    private static final String TEST_CREATOR_USERNAME = "creator";
    private static final String TEST_NOTALLOWED_USERNAME = "notallowed";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private FlatReviewService flatReviewService;

    @MockBean
    private FlatService flatService;

    @BeforeEach
    void setup() {

    	Flat flat = new Flat();
    	flat.setId(TEST_FLAT_ID);

    	Tenant creator = new Tenant();
    	creator.setUsername(TEST_CREATOR_USERNAME);
        Set<Tenant> tenants = new HashSet<>();
        creator.setFlat(flat);
        tenants.add(creator);
        flat.setTenants(tenants);
        Set<FlatReview> rews = new HashSet<>();
        flat.setFlatReviews(rews);

        LocalDate creationDate = LocalDate.now();

        FlatReview review = new FlatReview();
    	review.setCreationDate(creationDate);
    	review.setCreator(creator);
    	review.setDescription("description");
    	review.setRate(4);
    	review.setId(TEST_FLATREVIEW_ID);

        Tenant notAllowed = new Tenant();
        notAllowed.setUsername(TEST_NOTALLOWED_USERNAME);

        given(this.tenantService.findTenantById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.tenantService.findTenantById(TEST_NOTALLOWED_USERNAME)).willReturn(notAllowed);
        given(this.flatService.findFlatById(TEST_FLAT_ID)).willReturn(flat);
        given(this.flatReviewService.findFlatReviewById(TEST_FLATREVIEW_ID)).willReturn(review);
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/reviews/new", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("flats/reviews/createOrUpdateFlatReviewForm"))
            .andExpect(model().attributeExists("flatReview"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/flats/{flatId}/reviews/new", TEST_FLAT_ID)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description")
        	.param("rate", "4"))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormHasErrors() throws Exception {

        mockMvc.perform(post("/flats/{flatId}/reviews/new", TEST_FLAT_ID)
            .with(csrf())
            .param("creationDate", "01/01/2005")
        	.param("description", "description")
        	.param("rate", "6"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("flatReview"))
            .andExpect(model().attributeHasFieldErrors("flatReview", "rate"))
            .andExpect(model().attributeHasFieldErrors("flatReview", "creator"))
            .andExpect(view().name("flats/reviews/createOrUpdateFlatReviewForm"));
    }

    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormNotAllowedUser() throws Exception {

        mockMvc.perform(post("/flats/{flatId}/reviews/new", TEST_FLAT_ID)
            .with(csrf())
            .param("creationDate", "01/01/2005")
        	.param("creator", TEST_NOTALLOWED_USERNAME)
        	.param("description", "description")
        	.param("rate", "4"))
        	.andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessTenantReviewRemovalSucess() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/reviews/{flatReviewId}/remove", TEST_FLAT_ID, TEST_FLATREVIEW_ID))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessTenantReviewRemovalNotAllowed() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/reviews/{flatReviewId}/remove", TEST_FLAT_ID, TEST_FLATREVIEW_ID))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }
}
