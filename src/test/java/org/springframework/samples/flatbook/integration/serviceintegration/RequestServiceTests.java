package org.springframework.samples.flatbook.integration.serviceintegration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.service.RequestService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.samples.flatbook.utils.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RequestServiceTests {

    @Autowired
    private RequestService requestService;

    private Request request;

    @BeforeEach
    void setup() {
        request = new Request();
        request.setDescription("Sample description");
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.of(9999, 1, 1));
        request.setFinishDate(LocalDate.of(9999, 12, 1));
    }

    @Test
    void shouldFindRequestsByTenantUsername() {
        Set<Request> requests = this.requestService.findRequestsByTenantUsername("rdunleavy0");
        Assertions.assertThat(requests.size()).isEqualTo(1);

        requests = this.requestService.findRequestsByTenantUsername("dballchin1");
        Assertions.assertThat(requests.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"host1", ""})
    void shouldNotFindRequestsByTenantUsername(String username) {
        Set<Request> requests = this.requestService.findRequestsByTenantUsername(username);
        Assertions.assertThat(requests.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "creuble0, 1",
        "hcorday1, 2"
    })
    void shouldAssertThatThereIsRequestOfTenantByFlatId(String username, int flatId) {
        Boolean b = this.requestService.isThereRequestOfTenantByFlatId(username, flatId);
        Assertions.assertThat(b).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "rdunleavy0, 2",
        "host1, 1",
        "'', 3",
    })
    void shouldAssertThatThereIsNotRequestOfTenantByFlatId(String username, int flatId) {
        Boolean b = this.requestService.isThereRequestOfTenantByFlatId(username, flatId);
        Assertions.assertThat(b).isFalse();
    }

    @Test
    void shouldFindRequestById() {
        Request request = this.requestService.findRequestById(1);
        assertThat(request).hasId(1);
        assertThat(request).hasDescription("Mauris sit amet eros.");
        assertThat(request).hasStartDate(LocalDate.of(2020, 3, 31));
        assertThat(request).hasFinishDate(LocalDate.of(2020, 10, 31));
        assertThat(request).hasStatus(RequestStatus.ACCEPTED);
    }

    @Test
    void shouldNotFindRequestById() {
        Request request = this.requestService.findRequestById(0);
        Assertions.assertThat(request).isNull();
    }

    @Test
    void shouldAddNewRequest() {
        this.requestService.saveRequest(request);
        Request requestSaved = this.requestService.findRequestById(request.getId());
        assertThat(requestSaved).hasDescription(request.getDescription());
        assertThat(requestSaved).hasStartDate(request.getStartDate());
        assertThat(requestSaved).hasFinishDate(request.getFinishDate());
        assertThat(requestSaved).hasCreationDate(request.getCreationDate());
        assertThat(requestSaved).hasStatus(request.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenTryingToAddNullAdvertisement() {
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.requestService.saveRequest(null));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null");
    }


}
