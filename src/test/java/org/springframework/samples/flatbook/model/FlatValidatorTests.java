package org.springframework.samples.flatbook.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class FlatValidatorTests {

    public static Address address;
    public static DBImage image1;
    public static DBImage image2;
    public static DBImage image3;
    public static DBImage image4;
    public static DBImage image5;
    public static DBImage image6;

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @BeforeAll
    static void instantiateAddressAndImages() {
        address = new Address();
        address.setAddress("Av. de la República Argentina 72");
        address.setCity("Sevilla");
        address.setCountry("Spain");
        address.setPostalCode("41011");

        image1 = new DBImage();
        image1.setFilename("abc");
        image1.setFileType("image/png");
        image1.setData(new byte[]{12, 45, 8, 103});

        image2 = new DBImage();
        image2.setFilename("def");
        image2.setFileType("image/png");
        image2.setData(new byte[]{122, 124, 21, 4});

        image3 = new DBImage();
        image3.setFilename("ghi");
        image3.setFileType("image/png");
        image3.setData(new byte[]{69, 81, 0, 22});

        image4 = new DBImage();
        image4.setFilename("jkl");
        image4.setFileType("image/png");
        image4.setData(new byte[]{127, 5, 72, 5});

        image5 = new DBImage();
        image5.setFilename("mno");
        image5.setFileType("image/png");
        image5.setData(new byte[]{8, 9, 10, 11});

        image6 = new DBImage();
        image6.setFilename("pqr");
        image6.setFileType("image/png");
        image6.setData(new byte[]{12, 13, 14, 15});
    }

    @Test
    void shouldNotValidateWhenDescriptionEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("");
        flat.setSquareMeters(90);
        flat.setNumberRooms(2);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(2);
        assertTrue("Expecting propertyPath to be equal to description but was not.", constraintViolations.stream().allMatch(x -> x.getPropertyPath().toString().equals("description")));
        assertTrue("Expecting one of the constraint violation messages to be equal to 'must not be blank', but was not.", constraintViolations.stream().anyMatch(x -> x.getMessage().equals("must not be blank")));
    }

    @Test
    void shouldNotValidateWhenDescriptionSizeLessThan30() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("sample description w 29 chars");
        flat.setSquareMeters(90);
        flat.setNumberRooms(2);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("description");
        assertThat(violation.getMessage()).isEqualTo("size must be between 30 and 10000");
    }

    @Test
    void shouldNotValidateWhenSquareMetersNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(null);
        flat.setNumberRooms(2);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("squareMeters");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void shouldNotValidateWhenSquareMetersNegativeOrZero(int squareMeters) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(squareMeters);
        flat.setNumberRooms(2);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("squareMeters");
        assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
    }

    @Test
    void shouldNotValidateWhenNumberRoomsNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(90);
        flat.setNumberRooms(null);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("numberRooms");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void shouldNotValidateWhenNumberRoomsNegativeOrZero(int rooms) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(90);
        flat.setNumberRooms(rooms);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("numberRooms");
        assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
    }

    @Test
    void shouldNotValidateWhenNumberBathsNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(90);
        flat.setNumberRooms(2);
        flat.setNumberBaths(null);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("numberBaths");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void shouldNotValidateWhenNumberBathsNegativeOrZero(int baths) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(90);
        flat.setNumberRooms(2);
        flat.setNumberBaths(baths);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("numberBaths");
        assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
    }

    @Test
    void shouldNotValidateWhenAvailableServicesEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(90);
        flat.setNumberRooms(2);
        flat.setNumberBaths(2);
        flat.setAvailableServices("");
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("availableServices");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenAddressNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(90);
        flat.setNumberRooms(2);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(null);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("address");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenImagesNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 29 chars");
        flat.setSquareMeters(90);
        flat.setNumberRooms(2);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        flat.setImages(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("images");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @CsvSource({
        "sample description w 30 charss, 1, 1, 1, a",
        "this is another sample description with more than 30 chars, 90, 2, 2, Wifi and cable TV"
    })
    void shouldValidate(String description, Integer squareMeters, Integer rooms, Integer baths, String availableServices) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Flat flat = new Flat();
        flat.setDescription(description);
        flat.setSquareMeters(squareMeters);
        flat.setNumberRooms(rooms);
        flat.setNumberBaths(baths);
        flat.setAvailableServices(availableServices);
        flat.setAddress(address);
        Set<DBImage> images = new HashSet<>();
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);
        images.add(image6);
        flat.setImages(images);

        Validator validator = createValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }
}
