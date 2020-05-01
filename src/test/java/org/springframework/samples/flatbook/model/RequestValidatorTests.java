package org.springframework.samples.flatbook.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    void shouldNotValidateWhenDescriptionNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Request request = new Request();
        request.setDescription(null);
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Request> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("description");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenDescriptionEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Request request = new Request();
        request.setDescription("");
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Request> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("description");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenStatusNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Request request = new Request();
        request.setDescription("Sample description");
        request.setStatus(null);
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Request> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("status");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenCreationDateNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Request request = new Request();
        request.setDescription("Sample description");
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(null);
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Request> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenCreationDateIsFuture() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Request request = new Request();
        request.setDescription("Sample description");
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.MAX);
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Request> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must be a date in the past or in the present");
    }

    @Test
    void shouldNotValidateWhenStartDateNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Request request = new Request();
        request.setDescription("Sample description");
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(null);
        request.setFinishDate(LocalDate.MAX);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Request> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("startDate");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenFinishDateNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Request request = new Request();
        request.setDescription("Sample description");
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Request> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("finishDate");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "Sample description"})
    void shouldValidate(String description) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Request request = new Request();
        request.setDescription(description);
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        request.setStartDate(LocalDate.MAX);
        request.setFinishDate(LocalDate.MAX);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
