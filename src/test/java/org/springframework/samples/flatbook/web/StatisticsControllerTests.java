
package org.springframework.samples.flatbook.web;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Statistics;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.service.StatisticsService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = StatisticsController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class StatisticsControllerTests {

	@Autowired
	private MockMvc					mockMvc;

	@MockBean
	private StatisticsService		statisticsService;

	private static final Integer	ID_1				= 1;
	private static final Integer	ID_2				= 2;
	private static final Integer	ID_3				= 3;

	private static final String		HOST_USERNAME_1		= "host1";
	private static final String		HOST_USERNAME_2		= "host2";
	private static final String		HOST_USERNAME_3		= "host3";

	private static final String		TENANT_USERNAME_1	= "tenant1";
	private static final String		TENANT_USERNAME_2	= "tenant2";
	private static final String		TENANT_USERNAME_3	= "tenant3";


	@BeforeEach
	void setup() {
		Flat flat1 = new Flat();
		flat1.setId(StatisticsControllerTests.ID_1);

		Flat flat2 = new Flat();
		flat1.setId(StatisticsControllerTests.ID_2);

		Flat flat3 = new Flat();
		flat1.setId(StatisticsControllerTests.ID_3);

		Host host1 = new Host();
		host1.setUsername(StatisticsControllerTests.HOST_USERNAME_1);

		Host host2 = new Host();
		host1.setUsername(StatisticsControllerTests.HOST_USERNAME_2);

		Host host3 = new Host();
		host1.setUsername(StatisticsControllerTests.HOST_USERNAME_3);

		Tenant tenant1 = new Tenant();
		tenant1.setUsername(StatisticsControllerTests.TENANT_USERNAME_1);

		Tenant tenant2 = new Tenant();
		tenant2.setUsername(StatisticsControllerTests.TENANT_USERNAME_2);

		Tenant tenant3 = new Tenant();
		tenant3.setUsername(StatisticsControllerTests.TENANT_USERNAME_3);

		Statistics statistics = new Statistics();
		statistics.setNumberOfAdvertisements(5);
		statistics.setNumberOfFlats(7);
		statistics.setNumberOfRequests(8);
		statistics.setNumberOfUsers(6);
		statistics.setRatioOfAcceptedRequests(0.25d);
		statistics.setRatioOfCanceledRequests(0.125d);
		statistics.setRatioOfFinishedRequests(0.25d);
		statistics.setRatioOfRejectedRequests(0.125d);
		statistics.setRatioOfFlatsWithAdvertisement(5d / 7d);
		statistics.setTopThreeBestReviewedFlats(Arrays.asList(flat1, flat2, flat3));
		statistics.setTopThreeBestReviewedHosts(Arrays.asList(host1, host2, host3));
		statistics.setTopThreeBestReviewedTenants(Arrays.asList(tenant1, tenant2, tenant3));
		statistics.setTopThreeMostReportedUsers(Arrays.asList(tenant2, host1, host3));
		statistics.setTopThreeWorstReviewedFlats(Arrays.asList(flat1, flat2, flat3));
		statistics.setTopThreeWorstReviewedHosts(Arrays.asList(host1, host2, host3));
		statistics.setTopThreeWorstReviewedTenants(Arrays.asList(tenant1, tenant2, tenant3));

		BDDMockito.given(this.statisticsService.findStatistics()).willReturn(statistics);
	}

	@WithMockUser(value = "spring-admin", roles = {
		"ADMIN"
	})
	@Test
	void testGetStatistics() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/statistics")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("statistics"))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("numberOfRequests", Matchers.is(8))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("numberOfFlats", Matchers.is(7))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("numberOfAdvertisements", Matchers.is(5))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("numberOfUsers", Matchers.is(6))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("ratioOfAcceptedRequests", Matchers.is(0.25d))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("ratioOfCanceledRequests", Matchers.is(0.125d))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("ratioOfFinishedRequests", Matchers.is(0.25d))))
			.andExpect(MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("ratioOfRejectedRequests", Matchers.is(0.125d))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("statistics", Matchers.hasProperty("ratioOfFlatsWithAdvertisement", Matchers.is(5d / 7d))))
			.andExpect(MockMvcResultMatchers.view().name("statistics/statisticsList"));
	}
}
