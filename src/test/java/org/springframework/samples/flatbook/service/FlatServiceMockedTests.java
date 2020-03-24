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
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.util.EntityUtils;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlatServiceMockedTests {

    @Mock
    private static FlatRepository flatRepository;

    @Mock
    private static DBImageRepository dbImageRepository;

    private static Set<Flat> flats;
    private static Set<Flat> flatsOfHost1;
    private static Set<Flat> flatsOfHost2;
    private static Flat flat1;
    private static Flat flat2;
    private static Flat flat3;

    protected FlatService flatService;

    @BeforeAll
    static void setupMock() {
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

    @BeforeEach
    void setup() {
        flatService = new FlatService(flatRepository, dbImageRepository);
    }

    @Test
    void shouldFindAllFlats() {
        when(flatRepository.findAll()).thenReturn(flats);

        Set<Flat> flats = this.flatService.findAllFlats();
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

        Flat flat = this.flatService.findFlatById(id);
        assertThat(flat.getId()).isEqualTo(id);
        assertThat(flat.getImages().iterator().next().getFilename()).isEqualTo("image" + id);
    }

    @Test
    void shouldNotFindFlat() {
        Flat flat = this.flatService.findFlatById(50);
        assertThat(flat).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"host1", "host2"})
    void shouldFindFlatsOfHost(String username) {
        lenient().when(flatRepository.findByHostUsername("host1")).thenReturn(flatsOfHost1);
        lenient().when(flatRepository.findByHostUsername("host2")).thenReturn(flatsOfHost2);

        Set<Flat> flatsOfHost = this.flatService.findFlatByHostUsername(username);
        assertThat(flatsOfHost).isNotNull();
        assertThat(flatsOfHost).isNotEmpty();
    }

    @Test
    void shouldNotFindFlatsOfHost() {
        Set<Flat> flatsOfHost = this.flatService.findFlatByHostUsername("host3");
        assertThat(flatsOfHost).isNotNull();
        assertThat(flatsOfHost).isEmpty();
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to find flats of a host with a null username")
    void shouldThrowNullPointerWhenTryingToFindFlatsOfHostWithNullUsername() {
        when(flatRepository.findByHostUsername(isNull())).thenThrow(new NullPointerException("Null username"));

        Exception exception = assertThrows(NullPointerException.class, () -> this.flatService.findFlatByHostUsername(null));
        assertThat(exception.getMessage()).isEqualTo("Null username");
    }

    @Test
    void shouldAddNewFlat() {
        doNothing().when(flatRepository).save(isA(Flat.class));
        doNothing().when(dbImageRepository).save(isA(DBImage.class));

        this.flatService.saveFlat(flat1);

        verify(flatRepository).save(flat1);
        verify(dbImageRepository).save(flat1.getImages().iterator().next());
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to add a null flat")
    void shouldThrowNullPointerWhenTryingToAddNullFlat() {
        doThrow(new NullPointerException("Null flat")).when(flatRepository).save(isNull());

        Exception exception = assertThrows(NullPointerException.class, () -> this.flatService.saveFlat(null));
        assertThat(exception.getMessage()).isEqualTo("Null flat");
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to add a null image")
    void shouldThrowNullPointerWhenTryingToAddNullImageInFlat() {
        flat2.getImages().add(null);
        doThrow(new NullPointerException("Null image")).when(dbImageRepository).save(isNull());

        Exception exception = assertThrows(NullPointerException.class, () -> this.flatService.saveFlat(flat2));
        assertThat(exception.getMessage()).isEqualTo("Null image");
    }


}
