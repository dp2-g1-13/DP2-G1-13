package org.springframework.samples.flatbook.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.*;
import org.springframework.stereotype.Service;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class StatisticsServiceTests {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    strictfp void shouldFindStatistics() {
        Statistics statistics = this.statisticsService.findStatistics();
        assertThat(statistics).isNotNull();
        assertThat(statistics).hasNumberOfRequests(135);
        assertThat(statistics).hasRatioOfAcceptedRequests(2d/3d);
        assertThat(statistics).hasRatioOfRejectedRequests(2d/90d);
        assertThat(statistics).hasRatioOfFinishedRequests(0d);
        assertThat(statistics).hasRatioOfCanceledRequests(4d/90d);
        assertThat(statistics).hasNumberOfAdvertisements(45);
        assertThat(statistics).hasNumberOfFlats(45);
        assertThat(statistics).hasNumberOfUsers(151);
        assertThat(statistics).hasRatioOfFlatsWithAdvertisement(1d);
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
