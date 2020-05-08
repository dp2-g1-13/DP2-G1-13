package org.springframework.samples.flatbook.service.integration;

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

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class StatisticsServiceTests {

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
        Assertions.assertThat(statistics.getTopThreeMostReportedUsers()).extracting(Person::getUsername)
            .containsExactlyInAnyOrder("aarnoldi7", "ahollows1l", "cmingusn");
        Assertions.assertThat(statistics.getTopThreeBestReviewedTenants()).extracting(Tenant::getUsername)
            .containsExactlyInAnyOrder("acordingly22", "afahrenbach25", "anund5");
        Assertions.assertThat(statistics.getTopThreeBestReviewedHosts()).extracting(Host::getUsername)
            .containsExactlyInAnyOrder("bputleyc", "fricart1", "glikly4");
        Assertions.assertThat(statistics.getTopThreeBestReviewedFlats()).extracting(Flat::getAvailableServices)
            .containsExactlyInAnyOrder("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin interdum mauris non ligula pellentesque ultrices.",
                "Vestibulum rutrum rutrum neque. Aenean auctor gravida sem.", "Vivamus tortor. Duis mattis egestas metus.");

    }
}
