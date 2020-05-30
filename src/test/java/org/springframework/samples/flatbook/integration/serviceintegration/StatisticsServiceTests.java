package org.springframework.samples.flatbook.integration.serviceintegration;

import static org.springframework.samples.flatbook.utils.assertj.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.service.StatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class StatisticsServiceTests {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    strictfp void shouldFindStatistics() {
        Statistics statistics = this.statisticsService.findStatistics();
        assertThat(statistics).isNotNull();
        assertThat(statistics).hasNumberOfRequests(135);
        assertThat(statistics).hasRatioOfAcceptedRequestsCloseTo(2d/3d, 0.01d);
        assertThat(statistics).hasRatioOfRejectedRequestsCloseTo(2d/90d, 0.01d);
        assertThat(statistics).hasRatioOfFinishedRequests(0d);
        assertThat(statistics).hasRatioOfCanceledRequestsCloseTo(4d/90d,  0.01d);
        assertThat(statistics).hasNumberOfAdvertisements(44);
        assertThat(statistics).hasNumberOfFlats(45);
        assertThat(statistics).hasNumberOfUsers(152);
        assertThat(statistics).hasRatioOfFlatsWithAdvertisementCloseTo(44d/45d, 0.01d);
        Assertions.assertThat(statistics.getTopThreeMostReportedUsers()).hasSize(3);
        Assertions.assertThat(statistics.getTopThreeBestReviewedTenants()).hasSize(3);
        Assertions.assertThat(statistics.getTopThreeBestReviewedHosts()).hasSize(3);
        Assertions.assertThat(statistics.getTopThreeBestReviewedFlats()).hasSize(3);
    }
}
