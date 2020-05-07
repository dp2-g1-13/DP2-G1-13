package org.springframework.samples.flatbook.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.utils.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FlatServiceTests {

    @Autowired
    private FlatService flatService;

    private static final int ID = 1;
    private static final int ID_2 = 50;
    private static final String DESCRIPTION = "This is a sample description with more than 30 characters";
    private static final Integer NUMBER_ROOMS = 2;
    private static final Integer NUMBER_BATHS = 2;
    private static final Integer SQUARE_METERS = 100;
    private static final String AVAILABLE_SERVICES = "Service 1, service 2";

    private Flat flat1;

    @BeforeEach
    void setup() {
        flat1 = new Flat();

        Address address = new Address();
        address.setCountry("Spain");
        address.setCity("Sevilla");
        address.setPostalCode("41000");
        address.setAddress("Plaza Nueva");

        DBImage image = new DBImage();
        image.setFilename("a.png");
        image.setFileType("image/png");
        image.setData(new byte[]{1, 2, 3});

        DBImage image2 = new DBImage();
        image2.setFilename("b.png");
        image2.setFileType("image/png");
        image2.setData(new byte[]{1, 2, 3});

        DBImage image3 = new DBImage();
        image3.setFilename("c.png");
        image3.setFileType("image/png");
        image3.setData(new byte[]{1, 2, 3});

        DBImage image4 = new DBImage();
        image4.setFilename("d.png");
        image4.setFileType("image/png");
        image4.setData(new byte[]{1, 2, 3});

        DBImage image5 = new DBImage();
        image5.setFilename("e.png");
        image5.setFileType("image/png");
        image5.setData(new byte[]{1, 2, 3});

        DBImage image6 = new DBImage();
        image6.setFilename("f.png");
        image6.setFileType("image/png");
        image6.setData(new byte[]{1, 2, 3});

        flat1.setDescription(DESCRIPTION);
        flat1.setSquareMeters(SQUARE_METERS);
        flat1.setNumberRooms(NUMBER_ROOMS);
        flat1.setNumberBaths(NUMBER_BATHS);
        flat1.setAvailableServices(AVAILABLE_SERVICES);
        flat1.setImages(new HashSet<>(Arrays.asList(image, image2, image3, image4, image5, image6)));
        flat1.setAddress(address);
    }

    @Test
    void shouldFindAllFlats() {
        Set<Flat> flats = this.flatService.findAllFlats();
        Assertions.assertThat(flats.size()).isEqualTo(45);

        Flat f1 = EntityUtils.getById(flats, Flat.class, ID);
        assertThat(f1).hasAvailableServices("Aenean lectus. Pellentesque eget nunc.");
        assertThat(f1).hasSquareMeters(994);
        assertThat(f1).hasNumberBaths(1);
        assertThat(f1).hasNumberRooms(2);
    }

    @ParameterizedTest
    @CsvSource({
        "1, Aenean lectus. Pellentesque eget nunc., 994, 1, 2",
        "2, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin interdum mauris non ligula pellentesque ultrices.', 201, 2, 3",
        "3, Pellentesque ultrices mattis odio. Donec vitae nisi., 957, 3, 4"
    })
    void shouldFindFlatWithCorrectId(int id, String availableServices, int squareMeters, int numberBaths, int numberRooms) {
        Flat flat = this.flatService.findFlatById(id);
        assertThat(flat).hasId(id);
        assertThat(flat).hasAvailableServices(availableServices);
        assertThat(flat).hasSquareMeters(squareMeters);
        assertThat(flat).hasNumberBaths(numberBaths);
        assertThat(flat).hasNumberRooms(numberRooms);
    }

    @Test
    void shouldNotFindFlat() {
        Flat flat = this.flatService.findFlatById(ID_2);
        assertThat(flat).isNull();
    }

    @Test
    void shouldFindFlatsOfHost() {
        Set<Flat> flatsOfHost = this.flatService.findFlatByHostUsername("rbordessa0");
        Assertions.assertThat(flatsOfHost).isNotNull();
        Assertions.assertThat(flatsOfHost).isNotEmpty();
        Assertions.assertThat(flatsOfHost.size()).isEqualTo(3);
        Assertions.assertThat(flatsOfHost).extracting(Flat::getDescription)
            .containsExactlyInAnyOrder("In hac habitasse platea dictumst. Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo.\n" +
                "\n" +
                "Aliquam quis turpis eget elit sodales scelerisque. Mauris sit amet eros. Suspendisse accumsan tortor quis turpis.",
                "Sed ante. Vivamus tortor. Duis mattis egestas metus.\n" +
                "\n" +
                "Aenean fermentum. Donec ut mauris eget massa tempor convallis. Nulla neque libero, convallis eget, eleifend luctus, ultricies eu, nibh.",
                "Cras non velit nec nisi vulputate nonummy. Maecenas tincidunt lacus at velit. Vivamus vel nulla eget eros elementum pellentesque.\n" +
                "\n" +
                "Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.");

    }

    @ParameterizedTest
    @ValueSource(strings = {"host3", ""})
    void shouldNotFindFlatsOfHost(String username) {
        Set<Flat> flatsOfHost = this.flatService.findFlatByHostUsername(username);
        Assertions.assertThat(flatsOfHost).isNotNull();
        Assertions.assertThat(flatsOfHost).isEmpty();
    }

    @Test
    void shouldAddNewFlat() {
        this.flatService.saveFlat(flat1);
        Flat flatSaved = this.flatService.findFlatById(flat1.getId());
        assertThat(flatSaved).hasDescription(flat1.getDescription());
        assertThat(flatSaved).hasSquareMeters(flat1.getSquareMeters());
        assertThat(flatSaved).hasNumberBaths(flat1.getNumberBaths());
        assertThat(flatSaved).hasNumberRooms(flat1.getNumberRooms());
        assertThat(flatSaved).hasImages(flat1.getImages());
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to add a null flat")
    void shouldThrowExceptionWhenTryingToAddNullFlat() {
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.flatService.saveFlat(null));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null");
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to add a null image")
    void shouldThrowExceptionWhenTryingToAddNullImageInFlat() {
        flat1.getImages().add(null);

        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.flatService.saveFlat(flat1));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null");
    }

    @Test
    void shouldFindFlatByReviewId() {
        Flat flat = this.flatService.findFlatByReviewId(70);
        assertThat(flat).isNotNull();
        assertThat(flat).hasId(35);
        assertThat(flat).hasDescription("Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.\n" +
            "\n" +
            "Phasellus in felis. Donec semper sapien a libero. Nam dui.");
        assertThat(flat).hasSquareMeters(150);
    }

    @Test
    void shouldNotFindFlatByReviewId() {
        Flat flat = this.flatService.findFlatByReviewId(0);
        assertThat(flat).isNull();
    }

    @Test
    void shouldFindFlatByRequestId() {
        Flat flat = this.flatService.findFlatWithRequestId(37);
        assertThat(flat).isNotNull();
        assertThat(flat).hasId(19);
        assertThat(flat).hasDescription("Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero.\n" +
            "\n" +
            "Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh.");
        assertThat(flat).hasSquareMeters(955);
    }

    @Test
    void shouldNotFindFlatByRequestId() {
        Flat flat = this.flatService.findFlatWithRequestId(0);
        assertThat(flat).isNull();
    }

    @Test
    void shouldDeleteFlat() {
        Flat flat = this.flatService.findFlatById(ID);
        this.flatService.deleteFlat(flat);
        flat = this.flatService.findFlatById(ID);
        assertThat(flat).isNull();
    }

    @Test
    @DisplayName("Should throw NullPointerException when trying to delete a null flat")
    void shouldThrowNullPointerExceptionWhenTryingToDeleteNullFlat() {
        assertThrows(NullPointerException.class, () -> this.flatService.deleteFlat(null));
    }


}
