
package org.springframework.samples.flatbook.web.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class StatisticsControllerE2ETests {

	@Autowired
	private MockMvc				mockMvc;

	private static final String	ADMIN_USER	= "ADMIN";
	private static final String	TENANT_USER	= "TENANT";


	@WithMockUser(username = StatisticsControllerE2ETests.ADMIN_USER, authorities = {
		StatisticsControllerE2ETests.ADMIN_USER
	})
	@Test
	void testGetStatistics() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/statistics")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("statistics"))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("numberOfRequests", Matchers.is(135))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("numberOfFlats", Matchers.is(45))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("numberOfAdvertisements", Matchers.is(44))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("numberOfUsers", Matchers.is(152))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics",
				Matchers.hasProperty("ratioOfAcceptedRequests", Matchers.closeTo(2 / 3.0, 0.01))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics",
				Matchers.hasProperty("ratioOfCanceledRequests", Matchers.closeTo(4 / 90.0, 0.01))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("ratioOfFinishedRequests", Matchers.closeTo(0.0, 0.01))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics",
				Matchers.hasProperty("ratioOfRejectedRequests", Matchers.closeTo(2 / 90.0, 0.01))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics",
				Matchers.hasProperty("ratioOfFlatsWithAdvertisement", Matchers.closeTo(1.0, 0.1))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("topThreeBestReviewedFlats", Matchers.hasSize(3))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("topThreeBestReviewedHosts", Matchers.hasSize(3))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("topThreeBestReviewedTenants", Matchers.hasSize(3))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("topThreeMostReportedUsers", Matchers.hasSize(3))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("topThreeWorstReviewedFlats", Matchers.hasSize(3))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("topThreeWorstReviewedHosts", Matchers.hasSize(3))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("topThreeWorstReviewedTenants", Matchers.hasSize(3))))
			.andExpect(MockMvcResultMatchers.view().name("statistics/statisticsList"));
	}

	@WithMockUser(username = StatisticsControllerE2ETests.TENANT_USER, authorities = {
		StatisticsControllerE2ETests.TENANT_USER
	})
	@Test
	void testGetStatisticsForbbidenForNotAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/statistics")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}
}
