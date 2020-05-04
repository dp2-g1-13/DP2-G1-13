package org.springframework.samples.flatbook.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;
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

        advertisement = new Advertisement();
        advertisement.setId(1);
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);
    }

    @BeforeEach
    void instantiateMockService() {
        this.advertisementServiceMockito = new AdvertisementService(mockedAdvertisementRepository);
    }

    @Test
    void shouldFindAdvertisementWithFlatId() {
        Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(1);
        assertThat(adv).hasId(1);
//        assertThat(adv.getRequests().size()).isEqualTo(?);
        assertThat(adv).hasPricePerMonth(98.);
        assertThat(adv).hasTitle("Advertisement 1");
    }

    @Test
    void shouldNotFindAdvertisementWithFlatIdNotInDatabase() {
        Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(0);
        assertThat(adv).isNull();
    }

    @Test
    void shouldCheckThereIsAdvertisementWithFlatId() {
        Boolean b = this.advertisementService.isAdvertisementWithFlatId(2);
        Assertions.assertThat(b).isTrue();
    }

    @Test
    void shouldCheckThereIsNotAdvertisementWithFlatId() {
        Boolean b = this.advertisementService.isAdvertisementWithFlatId(0);
        Assertions.assertThat(b).isFalse();
    }

    @Test
    void shouldFindAdvertisementsByHost() {
        Set<Advertisement> advertisements = this.advertisementService.findAdvertisementsByHost("rbordessa0");
        Assertions.assertThat(advertisements).hasSize(3);
        Assertions.assertThat(advertisements).extracting(Advertisement::getTitle)
            .containsExactlyInAnyOrder("Advertisement 1", "Advertisement 16", "Advertisement 31");
    }
    @Test
    void shouldNotFindAdvertisementsByHost() {
        Set<Advertisement> advertisements = this.advertisementService.findAdvertisementsByHost("User1");
        Assertions.assertThat(advertisements).isEmpty();
    }
    @Test
    void shouldFindAllAdvertisements() {
        when(this.mockedAdvertisementRepository.findAll()).thenReturn(Collections.singleton(advertisement));
        Set<Advertisement> advertisements = this.advertisementServiceMockito.findAllAdvertisements();
        Assertions.assertThat(advertisements).hasSize(1);
        Assertions.assertThat(advertisements).extracting(Advertisement::getTitle)
            .containsExactly("Sample title");
    }

    @Test
    void shouldFindAdvertisementById() {
        when(this.mockedAdvertisementRepository.findById(1)).thenReturn(advertisement);
        Advertisement advertisement = this.advertisementServiceMockito.findAdvertisementById(1);
        assertThat(advertisement).hasId(1);
        assertThat(advertisement).hasTitle("Sample title");
        assertThat(advertisement).hasPricePerMonth(100.50);
        assertThat(advertisement.getFlat()).isNotNull();
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
        Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null");
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
        Assertions.assertThat(exception.getMessage()).isEqualTo("Entity must not be null!");
        verify(this.mockedAdvertisementRepository).delete(null);

    }

}
