package org.springframework.samples.flatbook.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@ExtendWith(MockitoExtension.class)
public class RequestServiceTests {

    @Autowired
    private RequestService requestService;

    @Mock
    private RequestRepository mockedRequestRepository;

    private RequestService requestServiceMockito;

    private static Request request;

    @BeforeAll
    static void setupMock() {
        request = new Request();
        request.setId(1);
        request.setDescription("Sample description");
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.of(10000, 1, 1));
        request.setStartDate(LocalDate.of(10000, 12, 1));
    }

    @BeforeEach
    void instantiateMockService() {
        this.requestServiceMockito = new RequestService(mockedRequestRepository);
    }

    @Test
    void shouldFindRequestsByTenantUsername() {
        Set<Request> requests = this.requestService.findRequestsByTenantUsername("tenant1");
        assertThat(requests.size()).isEqualTo(2);

        requests = this.requestService.findRequestsByTenantUsername("tenant2");
        assertThat(requests.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"host1", ""})
    void shouldNotFindRequestsByTenantUsername(String username) {
        Set<Request> requests = this.requestService.findRequestsByTenantUsername(username);
        assertThat(requests.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "tenant1, 1",
        "tenant2, 1"
    })
    void shouldAssertThatThereIsRequestOfTenantByAdvertisementId(String username, int advertisementId) {
        Boolean b = this.requestService.isThereRequestOfTenantByAdvertisementId(username, advertisementId);
        assertThat(b).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "tenant1, 2",
        "host1, 1",
        "'', 3",
    })
    void shouldAssertThatThereIsNotRequestOfTenantByAdvertisementId(String username, int advertisementId) {
        Boolean b = this.requestService.isThereRequestOfTenantByAdvertisementId(username, advertisementId);
        assertThat(b).isFalse();
    }

    @Test
    void shouldFindRequestById() {
        when(this.mockedRequestRepository.findById(1)).thenReturn(request);

        Request request = this.requestServiceMockito.findRequestById(1);
        assertThat(request.getId()).isEqualTo(1);
        assertThat(request.getDescription()).isEqualTo("Sample description");
        assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDING);

        verify(this.mockedRequestRepository).findById(1);
    }

    @Test
    void shouldNotFindRequestById() {
        Request request = this.requestServiceMockito.findRequestById(100);
        assertThat(request).isNull();

        verify(this.mockedRequestRepository).findById(anyInt());
    }

    @Test
    void shouldAddNewRequest() {
        doNothing().when(this.mockedRequestRepository).save(isA(Request.class));
        this.requestServiceMockito.saveRequest(request);

        verify(this.mockedRequestRepository).save(request);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTryingToAddNullAdvertisement() {
        doThrow(new IllegalArgumentException("Target object must not be null")).when(this.mockedRequestRepository).save(isNull());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.requestServiceMockito.saveRequest(null));
        assertThat(exception.getMessage()).isEqualTo("Target object must not be null");
        verify(this.mockedRequestRepository).save(null);
    }


}
