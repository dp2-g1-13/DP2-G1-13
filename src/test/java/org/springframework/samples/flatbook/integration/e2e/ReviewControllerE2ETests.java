
package org.springframework.samples.flatbook.integration.e2e;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.flatbook.model.enums.ReviewType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ReviewControllerE2ETests {

	private static final String		HOST_USER					= "HOST";
	private static final String		ADMIN_USER					= "ADMIN";
	private static final String		TENANT_USER					= "TENANT";

	private static final String		TEST_CREATOR_USERNAME		= "rdunleavy0";
	private static final String		TEST_REVIEWED_USERNAME		= "dballchin1";
	private static final String		TEST_CREATOR_HOST_USERNAME	= "rbordessa0";
	private static final Integer	TEST_FLAT_ID				= 1;

	private static final Integer	TEST_FLATREVIEW_ID			= 181;
	private static final Integer	TEST_TENANTREVIEW_ID		= 182;
	private static final Integer	TEST_BAD_FLAT_ID			= -1;

	private static final String		TEST_NOTALLOWED_USERNAME	= "uzanioletti1j";

	@Autowired
	private MockMvc					mockMvc;


	@WithMockUser(username = ReviewControllerE2ETests.ADMIN_USER, authorities = {
		ReviewControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(1)
	void testInitCreationFormReviewForbbidenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(2)
	void testInitCreationFormFlatReview() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/new?flatId={flatId}", ReviewControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("reviews/createOrUpdateReviewForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("reviewForm"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_HOST_USERNAME, authorities = {
		ReviewControllerE2ETests.HOST_USER
	})
	@Test
	@Order(3)
	void testInitCreationFormTenantReview() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/new?tenantId={tenantId}", ReviewControllerE2ETests.TEST_REVIEWED_USERNAME))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("reviews/createOrUpdateReviewForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("reviewForm"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(4)
	void testInitCreationFormTenantReviewByATenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/new?tenantId={tenantId}", ReviewControllerE2ETests.TEST_REVIEWED_USERNAME))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("reviews/createOrUpdateReviewForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("reviewForm"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(5)
	void testInitCreationFormThrowExceptionBadFlatId() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/new?flatId={flatId}", ReviewControllerE2ETests.TEST_BAD_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(6)
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reviews/new?flatId={flatId}", ReviewControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("creationDate", "")
				.param("creator", ReviewControllerE2ETests.TEST_CREATOR_USERNAME).param("description", "description").param("rate", "4")
				.param("reviewed", ReviewControllerE2ETests.TEST_FLAT_ID.toString()).param("type", "FLAT_REVIEW"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("reviewForm"))
			.andExpect(MockMvcResultMatchers.view().name("reviews/createOrUpdateReviewForm"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(7)
	void testProcessCreationFormSuccessFlatReview() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reviews/new?flatId={flatId}", ReviewControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("creationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.param("creator", ReviewControllerE2ETests.TEST_CREATOR_USERNAME).param("description", "description").param("rate", "4")
				.param("reviewed", ReviewControllerE2ETests.TEST_FLAT_ID.toString()).param("type", "FLAT_REVIEW"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_HOST_USERNAME, authorities = {
		ReviewControllerE2ETests.HOST_USER
	})
	@Test
	@Order(8)
	void testProcessCreationFormSuccessTenantReview() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reviews/new?tenantId={tenantId}", ReviewControllerE2ETests.TEST_REVIEWED_USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("creationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.param("creator", ReviewControllerE2ETests.TEST_CREATOR_USERNAME).param("description", "description").param("rate", "4")
				.param("reviewed", ReviewControllerE2ETests.TEST_REVIEWED_USERNAME).param("type", "TENANT_REVIEW"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(9)
	void testProcessCreationFormThrowExceptionNotAllowed() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reviews/new?flatId={flatId}", ReviewControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("creationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.param("creator", ReviewControllerE2ETests.TEST_NOTALLOWED_USERNAME).param("description", "description").param("rate", "4")
				.param("reviewed", ReviewControllerE2ETests.TEST_FLAT_ID.toString()).param("type", "FLAT_REVIEW"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(10)
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/{reviewId}/edit", ReviewControllerE2ETests.TEST_FLATREVIEW_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("reviewForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("reviewForm", Matchers.hasProperty("description", Matchers.is("description"))))
			.andExpect(MockMvcResultMatchers.model().attribute("reviewForm", Matchers.hasProperty("rate", Matchers.is(4))))
			.andExpect(MockMvcResultMatchers.model().attribute("reviewForm", Matchers.hasProperty("creationDate", Matchers.is(LocalDate.now()))))
			.andExpect(MockMvcResultMatchers.model().attribute("reviewForm", Matchers.hasProperty("type", Matchers.is(ReviewType.FLAT_REVIEW))))
			.andExpect(MockMvcResultMatchers.view().name("reviews/createOrUpdateReviewForm"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(11)
	void testInitUpdateFormThrowExceptionNotAllowed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/{reviewId}/edit", ReviewControllerE2ETests.TEST_FLATREVIEW_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(12)
	void testProcessUpdateFormSuccessFlatReview() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reviews/{reviewId}/edit", ReviewControllerE2ETests.TEST_FLATREVIEW_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("creationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.param("creator", ReviewControllerE2ETests.TEST_CREATOR_USERNAME).param("description", "description edited").param("rate", "4")
				.param("reviewed", ReviewControllerE2ETests.TEST_FLAT_ID.toString()).param("type", "FLAT_REVIEW"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_HOST_USERNAME, authorities = {
		ReviewControllerE2ETests.HOST_USER
	})
	@Test
	@Order(13)
	void testProcessUpdateFormSuccessTenantReview() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reviews/{reviewId}/edit", ReviewControllerE2ETests.TEST_TENANTREVIEW_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("creationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.param("creator", ReviewControllerE2ETests.TEST_CREATOR_HOST_USERNAME).param("description", "description edited").param("rate", "4")
				.param("reviewed", ReviewControllerE2ETests.TEST_REVIEWED_USERNAME).param("type", "TENANT_REVIEW"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(14)
	void testProcessUpdateFormFlatReviewWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reviews/{reviewId}/edit", ReviewControllerE2ETests.TEST_FLATREVIEW_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("creationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.param("creator", ReviewControllerE2ETests.TEST_CREATOR_USERNAME).param("description", "description edited").param("rate", "")
				.param("reviewed", ReviewControllerE2ETests.TEST_FLAT_ID.toString()).param("type", "FLAT_REVIEW"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("reviews/createOrUpdateReviewForm"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(15)
	void testProcessUpdateFormThrowExceptionNotAllowed() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reviews/{reviewId}/edit", ReviewControllerE2ETests.TEST_FLATREVIEW_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("creationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.param("creator", ReviewControllerE2ETests.TEST_NOTALLOWED_USERNAME).param("description", "description edited").param("rate", "4")
				.param("reviewed", ReviewControllerE2ETests.TEST_REVIEWED_USERNAME).param("type", "TENANT_REVIEW"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_HOST_USERNAME, authorities = {
		ReviewControllerE2ETests.HOST_USER
	})
	@Test
	@Order(16)
	void testProcessTenantReviewRemovalSucess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/{reviewId}/delete", ReviewControllerE2ETests.TEST_TENANTREVIEW_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_CREATOR_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(17)
	void testProcessFlatReviewRemovalSucess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/{reviewId}/delete", ReviewControllerE2ETests.TEST_FLATREVIEW_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(18)
	void testProcessFlatReviewRemovalThrowExceptionNotAllowed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/{reviewId}/delete", ReviewControllerE2ETests.TEST_FLATREVIEW_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.TEST_REVIEWED_USERNAME, authorities = {
		ReviewControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(19)
	void testProcessFlatReviewRemovalThrowExceptionBadFlatReviewId() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/{reviewId}/delete", ReviewControllerE2ETests.TEST_BAD_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = ReviewControllerE2ETests.ADMIN_USER, authorities = {
		ReviewControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(20)
	void testProcessCreationFormReviewForbbidenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/reviews/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReviewControllerE2ETests.ADMIN_USER, authorities = {
		ReviewControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(21)
	void testInitUpdateFormReviewForbbidenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reviews/{reviewId}/edit", ReviewControllerE2ETests.TEST_TENANTREVIEW_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReviewControllerE2ETests.ADMIN_USER, authorities = {
		ReviewControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(22)
	void testProcessUpdateFormReviewForbbidenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/reviews/{reviewId}/edit", ReviewControllerE2ETests.TEST_TENANTREVIEW_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReviewControllerE2ETests.ADMIN_USER, authorities = {
		ReviewControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(23)
	void testProcessDeleteFormReviewForbbidenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/reviews/{reviewId}/delete", ReviewControllerE2ETests.TEST_TENANTREVIEW_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
}
