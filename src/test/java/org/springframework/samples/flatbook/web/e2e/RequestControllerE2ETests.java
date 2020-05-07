
package org.springframework.samples.flatbook.web.e2e;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class RequestControllerE2ETests {

	private static final Integer	TEST_REQUEST_ID								= 130;
	private static final Integer	TEST_NEW_REQUEST_ID							= 136;
	private static final Integer	TEST_REQUEST_ACCEPTED_START_DATE_PAST_ID	= 3;
	private static final String		TEST_HOST_USERNAME							= "ochillistone9";
	private static final String		TEST_HOST_WRONG_USERNAME					= "mmcgaheye";
	private static final String		TEST_TENANT_USERNAME						= "itendahl1a8";
	private static final String		TEST_TENANT_WRONG_USERNAME					= "blivzey13";
	private static final Integer	TEST_FLAT_ID								= 40;
	private static final String		TENANT_USER									= "TENANT";
	private static final String		HOST_USER									= "HOST";

	@Autowired
	private MockMvc					mockMvc;


	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(1)
	void testInitCreationFormForbbidenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/new", RequestControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(2)
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/new", RequestControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("requestForm"))
			.andExpect(MockMvcResultMatchers.view().name("requests/createRequestForm"));

	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_WRONG_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(3)
	void testInitCreationFormThrowExceptionWithTenantWithExistingPendingRequest() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/new", RequestControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));

	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(4)
	void testProcessCreationFormForbbidenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/flats/{flatId}/requests/new", RequestControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(5)
	void testProcessCreationFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/flats/{flatId}/requests/new", RequestControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Sample description").param("startDate", "01/05/2018")
				.param("finishDate", "01/01/2018"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("requestForm"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("requestForm", "startDate"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("requestForm", "finishDate"))
			.andExpect(MockMvcResultMatchers.view().name("requests/createRequestForm"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_WRONG_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(6)
	void testProcessCreationFormThrowExceptionWithTenantWithExistingPendingRequest() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/flats/{flatId}/requests/new", RequestControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Sample description").param("startDate", "01/05/2022")
				.param("finishDate", "01/05/2023"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(7)
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/flats/{flatId}/requests/new", RequestControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Sample description").param("startDate", "01/05/2022")
				.param("finishDate", "01/05/2023"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/requests/list"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(8)
	void testProcessAcceptRequestForbbidenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/accept", RequestControllerE2ETests.TEST_FLAT_ID,
			RequestControllerE2ETests.TEST_REQUEST_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(9)
	void testProcessAcceptRequest() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/accept", RequestControllerE2ETests.TEST_FLAT_ID,
				RequestControllerE2ETests.TEST_NEW_REQUEST_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/flats/{flatId}/requests/list"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_WRONG_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(10)
	void testProcessAcceptRequestThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/accept", RequestControllerE2ETests.TEST_FLAT_ID,
				RequestControllerE2ETests.TEST_REQUEST_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(11)
	void testProcessRejectRequestForbbidenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/reject", RequestControllerE2ETests.TEST_FLAT_ID,
			RequestControllerE2ETests.TEST_REQUEST_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(12)
	void testProcessRejectRequest() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/reject", RequestControllerE2ETests.TEST_FLAT_ID,
				RequestControllerE2ETests.TEST_REQUEST_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/flats/{flatId}/requests/list"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_WRONG_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(13)
	void testProcessRejectRequestThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/reject", RequestControllerE2ETests.TEST_FLAT_ID,
				RequestControllerE2ETests.TEST_REQUEST_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(14)
	void testProcessRejectRequestForbbidenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/requests/list", RequestControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(15)
	void testShowRequestsOfTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/requests/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("requests")).andExpect(MockMvcResultMatchers.model().attributeExists("advIds"))
			.andExpect(MockMvcResultMatchers.view().name("requests/requestsList"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(16)
	void testShowRequestsOfFlatForbbidenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/list", RequestControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(17)
	void testShowRequestsOfFlat() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/list", RequestControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("requests"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("tenants")).andExpect(MockMvcResultMatchers.model().attributeExists("flatId"))
			.andExpect(MockMvcResultMatchers.view().name("requests/requestsList"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_WRONG_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(18)
	void testShowRequestsOfFlatThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/list", RequestControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(19)
	void testProcessCancelRequestForbbidenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/cancel", RequestControllerE2ETests.TEST_FLAT_ID,
			RequestControllerE2ETests.TEST_REQUEST_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(20)
	void testProcessCancelRequest() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/cancel", RequestControllerE2ETests.TEST_FLAT_ID,
				RequestControllerE2ETests.TEST_NEW_REQUEST_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/flats/{flatId}/requests/list"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(21)
	void testProcessCancelRequestThrowExceptionWithNoAcceptedRequest() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/cancel", RequestControllerE2ETests.TEST_FLAT_ID,
				RequestControllerE2ETests.TEST_REQUEST_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_TENANT_USERNAME, authorities = {
		RequestControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(22)
	void testProcessConcludeRequestForbbidenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/conclude", RequestControllerE2ETests.TEST_FLAT_ID,
			RequestControllerE2ETests.TEST_REQUEST_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(23)
	void testProcessConcludeRequest() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/conclude", RequestControllerE2ETests.TEST_FLAT_ID,
				RequestControllerE2ETests.TEST_REQUEST_ACCEPTED_START_DATE_PAST_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/flats/{flatId}/requests/list"));
	}

	@WithMockUser(username = RequestControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		RequestControllerE2ETests.HOST_USER
	})
	@Test
	@Order(24)
	void testProcessConcludeRequestThrowExceptionWithNoAcceptedRequest() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/requests/{requestId}/conclude", RequestControllerE2ETests.TEST_FLAT_ID,
				RequestControllerE2ETests.TEST_REQUEST_ACCEPTED_START_DATE_PAST_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));

	}
}
