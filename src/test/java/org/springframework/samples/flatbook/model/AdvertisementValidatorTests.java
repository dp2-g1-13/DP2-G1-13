package org.springframework.samples.flatbook.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AdvertisementValidatorTests {

    private static Flat flat;

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @BeforeAll
    static void instantiateFlatAndRequests() {
        Address address = new Address();
        address.setCountry("Spain");
        address.setCity("Sevilla");
        address.setPostalCode("41000");
        address.setLocation("Plaza Nueva");

        DBImage image = new DBImage();
        image.setFilename("a");
        image.setFileType("b");
        image.setData(new byte[]{1, 2, 3});
        Set<DBImage> images = new HashSet<>();
        images.add(image);

        flat = new Flat();
        flat.setDescription("this is a sample description with more than 30 characters");
        flat.setSquareMeters(100);
        flat.setNumberRooms(3);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and TV");
        flat.setAddress(address);
        flat.setImages(images);
    }

    @Test
    void shouldNotValidateWhenTitleNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(null);
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("title");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenTitleEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("title");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenDescriptionNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription(null);
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("description");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenDescriptionEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("description");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenRequirementsNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements(null);
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("requirements");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenRequirementsEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("requirements");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenPricePerMonthNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(null);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("pricePerMonth");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1., 0.})
    void shouldNotValidateWhenPricePerMonthNotPositive(double price) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(price);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("pricePerMonth");
        assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
    }

    @Test
    void shouldNotValidateWhenCreationDateNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(null);
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenCreationDateIsInFuture() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.MAX);
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must be a date in the past or in the present");
    }

    @Test
    void shouldNotValidateWhenFlatNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Advertisement> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("flat");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @CsvSource({
        "a, b, 0.01, c",
        "Sample title, Sample description, 650.45, Sample requirements"
    })
    void shouldValidate(String title, String description, Double pricePerMonth, String requirements) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(title);
        advertisement.setDescription(description);
        advertisement.setRequirements(requirements);
        advertisement.setPricePerMonth(pricePerMonth);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);

        Validator validator = createValidator();
        Set<ConstraintViolation<Advertisement>> constraintViolations = validator.validate(advertisement);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
