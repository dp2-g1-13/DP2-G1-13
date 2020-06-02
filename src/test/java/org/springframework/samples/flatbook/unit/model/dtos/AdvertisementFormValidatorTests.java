package org.springframework.samples.flatbook.unit.model.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.dtos.AdvertisementForm;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class AdvertisementFormValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    void shouldNotValidateWhenTitleNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle(null);
        advForm.setDescription("Sample description");
        advForm.setPricePerMonth(250.99);
        advForm.setRequirements("Sample requirements");

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AdvertisementForm> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("title");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenTitleEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle("");
        advForm.setDescription("Sample description");
        advForm.setPricePerMonth(250.99);
        advForm.setRequirements("Sample requirements");

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AdvertisementForm> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("title");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenDescriptionNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle("Sample title");
        advForm.setDescription(null);
        advForm.setPricePerMonth(250.99);
        advForm.setRequirements("Sample requirements");

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AdvertisementForm> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("description");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenDescriptionEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle("Sample title");
        advForm.setDescription("");
        advForm.setPricePerMonth(250.99);
        advForm.setRequirements("Sample requirements");

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AdvertisementForm> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("description");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenPricePerMonthNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle("Sample title");
        advForm.setDescription("Sample description");
        advForm.setPricePerMonth(null);
        advForm.setRequirements("Sample requirements");

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AdvertisementForm> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("pricePerMonth");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1., 0.})
    void shouldNotValidateWhenPricePerMonthNotPositive(double price) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle("Sample title");
        advForm.setDescription("Sample description");
        advForm.setPricePerMonth(price);
        advForm.setRequirements("Sample requirements");

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AdvertisementForm> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("pricePerMonth");
        assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
    }

    @Test
    void shouldNotValidateWhenRequirementsNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle("Sample title");
        advForm.setDescription("Sample description");
        advForm.setPricePerMonth(250.99);
        advForm.setRequirements(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AdvertisementForm> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("requirements");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
        void shouldNotValidateWhenRequirementsEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle("Sample title");
        advForm.setDescription("Sample description");
        advForm.setPricePerMonth(250.99);
        advForm.setRequirements("");

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<AdvertisementForm> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("requirements");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @ParameterizedTest
    @CsvSource({
        "a, b, 0.01, c",
        "Sample title, Sample description, 650.45, Sample requirements"
    })
    void shouldValidate(String title, String description, Double pricePerMonth, String requirements) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        AdvertisementForm advForm = new AdvertisementForm();
        advForm.setTitle(title);
        advForm.setDescription(description);
        advForm.setPricePerMonth(pricePerMonth);
        advForm.setRequirements(requirements);

        Validator validator = createValidator();
        Set<ConstraintViolation<AdvertisementForm>> constraintViolations = validator.validate(advForm);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }
}
