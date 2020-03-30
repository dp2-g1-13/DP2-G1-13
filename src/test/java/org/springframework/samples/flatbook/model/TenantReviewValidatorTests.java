package org.springframework.samples.flatbook.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class TenantReviewValidatorTests {

    public static LocalDate creationDate;
    public static Person creator;

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @BeforeAll
    static void instantiateStatusDateAndTenants() {
        creationDate = LocalDate.now();

        creator = new Person();
        creator.setDni("12345678A");
        creator.setEmail("email@mail.com");
        creator.setFirstName("Antonio");
        creator.setLastName("Rosado");
        creator.setPassword("/4A]m^ub~4e$KFAY");
        creator.setPhoneNumber("123456789");
        creator.setUsername("anton");
    }

    @Test
    void shouldNotValidateWhenRateNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        TenantReview review = new TenantReview();
        review.setCreationDate(creationDate);
        review.setCreator(creator);
        review.setDescription("test");
        review.setRate(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<TenantReview>> constraintViolations = validator.validate(review);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<TenantReview> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("rate");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenRateNegative() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        TenantReview review = new TenantReview();
        review.setCreationDate(creationDate);
        review.setCreator(creator);
        review.setDescription("test");
        review.setRate(-1);

        Validator validator = createValidator();
        Set<ConstraintViolation<TenantReview>> constraintViolations = validator.validate(review);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<TenantReview> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("rate");
        assertThat(violation.getMessage()).isEqualTo("must be greater than or equal to 0");
    }

    @Test
    void shouldNotValidateWhenRateHigherThan5() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        TenantReview review = new TenantReview();
        review.setCreationDate(creationDate);
        review.setCreator(creator);
        review.setDescription("test");
        review.setRate(6);

        Validator validator = createValidator();
        Set<ConstraintViolation<TenantReview>> constraintViolations = validator.validate(review);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<TenantReview> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("rate");
        assertThat(violation.getMessage()).isEqualTo("must be less than or equal to 5");
    }

    @Test
    void shouldNotValidateWhenCreationDateNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        TenantReview review = new TenantReview();
        review.setCreationDate(null);
        review.setCreator(creator);
        review.setDescription("test");
        review.setRate(0);

        Validator validator = createValidator();
        Set<ConstraintViolation<TenantReview>> constraintViolations = validator.validate(review);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<TenantReview> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenCreationDateIsFuture() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        TenantReview review = new TenantReview();
        review.setCreationDate(LocalDate.of(2050, 1, 1));
        review.setCreator(creator);
        review.setDescription("test");
        review.setRate(0);

        Validator validator = createValidator();
        Set<ConstraintViolation<TenantReview>> constraintViolations = validator.validate(review);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<TenantReview> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must be a date in the past or in the present");
    }

    @Test
    void shouldNotValidateWhenCreatorNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        TenantReview review = new TenantReview();
        review.setCreationDate(creationDate);
        review.setCreator(null);
        review.setDescription("test");
        review.setRate(0);

        Validator validator = createValidator();
        Set<ConstraintViolation<TenantReview>> constraintViolations = validator.validate(review);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<TenantReview> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creator");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5})
    void shouldValidate(int rate) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        TenantReview review = new TenantReview();
        review.setCreationDate(creationDate);
        review.setCreator(creator);
        review.setDescription("test");
        review.setRate(rate);

        Validator validator = createValidator();
        Set<ConstraintViolation<TenantReview>> constraintViolations = validator.validate(review);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
