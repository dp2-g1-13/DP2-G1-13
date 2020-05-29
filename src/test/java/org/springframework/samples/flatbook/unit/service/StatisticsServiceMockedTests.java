package org.springframework.samples.flatbook.unit.service;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Statistics;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.samples.flatbook.repository.TenantRepository;
import org.springframework.samples.flatbook.service.StatisticsService;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceMockedTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HostRepository hostRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private FlatRepository flatRepository;

    @Mock
    private AdvertisementRepository	advertisementRepository;

    private StatisticsService statisticsService;

    private Person person;
    private Host host;
    private Flat flat;
    private Tenant tenant;

    @BeforeEach
    void setupMock() {
        this.person = new Person();
        this.person.setUsername("user");
        this.person.setDni("12345678A");
        this.person.setEmail("user@mail.com");

        this.tenant = new Tenant();
        this.tenant.setUsername("tenant");
        this.tenant.setDni("12345670B");
        this.tenant.setEmail("tenant@mail.com");

        this.host = new Host();
        this.host.setUsername("host");
        this.host.setDni("12345671C");
        this.host.setEmail("host@mail.com");

        this.flat = new Flat();
        this.flat.setDescription("description");
        this.flat.setNumberBaths(1);
        this.flat.setNumberRooms(2);
        this.flat.setSquareMeters(100);

        this.statisticsService = new StatisticsService(personRepository, requestRepository, hostRepository, flatRepository, tenantRepository, advertisementRepository);
    }

    @Test
    void shouldFindStatistics() {
        Mockito.when(this.requestRepository.numberOfRequests()).thenReturn(5);
        Mockito.when(this.requestRepository.ratioOfAcceptedRequests()).thenReturn(1d/5d);
        Mockito.when(this.requestRepository.ratioOfRejectedRequests()).thenReturn(1d/5d);
        Mockito.when(this.requestRepository.ratioOfFinishedRequests()).thenReturn(1d/5d);
        Mockito.when(this.requestRepository.ratioOfCanceledRequests()).thenReturn(1d/5d);
        Mockito.when(this.advertisementRepository.numberOfAdvertisements()).thenReturn(2);
        Mockito.when(this.flatRepository.numberOfFlats()).thenReturn(3);
        Mockito.when(this.personRepository.numberOfUsers()).thenReturn(7);
        Mockito.when(this.personRepository.topMostReportedUsers(ArgumentMatchers.any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(person)));
        Mockito.when(this.hostRepository.topWorstReviewedHosts(ArgumentMatchers.any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(host)));
        Mockito.when(this.hostRepository.topBestReviewedHosts(ArgumentMatchers.any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(host)));
        Mockito.when(this.flatRepository.topBestReviewedFlats(ArgumentMatchers.any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(flat)));
        Mockito.when(this.flatRepository.topWorstReviewedFlats(ArgumentMatchers.any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(flat)));
        Mockito.when(this.tenantRepository.topBestReviewedTenants(ArgumentMatchers.any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(tenant)));
        Mockito.when(this.tenantRepository.topWorstReviewedTenants(ArgumentMatchers.any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(tenant)));

        Statistics statistics = this.statisticsService.findStatistics();
        assertThat(statistics).isNotNull();
        assertThat(statistics).hasNumberOfRequests(5);
        assertThat(statistics).hasRatioOfAcceptedRequests(0.2d);
        assertThat(statistics).hasRatioOfRejectedRequests(0.2d);
        assertThat(statistics).hasRatioOfFinishedRequests(0.2d);
        assertThat(statistics).hasRatioOfCanceledRequests(0.2d);
        assertThat(statistics).hasNumberOfAdvertisements(2);
        assertThat(statistics).hasNumberOfFlats(3);
        assertThat(statistics).hasNumberOfUsers(7);
        assertThat(statistics).hasRatioOfFlatsWithAdvertisement(2d/3d);
        Assertions.assertThat(statistics.getTopThreeMostReportedUsers()).extracting(Person::getUsername)
            .containsExactlyInAnyOrder("user");
        Assertions.assertThat(statistics.getTopThreeBestReviewedTenants()).extracting(Tenant::getUsername)
            .containsExactlyInAnyOrder("tenant");
        Assertions.assertThat(statistics.getTopThreeBestReviewedHosts()).extracting(Host::getUsername)
            .containsExactlyInAnyOrder("host");
        Assertions.assertThat(statistics.getTopThreeBestReviewedFlats()).extracting(Flat::getDescription)
            .containsExactlyInAnyOrder("description");

    }
}
