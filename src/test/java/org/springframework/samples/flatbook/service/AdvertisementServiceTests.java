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
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@ExtendWith(MockitoExtension.class)
public class AdvertisementServiceTests {

    @Autowired
    private AdvertisementService advertisementService;

    @Mock
    private AdvertisementRepository mockedAdvertisementRepository;

    private AdvertisementService advertisementServiceMockito;

    private static Advertisement advertisement;

    @BeforeAll
    static void setupMock() {
        Address address = new Address();
        address.setCountry("Spain");
        address.setCity("Sevilla");
        address.setPostalCode("41000");
        address.setAddress("Plaza Nueva");

        DBImage image = new DBImage();
        image.setFilename("a");
        image.setFileType("b");
        image.setData(new byte[]{1, 2, 3});
        Set<DBImage> images = new HashSet<>();
        images.add(image);

        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 30 characters");
        flat.setSquareMeters(100);
        flat.setNumberRooms(3);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and TV");
        flat.setAddress(address);
        flat.setImages(images);

        Request request = new Request();
        request.setStatus(RequestStatus.PENDING);
        request.setDescription("This is a sample description");
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Set<Request> requests = new HashSet<>();
        requests.add(request);

        advertisement = new Advertisement();
        advertisement.setId(1);
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);
        advertisement.setRequests(requests);
    }

    @BeforeEach
    void instantiateMockService() {
        this.advertisementServiceMockito = new AdvertisementService(mockedAdvertisementRepository);
    }

    @Test
    void shouldFindAdvertisementWithFlatId() {
        Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(1);
        assertThat(adv.getId()).isEqualTo(1);
//        assertThat(adv.getRequests().size()).isEqualTo(?);
        assertThat(adv.getPricePerMonth()).isEqualTo(850.9);
        assertThat(adv.getTitle()).isEqualTo("Beautiful flat in San Bernardo");
    }

    @Test
    void shouldNotFindAdvertisementWithFlatIdNotInDatabase() {
        Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(10);
        assertThat(adv).isNull();
    }

    @Test
    void shouldCheckThereIsAdvertisementWithFlatId() {
        Boolean b = this.advertisementService.isAdvertisementWithFlatId(2);
        assertThat(b).isTrue();
    }

    @Test
    void shouldCheckThereIsNotAdvertisementWithFlatId() {
        Boolean b = this.advertisementService.isAdvertisementWithFlatId(20);
        assertThat(b).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4})
    void shouldFindAdvertisementWithRequestId(int requestId) {
        Advertisement adv = this.advertisementService.findAdvertisementWithRequestId(requestId);
        assertThat(adv).isNotNull();
    }

    @Test
    void shouldNotFindAdvertisementWithRequestId() {
        Advertisement adv = this.advertisementService.findAdvertisementWithRequestId(10);
        assertThat(adv).isNull();
    }

    @Test
    void shouldFindAdvertisementsByCity() {
        Set<Advertisement> ads = this.advertisementService.findAdvertisementsByCity("Sevilla");
        assertThat(ads.size()).isEqualTo(2);

        ads = this.advertisementService.findAdvertisementsByCity("Madrid");
        assertThat(ads.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Santiago de Compostela", ""})
    void shouldNotFindAdvertisementsByCity(String city) {
        Set<Advertisement> ads = this.advertisementService.findAdvertisementsByCity(city);
        assertThat(ads.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "Sevilla, 41018",
        "Madrid, 28046"
    })
    void shouldFindAdvertisementsByCityAndPostalCode(String city, String postalCode) {
        Set<Advertisement> ads = this.advertisementService.findAdvertisementsByCityAndPostalCode(city, postalCode);
        assertThat(ads.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @CsvSource({
        "Valencia, 46022",
        "Sevilla, ''",
        "'', 41015",
        "'', ''"
    })
    void shouldNotFindAdvertisementsByCityAndPostalCode(String city, String postalCode) {
        Set<Advertisement> ads = this.advertisementService.findAdvertisementsByCityAndPostalCode(city, postalCode);
        assertThat(ads.isEmpty()).isTrue();
    }

    @Test
    void shouldFindAdvertisementsByCityAndCountry() {
        Set<Advertisement> ads = this.advertisementService.findAdvertisementsByCityAndCountry("Sevilla", "Spain");
        assertThat(ads.size()).isEqualTo(2);
    }

    @ParameterizedTest
    @CsvSource({
        "Sevilla, Colombia",
        "Sevilla, ''",
        "'', Spain",
        "'', ''"
    })
    void shouldFindAdvertisementsByCityAndCountry(String city, String country) {
        Set<Advertisement> ads = this.advertisementService.findAdvertisementsByCityAndCountry(city, country);
        assertThat(ads.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "Sevilla, Spain, 41003",
        "Madrid, Spain, 28046"
    })
    void shouldFindAdvertisementsByCityAndCountryAndPostalCode(String city, String country, String postalCode) {
        Set<Advertisement> ads = this.advertisementService.findAdvertisementsByCityAndCountryAndPostalCode(city, country, postalCode);
        assertThat(ads.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @CsvSource({
        "Sevilla, Spain, 41008",
        "'', Spain, 28046",
        "Sevilla, '', 41018",
        "Madrid, Spain, ''",
        "Sevilla, '', ''",
        "'', Spain, ''",
        "'', '', 41003",
        "'', '', ''"
    })
    void shouldNotFindAdvertisementsByCityAndCountryAndPostalCode(String city, String country, String postalCode) {
        Set<Advertisement> ads = this.advertisementService.findAdvertisementsByCityAndCountryAndPostalCode(city, country, postalCode);
        assertThat(ads.isEmpty()).isTrue();
    }

    @Test
    void shouldFindAdvertisementById() {
        when(this.mockedAdvertisementRepository.findById(1)).thenReturn(advertisement);
        Advertisement advertisement = this.advertisementServiceMockito.findAdvertisementById(1);
        assertThat(advertisement.getId()).isEqualTo(1);
        assertThat(advertisement.getTitle()).isEqualTo("Sample title");
        assertThat(advertisement.getPricePerMonth()).isEqualTo(100.50);
        assertThat(advertisement.getFlat()).isNotNull();
        assertThat(advertisement.getRequests().isEmpty()).isFalse();
    }

    @Test
    void shouldNotFindAdvertisementById() {
        Advertisement advertisement = this.advertisementServiceMockito.findAdvertisementById(100);
        assertThat(advertisement).isNull();
    }

    @Test
    void shouldAddNewAdvertisement() {
        doNothing().when(this.mockedAdvertisementRepository).save(isA(Advertisement.class));
        this.advertisementServiceMockito.saveAdvertisement(advertisement);

        verify(this.mockedAdvertisementRepository).save(advertisement);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTryingToAddNullAdvertisement() {
        doThrow(new IllegalArgumentException("Target object must not be null")).when(this.mockedAdvertisementRepository).save(isNull());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.advertisementServiceMockito.saveAdvertisement(null));
        assertThat(exception.getMessage()).isEqualTo("Target object must not be null");
        verify(this.mockedAdvertisementRepository).save(null);
    }

    @Test
    void shouldDeleteAdvertisement() {
        doNothing().when(this.mockedAdvertisementRepository).delete(isA(Advertisement.class));
        this.advertisementServiceMockito.deleteAdvertisement(advertisement);

        verify(this.mockedAdvertisementRepository).delete(advertisement);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTryingToDeleteNullAdvertisement() {
        doThrow(new IllegalArgumentException("Entity must not be null!")).when(this.mockedAdvertisementRepository).delete(isNull());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.advertisementServiceMockito.deleteAdvertisement(null));
        assertThat(exception.getMessage()).isEqualTo("Entity must not be null!");
        verify(this.mockedAdvertisementRepository).delete(null);

    }

}
