package org.springframework.samples.flatbook.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@ExtendWith(MockitoExtension.class)
public class FlatServiceTests {

    @Autowired
    private FlatService flatService;

    @Mock
    private static FlatRepository flatRepository;

    @Mock
    private static DBImageRepository dbImageRepository;

    private Set<Flat> flats;
    private Set<Flat> flatsOfHost1;
    private Set<Flat> flatsOfHost2;
    private Flat flat1;
    private Flat flat2;
    private Flat flat3;

    protected FlatService mockedFlatService;

    @BeforeEach
    void setup() {
        mockedFlatService = new FlatService(flatRepository, dbImageRepository);
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
        Set<DBImage> images1 = new HashSet<>();
        Set<DBImage> images2 = new HashSet<>();
        Set<DBImage> images3 = new HashSet<>();
        images1.add(dbImage1);
        images2.add(dbImage2);
        images3.add(dbImage3);
        flat1.setId(1);
        flat2.setId(2);
        flat3.setId(3);
        flat1.setImages(images1);
        flat2.setImages(images2);
        flat3.setImages(images3);
        flats.add(flat1);
        flats.add(flat2);
        flats.add(flat3);
        flatsOfHost1 = new HashSet<>(flats);
        flatsOfHost1.remove(flat3);
        flatsOfHost2 = new HashSet<>(flats);
        flatsOfHost2.remove(flat1);
        flatsOfHost2.remove(flat2);
    }

    @Test
    void shouldFindAllFlats() {
        when(flatRepository.findAll()).thenReturn(flats);

        Set<Flat> flats = this.mockedFlatService.findAllFlats();
        assertThat(flats.size()).isEqualTo(3);

        Flat f1 = EntityUtils.getById(flats, Flat.class, 1);
        assertThat(f1.getImages().size()).isEqualTo(1);
        assertThat(f1.getImages().iterator().next().getFilename()).isEqualTo("image1");

        Flat f3 = EntityUtils.getById(flats, Flat.class, 3);
        assertThat(f3.getImages().size()).isEqualTo(1);
        assertThat(f3.getImages().iterator().next().getFilename()).isEqualTo("image3");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void shouldFindFlatWithCorrectId(int id) {
        lenient().when(flatRepository.findById(1)).thenReturn(flat1);
        lenient().when(flatRepository.findById(2)).thenReturn(flat2);
        lenient().when(flatRepository.findById(3)).thenReturn(flat3);

        Flat flat = this.mockedFlatService.findFlatById(id);
        assertThat(flat.getId()).isEqualTo(id);
        assertThat(flat.getImages().iterator().next().getFilename()).isEqualTo("image" + id);
    }

    @Test
    void shouldNotFindFlat() {
        Flat flat = this.mockedFlatService.findFlatById(50);
        assertThat(flat).isNull();
    }

    @Test
    void shouldFindFlatsOfHost() {
        Set<Flat> flatsOfHost = this.flatService.findFlatByHostUsername("host1");
        assertThat(flatsOfHost).isNotNull();
        assertThat(flatsOfHost).isNotEmpty();
        assertThat(flatsOfHost.size()).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"host3", ""})
    void shouldNotFindFlatsOfHost(String username) {
        Set<Flat> flatsOfHost = this.flatService.findFlatByHostUsername(username);
        assertThat(flatsOfHost).isNotNull();
        assertThat(flatsOfHost).isEmpty();
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
        assertThat(exception.getMessage()).isEqualTo("Target object must not be null");
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to add a null image")
    void shouldThrowIllegalArgumentExceptionWhenTryingToAddNullImageInFlat() {
        flat2.getImages().add(null);
        doThrow(new IllegalArgumentException("Target object must not be null")).when(dbImageRepository).save(isNull());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.mockedFlatService.saveFlat(flat2));
        assertThat(exception.getMessage()).isEqualTo("Target object must not be null");
    }


}
