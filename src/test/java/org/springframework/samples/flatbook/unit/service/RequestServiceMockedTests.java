package org.springframework.samples.flatbook.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.samples.flatbook.service.RequestService;

@ExtendWith(MockitoExtension.class)
class RequestServiceMockedTests {

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
        request.setFinishDate(LocalDate.of(10000, 12, 1));
    }

    @BeforeEach
    void instantiateMockService() {
        this.requestServiceMockito = new RequestService(mockedRequestRepository);
    }

    @Test
    void shouldFindRequestsByTenantUsername() {
        when(this.requestServiceMockito.findRequestsByTenantUsername("tenant1")).thenReturn(Collections.singleton(request));
        Set<Request> requests = this.requestServiceMockito.findRequestsByTenantUsername("tenant1");
        Assertions.assertThat(requests.size()).isEqualTo(1);
        Assertions.assertThat(requests).extracting(Request::getDescription)
            .containsExactlyInAnyOrder("Sample description");
    }

    @ParameterizedTest
    @ValueSource(strings = {"host1", ""})
    void shouldNotFindRequestsByTenantUsername(String username) {
        when(this.mockedRequestRepository.findManyByTenantUsername(anyString())).thenReturn(new HashSet<>());
        Set<Request> requests = this.requestServiceMockito.findRequestsByTenantUsername(username);
        Assertions.assertThat(requests).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
        "tenant1, 1",
        "tenant2, 2"
    })
    void shouldAssertThatThereIsRequestOfTenantByFlatId(String username, int flatId) {
        Mockito.lenient().when(this.mockedRequestRepository.isThereRequestOfTenantByFlatId("tenant1", 1)).thenReturn(true);
        Mockito.lenient().when(this.mockedRequestRepository.isThereRequestOfTenantByFlatId("tenant2", 2)).thenReturn(true);
        Boolean b = this.requestServiceMockito.isThereRequestOfTenantByFlatId(username, flatId);
        Assertions.assertThat(b).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "rdunleavy0, 2",
        "host1, 1",
        "'', 3",
    })
    void shouldAssertThatThereIsNotRequestOfTenantByFlatId(String username, int flatId) {
        when(this.mockedRequestRepository.isThereRequestOfTenantByFlatId(anyString(), anyInt())).thenReturn(false);
        Boolean b = this.requestServiceMockito.isThereRequestOfTenantByFlatId(username, flatId);
        Assertions.assertThat(b).isFalse();
    }

    @Test
    void shouldFindRequestById() {
        when(this.mockedRequestRepository.findById(1)).thenReturn(request);

        Request request = this.requestServiceMockito.findRequestById(1);
        assertThat(request).hasId(1);
        assertThat(request).hasDescription("Sample description");
        assertThat(request).hasStatus(RequestStatus.PENDING);

        verify(this.mockedRequestRepository).findById(1);
    }

    @Test
    void shouldNotFindRequestById() {
        Request request = this.requestServiceMockito.findRequestById(100);
        Assertions.assertThat(request).isNull();

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
        Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null");
        verify(this.mockedRequestRepository).save(null);
    }


}
