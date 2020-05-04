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
import org.springframework.samples.flatbook.model.enums.ReviewType;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.formatters.PersonFormatter;
import org.springframework.samples.flatbook.web.formatters.ReviewFormatter;
import org.springframework.samples.flatbook.web.utils.ReviewUtils;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class,
includeFilters = {@ComponentScan.Filter(value = ReviewUtils.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PersonFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = ReviewFormatter.class, type = FilterType.ASSIGNABLE_TYPE)},
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class ReviewControllerTests {

	private static final Integer TEST_FLATREVIEW_ID = 1;
	private static final Integer TEST_FLATREVIEWTODELETE_ID = 4;
	private static final Integer TEST_FLAT_ID = 1;
	private static final Integer TEST_BAD_FLAT_ID = 99;
    private static final String TEST_CREATOR_USERNAME = "creator";
    private static final String TEST_OTHER_CREATOR_USERNAME = "otherCreator";
    private static final String TEST_CREATOR_HOST_USERNAME = "creatorHost";
    private static final String TEST_NOTALLOWED_USERNAME = "notallowed";
    private static final Integer TEST_TENANTREVIEW_ID = 2;
    private static final Integer TEST_TENANTREVIEWTODELETE_ID = 3;
    private static final String TEST_REVIEWED_USERNAME = "reviewed";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private FlatReviewService flatReviewService;

    @MockBean
    private FlatService flatService;
    
    @MockBean
    private PersonService personService;

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

    	Tenant creator = new Tenant();
    	creator.setUsername(TEST_CREATOR_USERNAME);
    	creator.setEnabled(true);
        Set<Tenant> tenants = new HashSet<>();
        creator.setFlat(flat);
        tenants.add(creator);
        Set<FlatReview> rews = new HashSet<>();
        

        LocalDate creationDate = LocalDate.of(2005, 1, 1);

        FlatReview review = new FlatReview();
    	review.setCreationDate(creationDate);
    	review.setCreator(creator);
    	review.setDescription("description");
    	review.setRate(4);
    	review.setId(TEST_FLATREVIEW_ID);

        Tenant notAllowed = new Tenant();
        notAllowed.setUsername(TEST_NOTALLOWED_USERNAME);

        Tenant otherCreator = new Tenant();
        otherCreator.setUsername(TEST_OTHER_CREATOR_USERNAME);
        otherCreator.setEnabled(true);
        otherCreator.setFlat(flat);
        TenantReview tReviewToDelete = new TenantReview();
        tReviewToDelete.setCreationDate(creationDate);
        tReviewToDelete.setCreator(otherCreator);
        tReviewToDelete.setDescription("description");
        tReviewToDelete.setRate(4);
        tReviewToDelete.setId(TEST_TENANTREVIEWTODELETE_ID);
        FlatReview fReviewToDelete = new FlatReview();
        fReviewToDelete.setCreationDate(creationDate);
        fReviewToDelete.setCreator(otherCreator);
        fReviewToDelete.setDescription("description");
        fReviewToDelete.setRate(4);
        fReviewToDelete.setId(TEST_FLATREVIEWTODELETE_ID);

    	Tenant reviewed = new Tenant();
        reviewed.setUsername(TEST_REVIEWED_USERNAME);
        reviewed.setFlat(flat);
        tenants.add(reviewed);
        tenants.add(otherCreator);
        flat.setTenants(tenants);
        Set<TenantReview> rewss = new HashSet<>();
        rewss.add(tReviewToDelete);
        reviewed.setReviews(rewss);

        Host creatorHost = new Host();
        creatorHost.setUsername(TEST_CREATOR_HOST_USERNAME);
        creatorHost.setEnabled(true);
        Set<Flat> hostFlats = new HashSet<>();
        hostFlats.add(flat);
        creatorHost.setFlats(hostFlats);

        TenantReview tenantReview = new TenantReview();
        tenantReview.setCreationDate(creationDate);
        tenantReview.setCreator(creatorHost);
        tenantReview.setDescription("description");
        tenantReview.setRate(4);
        tenantReview.setId(TEST_TENANTREVIEW_ID);
        
        rews.add(fReviewToDelete);
        flat.setFlatReviews(rews);

        given(this.tenantService.findTenantById(TEST_REVIEWED_USERNAME)).willReturn(reviewed);
        given(this.tenantService.findTenantById(TEST_NOTALLOWED_USERNAME)).willReturn(notAllowed);
        given(this.tenantService.findTenantById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.tenantService.findTenantById(TEST_OTHER_CREATOR_USERNAME)).willReturn(otherCreator);
        given(this.tenantService.findTenantByReviewId(TEST_TENANTREVIEW_ID)).willReturn(reviewed);
        given(this.tenantService.findTenantByReviewId(TEST_TENANTREVIEWTODELETE_ID)).willReturn(reviewed);
        given(this.authoritiesService.findAuthorityById(TEST_CREATOR_USERNAME)).willReturn(AuthoritiesType.TENANT);
        given(this.authoritiesService.findAuthorityById(TEST_CREATOR_HOST_USERNAME)).willReturn(AuthoritiesType.HOST);
        given(this.authoritiesService.findAuthorityById(TEST_NOTALLOWED_USERNAME)).willReturn(AuthoritiesType.TENANT);
        given(this.authoritiesService.findAuthorityById(TEST_OTHER_CREATOR_USERNAME)).willReturn(AuthoritiesType.TENANT);
        given(this.personService.findUserById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.personService.findUserById(TEST_CREATOR_HOST_USERNAME)).willReturn(creatorHost);
        given(this.personService.findUserById(TEST_REVIEWED_USERNAME)).willReturn(reviewed);
        given(this.personService.findUserById(TEST_NOTALLOWED_USERNAME)).willReturn(notAllowed);
        given(this.personService.findUserById(TEST_OTHER_CREATOR_USERNAME)).willReturn(otherCreator);
        given(this.tenantReviewService.findTenantReviewById(TEST_TENANTREVIEW_ID)).willReturn(tenantReview);
        given(this.tenantReviewService.findTenantReviewById(TEST_TENANTREVIEWTODELETE_ID)).willReturn(tReviewToDelete);
        given(this.flatService.findFlatById(TEST_FLAT_ID)).willReturn(flat);
        given(this.flatService.findFlatByReviewId(TEST_FLATREVIEW_ID)).willReturn(flat);
        given(this.flatService.findFlatByReviewId(TEST_FLATREVIEWTODELETE_ID)).willReturn(flat);
        given(this.flatReviewService.findFlatReviewById(TEST_FLATREVIEW_ID)).willReturn(review);
        given(this.flatReviewService.findFlatReviewById(TEST_FLATREVIEWTODELETE_ID)).willReturn(fReviewToDelete);
        given(this.hostService.findHostById(TEST_CREATOR_HOST_USERNAME)).willReturn(creatorHost);
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testInitCreationFormFlatReview() throws Exception {
        mockMvc.perform(get("/reviews/new?flatId={flatId}", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("reviews/createOrUpdateReviewForm"))
            .andExpect(model().attributeExists("reviewForm"));
    }
    
    @WithMockUser(value = TEST_CREATOR_HOST_USERNAME, roles = {"HOST"})
    @Test
    void testInitCreationFormTenantReview() throws Exception {
        mockMvc.perform(get("/reviews/new?tenantId={tenantId}", TEST_REVIEWED_USERNAME))
            .andExpect(status().isOk())
            .andExpect(view().name("reviews/createOrUpdateReviewForm"))
            .andExpect(model().attributeExists("reviewForm"));
    }
    
    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testInitCreationFormTenantReviewByATenant() throws Exception {
        mockMvc.perform(get("/reviews/new?tenantId={tenantId}", TEST_REVIEWED_USERNAME))
            .andExpect(status().isOk())
            .andExpect(view().name("reviews/createOrUpdateReviewForm"))
            .andExpect(model().attributeExists("reviewForm"));
    }
    
    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testInitCreationFormThrowExceptionBadFlatId() throws Exception {
        mockMvc.perform(get("/reviews/new?flatId={flatId}", TEST_BAD_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormSuccessFlatReview() throws Exception {
        mockMvc.perform(post("/reviews/new?flatId={flatId}", TEST_FLAT_ID)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description")
        	.param("rate", "4")
        	.param("reviewed", TEST_FLAT_ID.toString())
        	.param("type", "FLAT_REVIEW"))
            .andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser(value = TEST_CREATOR_HOST_USERNAME, roles = {"HOST"})
    @Test
    void testProcessCreationFormSuccessTenantReview() throws Exception {
        mockMvc.perform(post("/reviews/new?tenantId={tenantId}", TEST_REVIEWED_USERNAME)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description")
        	.param("rate", "4")
        	.param("reviewed", TEST_REVIEWED_USERNAME)
        	.param("type", "TENANT_REVIEW"))
            .andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormThrowExceptionNotAllowed() throws Exception {
        mockMvc.perform(post("/reviews/new?flatId={flatId}", TEST_FLAT_ID)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_NOTALLOWED_USERNAME)
        	.param("description", "description")
        	.param("rate", "4")
        	.param("reviewed", TEST_FLAT_ID.toString())
        	.param("type", "FLAT_REVIEW"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }
    
    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormHasErrors() throws Exception {
        mockMvc.perform(post("/reviews/new?flatId={flatId}", TEST_FLAT_ID)
            .with(csrf())
        	.param("creationDate", "")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description")
        	.param("rate", "4")
        	.param("reviewed", TEST_FLAT_ID.toString())
        	.param("type", "FLAT_REVIEW"))
        	.andExpect(status().isOk())
        	.andExpect(model().attributeHasErrors("reviewForm"))
        	.andExpect(view().name("reviews/createOrUpdateReviewForm"));
    }
    
    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testInitUpdateForm() throws Exception {
        mockMvc.perform(get("/reviews/{reviewId}/edit", TEST_FLATREVIEW_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("reviewForm"))
            .andExpect(model().attribute("reviewForm", hasProperty("description", is("description"))))
            .andExpect(model().attribute("reviewForm", hasProperty("rate", is(4))))
            .andExpect(model().attribute("reviewForm", hasProperty("creationDate", is(LocalDate.of(2005, 1, 1)))))
            .andExpect(model().attribute("reviewForm", hasProperty("type", is(ReviewType.FLAT_REVIEW))))
            .andExpect(model().attribute("reviewForm", hasProperty("creator", is(this.personService.findUserById(TEST_CREATOR_USERNAME)))))
            .andExpect(view().name("reviews/createOrUpdateReviewForm"));
    }
    
    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testInitUpdateFormThrowExceptionNotAllowed() throws Exception {
        mockMvc.perform(get("/reviews/{reviewId}/edit", TEST_FLATREVIEW_ID))
            .andExpect(status().isOk())
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }
    
    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessUpdateFormSuccessFlatReview() throws Exception {
        mockMvc.perform(post("/reviews/{reviewId}/edit", TEST_FLATREVIEW_ID)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description edited")
        	.param("rate", "4")
        	.param("reviewed", TEST_FLAT_ID.toString())
        	.param("type", "FLAT_REVIEW"))
            .andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser(value = TEST_CREATOR_HOST_USERNAME, roles = {"HOST"})
    @Test
    void testProcessUpdateFormSuccessTenantReview() throws Exception {
        mockMvc.perform(post("/reviews/{reviewId}/edit", TEST_TENANTREVIEW_ID)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_HOST_USERNAME)
        	.param("description", "description edited")
        	.param("rate", "4")
        	.param("reviewed", TEST_REVIEWED_USERNAME)
        	.param("type", "TENANT_REVIEW"))
            .andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessUpdateFormFlatReviewWithErrors() throws Exception {
        mockMvc.perform(post("/reviews/{reviewId}/edit", TEST_FLATREVIEW_ID)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description edited")
        	.param("rate", "")
        	.param("reviewed", TEST_FLAT_ID.toString())
        	.param("type", "FLAT_REVIEW"))
        	.andExpect(status().isOk())
        	.andExpect(view().name("reviews/createOrUpdateReviewForm"));
    }
    
    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessUpdateFormThrowExceptionNotAllowed() throws Exception {
        mockMvc.perform(post("/reviews/{reviewId}/edit", TEST_FLATREVIEW_ID)
        	.with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_NOTALLOWED_USERNAME)
        	.param("description", "description edited")
        	.param("rate", "4")
    		.param("reviewed", TEST_REVIEWED_USERNAME)
    		.param("type", "TENANT_REVIEW"))
            .andExpect(status().isOk())
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }
    
    @WithMockUser(value = TEST_OTHER_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessTenantReviewRemovalSucess() throws Exception {
      mockMvc.perform(get("/reviews/{reviewId}/delete", TEST_TENANTREVIEWTODELETE_ID))
          .andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser(value = TEST_OTHER_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessFlatReviewRemovalSucess() throws Exception {
      mockMvc.perform(get("/reviews/{reviewId}/delete", TEST_FLATREVIEWTODELETE_ID))
      	.andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessFlatReviewRemovalThrowExceptionNotAllowed() throws Exception {
      mockMvc.perform(get("/reviews/{reviewId}/delete", TEST_FLATREVIEWTODELETE_ID))
      	.andExpect(status().isOk())
      	.andExpect(status().is2xxSuccessful())
      	.andExpect(view().name("exception"));
    }
    
    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessFlatReviewRemovalThrowExceptionBadFlatReviewId() throws Exception {
      mockMvc.perform(get("/reviews/{reviewId}/delete", TEST_BAD_FLAT_ID))
      	.andExpect(status().isOk())
      	.andExpect(status().is2xxSuccessful())
      	.andExpect(view().name("exception"));
    }
}
