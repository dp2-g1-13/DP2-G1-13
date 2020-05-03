package org.springframework.samples.flatbook.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StatisticsController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration= SecurityConfiguration.class)
public class StatisticsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatisticsController statisticsController;

    @MockBean
    private StatisticsService statisticsService;

    private static final Integer ID_1 = 1;
    private static final Integer ID_2 = 2;
    private static final Integer ID_3 = 3;

    private static final String HOST_USERNAME_1 = "host1";
    private static final String HOST_USERNAME_2 = "host2";
    private static final String HOST_USERNAME_3 = "host3";

    private static final String TENANT_USERNAME_1 = "tenant1";
    private static final String TENANT_USERNAME_2 = "tenant2";
    private static final String TENANT_USERNAME_3 = "tenant3";

    @BeforeEach
    void setup() {
        Flat flat1 = new Flat();
        flat1.setId(ID_1);

        Flat flat2 = new Flat();
        flat1.setId(ID_2);

        Flat flat3 = new Flat();
        flat1.setId(ID_3);

        Host host1 = new Host();
        host1.setUsername(HOST_USERNAME_1);

        Host host2 = new Host();
        host1.setUsername(HOST_USERNAME_2);

        Host host3 = new Host();
        host1.setUsername(HOST_USERNAME_3);

        Tenant tenant1 = new Tenant();
        tenant1.setUsername(TENANT_USERNAME_1);

        Tenant tenant2 = new Tenant();
        tenant2.setUsername(TENANT_USERNAME_2);

        Tenant tenant3 = new Tenant();
        tenant3.setUsername(TENANT_USERNAME_3);

        Statistics statistics = new Statistics();
        statistics.setNumberOfAdvertisements(5);
        statistics.setNumberOfFlats(7);
        statistics.setNumberOfRequests(8);
        statistics.setNumberOfUsers(6);
        statistics.setRatioOfAcceptedRequests(0.25d);
        statistics.setRatioOfCanceledRequests(0.125d);
        statistics.setRatioOfFinishedRequests(0.25d);
        statistics.setRatioOfRejectedRequests(0.125d);
        statistics.setRatioOfFlatsWithAdvertisement(5d/7d);
        statistics.setTopThreeBestReviewedFlats(Arrays.asList(flat1, flat2, flat3));
        statistics.setTopThreeBestReviewedHosts(Arrays.asList(host1, host2, host3));
        statistics.setTopThreeBestReviewedTenants(Arrays.asList(tenant1, tenant2, tenant3));
        statistics.setTopThreeMostReportedUsers(Arrays.asList(tenant2, host1, host3));
        statistics.setTopThreeWorstReviewedFlats(Arrays.asList(flat1, flat2, flat3));
        statistics.setTopThreeWorstReviewedHosts(Arrays.asList(host1, host2, host3));
        statistics.setTopThreeWorstReviewedTenants(Arrays.asList(tenant1, tenant2, tenant3));

        given(this.statisticsService.findStatistics()).willReturn(statistics);
    }

    @WithMockUser(value = "spring-admin", roles = {"ADMIN"})
    @Test
    void testGetStatistics() throws Exception {
        mockMvc.perform(get("/statistics"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("statistics"))
            .andExpect(model().attribute("statistics", hasProperty("numberOfRequests", is(8))))
            .andExpect(model().attribute("statistics", hasProperty("numberOfFlats", is(7))))
            .andExpect(model().attribute("statistics", hasProperty("numberOfAdvertisements", is(5))))
            .andExpect(model().attribute("statistics", hasProperty("numberOfUsers", is(6))))
            .andExpect(model().attribute("statistics", hasProperty("ratioOfAcceptedRequests", is(0.25d))))
            .andExpect(model().attribute("statistics", hasProperty("ratioOfCanceledRequests", is(0.125d))))
            .andExpect(model().attribute("statistics", hasProperty("ratioOfFinishedRequests", is(0.25d))))
            .andExpect(model().attribute("statistics", hasProperty("ratioOfRejectedRequests", is(0.125d))))
            .andExpect(model().attribute("statistics", hasProperty("ratioOfFlatsWithAdvertisement", is(5d/7d))))
            .andExpect(view().name("statistics/statisticsList"));
    }
}
