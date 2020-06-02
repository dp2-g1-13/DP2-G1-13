package org.springframework.samples.flatbook.unit.model.dtos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.dtos.RequestForm;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.web.validators.RequestFormValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

class RequestFormValidatorTests {

    @Test
    void shouldNotValidateWhenDescriptionNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        RequestForm requestForm = new RequestForm();
        requestForm.setDescription(null);
        requestForm.setStartDate(LocalDate.of(10000, 1, 1));
        requestForm.setFinishDate(LocalDate.of(10000, 12, 1));


        Validator validator = new RequestFormValidator(AuthoritiesType.TENANT);
        Errors errors = new BeanPropertyBindingResult(requestForm, requestForm.getClass().toString());
        validator.validate(requestForm, errors);

        assertThat(errors.getAllErrors().size()).isEqualTo(1);
        FieldError violation = errors.getFieldErrors().iterator().next();
        assertThat(violation.getField()).isEqualTo("description");
        assertThat(violation.getDefaultMessage()).isEqualTo("Description must not be null nor blank");
    }

    @Test
    void shouldNotValidateWhenDescriptionEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        RequestForm requestForm = new RequestForm();
        requestForm.setDescription("");
        requestForm.setStartDate(LocalDate.of(10000, 1, 1));
        requestForm.setFinishDate(LocalDate.of(10000, 12, 1));


        Validator validator = new RequestFormValidator(AuthoritiesType.TENANT);
        Errors errors = new BeanPropertyBindingResult(requestForm, requestForm.getClass().toString());
        validator.validate(requestForm, errors);

        assertThat(errors.getAllErrors().size()).isEqualTo(1);
        FieldError violation = errors.getFieldErrors().iterator().next();
        assertThat(violation.getField()).isEqualTo("description");
        assertThat(violation.getDefaultMessage()).isEqualTo("Description must not be null nor blank");
    }

    @Test
    void shouldNotValidateWhenStartDateNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        RequestForm requestForm = new RequestForm();
        requestForm.setDescription("Sample description");
        requestForm.setStartDate(null);
        requestForm.setFinishDate(LocalDate.of(10000, 12, 1));


        Validator validator = new RequestFormValidator(AuthoritiesType.TENANT);
        Errors errors = new BeanPropertyBindingResult(requestForm, requestForm.getClass().toString());
        validator.validate(requestForm, errors);

        assertThat(errors.getAllErrors().size()).isEqualTo(1);
        FieldError violation = errors.getFieldErrors().iterator().next();
        assertThat(violation.getField()).isEqualTo("startDate");
        assertThat(violation.getDefaultMessage()).isEqualTo("Start date must not be null");
    }

    @Test
    void shouldNotValidateWhenFinishDateNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        RequestForm requestForm = new RequestForm();
        requestForm.setDescription("Sample description");
        requestForm.setStartDate(LocalDate.of(10000, 1, 1));
        requestForm.setFinishDate(null);


        Validator validator = new RequestFormValidator(AuthoritiesType.TENANT);
        Errors errors = new BeanPropertyBindingResult(requestForm, requestForm.getClass().toString());
        validator.validate(requestForm, errors);

        assertThat(errors.getAllErrors().size()).isEqualTo(1);
        FieldError violation = errors.getFieldErrors().iterator().next();
        assertThat(violation.getField()).isEqualTo("finishDate");
        assertThat(violation.getDefaultMessage()).isEqualTo("Finish date must not be null");
    }

    @Test
    void shouldNotValidateWhenStartDateBeforeToday() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        RequestForm requestForm = new RequestForm();
        requestForm.setDescription("Sample description");
        requestForm.setStartDate(LocalDate.MIN);
        requestForm.setFinishDate(LocalDate.of(10000, 12, 1));


        Validator validator = new RequestFormValidator(AuthoritiesType.TENANT);
        Errors errors = new BeanPropertyBindingResult(requestForm, requestForm.getClass().toString());
        validator.validate(requestForm, errors);

        assertThat(errors.getAllErrors().size()).isEqualTo(1);
        FieldError violation = errors.getFieldErrors().iterator().next();
        assertThat(violation.getField()).isEqualTo("startDate");
        assertThat(violation.getDefaultMessage()).isEqualTo("Start date must be after today's date");
    }

    @Test
    void shouldNotValidateWhenFinishDateBeforeToday() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        RequestForm requestForm = new RequestForm();
        requestForm.setDescription("Sample description");
        requestForm.setStartDate(LocalDate.of(-10000, 1, 1));
        requestForm.setFinishDate(LocalDate.of(-10000, 12, 1));


        Validator validator = new RequestFormValidator(AuthoritiesType.TENANT);
        Errors errors = new BeanPropertyBindingResult(requestForm, requestForm.getClass().toString());
        validator.validate(requestForm, errors);

        assertThat(errors.getAllErrors().size()).isEqualTo(2);
        assertTrue("Expecting propertyPath to be equal to 'description' but was not.", errors.getFieldErrors().stream().anyMatch(x -> x.getField().equals("finishDate")));
        assertTrue("Expecting mesaage to be equal to 'Finish date must be after today's date' but was not.", errors.getFieldErrors().stream().anyMatch(x -> Objects.equals(x.getDefaultMessage(), "Finish date must be after today's date")));
    }

    @ParameterizedTest
    @ValueSource(ints = {12, 1})
    void shouldNotValidateWhenFinishDateIsAfterStartDate() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        RequestForm requestForm = new RequestForm();
        requestForm.setDescription("Sample description");
        requestForm.setStartDate(LocalDate.of(10000, 12, 1));
        requestForm.setFinishDate(LocalDate.of(10000, 1, 1));


        Validator validator = new RequestFormValidator(AuthoritiesType.TENANT);
        Errors errors = new BeanPropertyBindingResult(requestForm, requestForm.getClass().toString());
        validator.validate(requestForm, errors);

        assertThat(errors.getAllErrors().size()).isEqualTo(2);

        Iterator<FieldError> iterator = errors.getFieldErrors().iterator();
        FieldError violation = iterator.next();
        assertThat(violation.getField()).isEqualTo("startDate");
        assertThat(violation.getDefaultMessage()).isEqualTo("Start date must be before finish date");

        violation = iterator.next();
        assertThat(violation.getField()).isEqualTo("finishDate");
        assertThat(violation.getDefaultMessage()).isEqualTo("Finish date must be after start date");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "Sample description"})
    void shouldValidate(String description) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        RequestForm requestForm = new RequestForm();
        requestForm.setDescription(description);
        requestForm.setStartDate(LocalDate.of(10000, 1, 1));
        requestForm.setFinishDate(LocalDate.of(10000, 12, 1));


        Validator validator = new RequestFormValidator(AuthoritiesType.TENANT);
        Errors errors = new BeanPropertyBindingResult(requestForm, requestForm.getClass().toString());
        validator.validate(requestForm, errors);

        assertThat(errors.getAllErrors().size()).isEqualTo(0);
    }
}
