package org.springframework.samples.flatbook.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AdvertisementServiceMockedTests {

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
        when(this.mockedAdvertisementRepository.findAdvertisementWithFlatId(1)).thenReturn(advertisement);
        Advertisement adv = this.advertisementServiceMockito.findAdvertisementWithFlatId(1);
        assertThat(adv).hasId(1);
//        assertThat(adv.getRequests().size()).isEqualTo(?);
        assertThat(adv).hasPricePerMonth(100.50);
        assertThat(adv).hasTitle("Sample title");
    }

    @Test
    void shouldNotFindAdvertisementWithFlatIdNotInDatabase() {
        Advertisement adv = this.advertisementServiceMockito.findAdvertisementWithFlatId(0);
        assertThat(adv).isNull();
    }

    @Test
    void shouldCheckThereIsAdvertisementWithFlatId() {
        when(this.mockedAdvertisementRepository.isAdvertisementWithFlatId(1)).thenReturn(true);
        Boolean b = this.advertisementServiceMockito.isAdvertisementWithFlatId(1);
        Assertions.assertThat(b).isTrue();
    }

    @Test
    void shouldCheckThereIsNotAdvertisementWithFlatId() {
        when(this.mockedAdvertisementRepository.isAdvertisementWithFlatId(0)).thenReturn(false);
        Boolean b = this.advertisementServiceMockito.isAdvertisementWithFlatId(0);
        Assertions.assertThat(b).isFalse();
    }

    @Test
    void shouldFindAdvertisementsByHost() {
        when(this.mockedAdvertisementRepository.findByHost("host1")).thenReturn(Collections.singleton(advertisement));
        Set<Advertisement> advertisements = this.advertisementServiceMockito.findAdvertisementsByHost("host1");
        Assertions.assertThat(advertisements).hasSize(1);
        Assertions.assertThat(advertisements).extracting(Advertisement::getTitle)
            .containsExactlyInAnyOrder("Sample title");
    }
    @Test
    void shouldNotFindAdvertisementsByHost() {
        when(this.mockedAdvertisementRepository.findByHost("User1")).thenReturn(new HashSet<>());
        Set<Advertisement> advertisements = this.advertisementServiceMockito.findAdvertisementsByHost("User1");
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
