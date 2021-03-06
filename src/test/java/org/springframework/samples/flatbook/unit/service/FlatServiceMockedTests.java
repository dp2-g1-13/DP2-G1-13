package org.springframework.samples.flatbook.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.samples.flatbook.utils.assertj.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.AddressRepository;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.repository.FlatReviewRepository;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.samples.flatbook.repository.TaskRepository;
import org.springframework.samples.flatbook.repository.TenantRepository;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.utils.EntityUtils;

@ExtendWith(MockitoExtension.class)
class FlatServiceMockedTests {

    @Mock
    private static FlatRepository           flatRepository;

    @Mock
    private static DBImageRepository        dbImageRepository;

    @Mock
    private static AddressRepository        addressRepository;

    @Mock
    private static TenantRepository         tenantRepository;

    @Mock
    private static AdvertisementRepository  advertisementRepository;

    @Mock
    private static RequestRepository		requestRepository;

    @Mock
    private static HostRepository			hostRepository;

    @Mock
    private static TaskRepository          taskRepository;

    @Mock
    private static FlatReviewRepository    flatReviewRepository;

    private Set<Flat> flats;
    private Flat flat1;
    private Flat flat2;
    private Flat flat3;
    private Host host;
    private Tenant tenant;
    private Task task;
    private Set<DBImage> images1;
    private Set<DBImage> images2;

    protected FlatService mockedFlatService;

    @BeforeEach
    void setup() {
        mockedFlatService = new FlatService(flatRepository, dbImageRepository, addressRepository, tenantRepository, advertisementRepository, requestRepository, hostRepository, taskRepository, flatReviewRepository);
        flats = new HashSet<>();
        flat1 = new Flat();
        flat2 = new Flat();
        flat3 = new Flat();

        DBImage dbImage1 = new DBImage();
        DBImage dbImage2 = new DBImage();
        DBImage dbImage3 = new DBImage();
        dbImage1.setFilename("image1");
        dbImage2.setFilename("image2");
        dbImage3.setFilename("image3");



        images1 = new HashSet<>();
        images2 = new HashSet<>();
        Set<DBImage> images3 = new HashSet<>();
        images1.add(dbImage1);
        images2.add(dbImage2);
        images3.add(dbImage3);

        flat1.setId(1);
        flat2.setId(2);
        flat3.setId(3);
        flat1.setDescription("Sample description 1");
        flat2.setDescription("Sample description 2");
        flat3.setDescription("Sample description 3");
        flat1.setImages(images1);
        flat2.setImages(images2);
        flat3.setImages(images3);

        Address address1 = new Address();
        address1.setId(1);

        Request request = new Request();
        request.setId(1);

        tenant = new Tenant();
        tenant.setRequests(new HashSet<>(Collections.singletonList(request)));

        flat1.setAddress(address1);
        flat1.setRequests(new HashSet<>(Collections.singletonList(request)));
        flat1.setTenants(Collections.singleton(tenant));
        flat1.setFlatReviews(new HashSet<>());

        flats.add(flat1);
        flats.add(flat2);
        flats.add(flat3);

        task = new Task();
        task.setId(1);

        host = new Host();
        host.setFlats(flats);
    }

    @Test
    void shouldFindAllFlats() {
        when(flatRepository.findAll()).thenReturn(flats);

        Set<Flat> flats = this.mockedFlatService.findAllFlats();
        Assertions.assertThat(flats.size()).isEqualTo(3);
        Assertions.assertThat(flats).extracting(Flat::getDescription)
            .containsExactlyInAnyOrder("Sample description 1", "Sample description 2", "Sample description 3");

        Flat f1 = EntityUtils.getById(flats, Flat.class, 1);
        Assertions.assertThat(f1.getImages().size()).isEqualTo(1);
        assertThat(f1.getImages().iterator().next()).hasFilename("image1");

        Flat f3 = EntityUtils.getById(flats, Flat.class, 3);
        Assertions.assertThat(f3.getImages().size()).isEqualTo(1);
        assertThat(f3.getImages().iterator().next()).hasFilename("image3");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void shouldFindFlatWithCorrectId(int id) {
        lenient().when(flatRepository.findById(1)).thenReturn(flat1);
        lenient().when(flatRepository.findById(2)).thenReturn(flat2);
        lenient().when(flatRepository.findById(3)).thenReturn(flat3);

        Flat flat = this.mockedFlatService.findFlatById(id);
        assertThat(flat).hasId(id);
        assertThat(flat.getImages().iterator().next()).hasFilename("image" + id);
    }

    @Test
    void shouldNotFindFlat() {
        Flat flat = this.mockedFlatService.findFlatById(50);
        assertThat(flat).isNull();
    }

    @Test
    void shouldFindFlatsOfHost() {
        when(flatRepository.findByHostUsername("host1")).thenReturn(new HashSet<>(Arrays.asList(flat1, flat2, flat3)));
        Set<Flat> flatsOfHost = this.mockedFlatService.findFlatByHostUsername("host1");
        Assertions.assertThat(flatsOfHost).isNotNull();
        Assertions.assertThat(flatsOfHost).isNotEmpty();
        Assertions.assertThat(flatsOfHost.size()).isEqualTo(3);
        Assertions.assertThat(flatsOfHost).extracting(Flat::getDescription)
            .containsExactlyInAnyOrder("Sample description 1", "Sample description 2", "Sample description 3");

    }

    @ParameterizedTest
    @ValueSource(strings = {"host3", ""})
    void shouldNotFindFlatsOfHost(String username) {
        when(flatRepository.findByHostUsername(anyString())).thenReturn(new HashSet<>());
        Set<Flat> flatsOfHost = this.mockedFlatService.findFlatByHostUsername(username);
        Assertions.assertThat(flatsOfHost).isNotNull();
        Assertions.assertThat(flatsOfHost).isEmpty();
    }

    @Test
    void shouldAddNewFlat() {
        doNothing().when(flatRepository).save(isA(Flat.class));
        doNothing().when(dbImageRepository).save(isA(DBImage.class));

        this.mockedFlatService.saveFlat(flat1);

        verify(flatRepository).save(flat1);
        verify(dbImageRepository).save(flat1.getImages().iterator().next());
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to add a null flat")
    void shouldThrowIllegalArgumentExceptionWhenTryingToAddNullFlat() {
        doThrow(new IllegalArgumentException("Target object must not be null")).when(flatRepository).save(isNull());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.mockedFlatService.saveFlat(null));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null");
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to add a null image")
    void shouldThrowIllegalArgumentExceptionWhenTryingToAddNullImageInFlat() {
        flat2.getImages().add(null);
        lenient().doThrow(new IllegalArgumentException("Target object must not be null")).when(dbImageRepository).save(isNull());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.mockedFlatService.saveFlat(flat2));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null");
    }

    @Test
    void shouldFindFlatByReviewId() {
        when(flatRepository.findByReviewId(1)).thenReturn(flat1);
        Flat flat = this.mockedFlatService.findFlatByReviewId(1);
        assertThat(flat).isNotNull();
        assertThat(flat).hasId(1);
        assertThat(flat).hasDescription("Sample description 1");
        assertThat(flat).hasImages(images1);
    }

    @Test
    void shouldNotFindFlatByReviewId() {
        Flat flat = this.mockedFlatService.findFlatByReviewId(0);
        assertThat(flat).isNull();
    }

    @Test
    void shouldFindFlatByRequestId() {
        when(flatRepository.findFlatWithRequestId(2)).thenReturn(flat2);
        Flat flat = this.mockedFlatService.findFlatWithRequestId(2);
        assertThat(flat).isNotNull();
        assertThat(flat).hasId(2);
        assertThat(flat).hasDescription("Sample description 2");
        assertThat(flat).hasImages(images2);
    }

    @Test
    void shouldNotFindFlatByRequestId() {
        Flat flat = this.mockedFlatService.findFlatWithRequestId(0);
        assertThat(flat).isNull();
    }

    @Test
    void shouldDeleteFlat() {
        when(taskRepository.findByFlatId(1)).thenReturn(Collections.singleton(task));
        when(hostRepository.findByFlatId(1)).thenReturn(host);
        when(tenantRepository.findByRequestId(1)).thenReturn(tenant);
        when(advertisementRepository.findAdvertisementWithFlatId(1)).thenReturn(new Advertisement());
        doNothing().when(flatRepository).delete(isA(Flat.class));

        this.mockedFlatService.deleteFlat(flat1);
        verify(hostRepository).findByFlatId(1);
        verify(tenantRepository).findByRequestId(1);
        verify(taskRepository).findByFlatId(1);
        verify(flatRepository).delete(flat1);
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to delete a null flat")
    void shouldThrowNullPointerExceptionWhenTryingToDeleteNullFlat() {
        assertThrows(NullPointerException.class, () -> this.mockedFlatService.deleteFlat(null));
    }


}
