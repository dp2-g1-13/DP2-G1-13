
package org.springframework.samples.flatbook.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ReportControllerE2ETests {

	private static final Integer	TEST_REPORT_ID				= 1;
	private static final String		USERNAME1					= "rdunleavy0";
	private static final String		USERNAME2					= "dframmingham2";
	private static final String		ADMIN_USER					= "ADMIN";
	private static final String		TENANT_USER					= "TENANT";
	private static final String		TEST_BAD_REPORTED_USERNAME	= "baddd";

	@Autowired
	private MockMvc					mockMvc;


	@WithMockUser(username = ReportControllerE2ETests.USERNAME1, authorities = ReportControllerE2ETests.TENANT_USER)
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{userId}/new", ReportControllerE2ETests.USERNAME2))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("reports/createOrUpdateReportForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("report"));
	}

	@WithMockUser(username = ReportControllerE2ETests.ADMIN_USER, authorities = ReportControllerE2ETests.ADMIN_USER)
	@Test
	void testInitCreationFormForbiddenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{userId}/new", ReportControllerE2ETests.USERNAME2))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReportControllerE2ETests.USERNAME1, authorities = ReportControllerE2ETests.TENANT_USER)
	@Test
	void testInitCreationFormThrowExceptionBadReportedUsername() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{userId}/new", ReportControllerE2ETests.TEST_BAD_REPORTED_USERNAME))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = ReportControllerE2ETests.ADMIN_USER, authorities = ReportControllerE2ETests.ADMIN_USER)
	@Test
	void testProcessCreationForbiddenForAdmin() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reports/{userId}/new", ReportControllerE2ETests.USERNAME2)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("creationDate", "01/01/2005").param("reason", "reason")
				.param("receiver", ReportControllerE2ETests.USERNAME2).param("sender", ReportControllerE2ETests.ADMIN_USER))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReportControllerE2ETests.USERNAME1, authorities = ReportControllerE2ETests.TENANT_USER)
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reports/{userId}/new", ReportControllerE2ETests.USERNAME2)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("creationDate", "01/01/2005").param("reason", "reason")
				.param("receiver", ReportControllerE2ETests.USERNAME2).param("sender", ReportControllerE2ETests.USERNAME1))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ReportControllerE2ETests.USERNAME1, authorities = ReportControllerE2ETests.TENANT_USER)
	@Test
	void testProcessCreationFormThrowExceptionBadReportedUsername() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reports/{userId}/new", ReportControllerE2ETests.TEST_BAD_REPORTED_USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("creationDate", "01/01/2005").param("reason", "reason")
				.param("receiver", ReportControllerE2ETests.TEST_BAD_REPORTED_USERNAME).param("sender", ReportControllerE2ETests.USERNAME1))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = ReportControllerE2ETests.USERNAME1, authorities = ReportControllerE2ETests.TENANT_USER)
	@Test
	void testProcessCreationFormHasErrors() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/reports/{userId}/new", ReportControllerE2ETests.USERNAME2)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("creationDate", "01/01/2005").param("reason", "")
				.param("sender", ReportControllerE2ETests.USERNAME1))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("report"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("report", "reason"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("report", "receiver"))
			.andExpect(MockMvcResultMatchers.view().name("reports/createOrUpdateReportForm"));
	}

	@WithMockUser(username = ReportControllerE2ETests.ADMIN_USER, authorities = ReportControllerE2ETests.ADMIN_USER)
	@Test
	void testInitReportList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("reports")).andExpect(MockMvcResultMatchers.view().name("reports/reportsList"));
	}

	@WithMockUser(username = ReportControllerE2ETests.USERNAME1, authorities = ReportControllerE2ETests.TENANT_USER)
	@Test
	void testInitReportListForbiddenForNotAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/list")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReportControllerE2ETests.ADMIN_USER, authorities = ReportControllerE2ETests.ADMIN_USER)
	@Test
	void testInitUserReportList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{userId}/list", ReportControllerE2ETests.USERNAME2))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("username"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("reports")).andExpect(MockMvcResultMatchers.view().name("reports/reportsList"));
	}

	@WithMockUser(username = ReportControllerE2ETests.USERNAME1, authorities = ReportControllerE2ETests.TENANT_USER)
	@Test
	void testInitUserReportListForbiddenForNotAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{userId}/list", ReportControllerE2ETests.USERNAME2))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReportControllerE2ETests.ADMIN_USER, authorities = ReportControllerE2ETests.ADMIN_USER)
	@Test
	void testInitUserReportListThrowExceptionBadUserId() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{userId}/list", "baduser")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = ReportControllerE2ETests.USERNAME1, authorities = ReportControllerE2ETests.TENANT_USER)
	@Test
	void testProcessReportRemovalForbiddenForNotAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{reportId}/delete", ReportControllerE2ETests.TEST_REPORT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = ReportControllerE2ETests.ADMIN_USER, authorities = ReportControllerE2ETests.ADMIN_USER)
	@Test
	void testProcessReportRemovalSucess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{reportId}/delete", ReportControllerE2ETests.TEST_REPORT_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ReportControllerE2ETests.ADMIN_USER, authorities = ReportControllerE2ETests.ADMIN_USER)
	@Test
	void testProcessReportRemovalThrowExceptionBadReportId() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reports/{reportId}/delete", 000)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
}
