package org.springframework.samples.flatbook.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class AddressValidatorTests {

    private Address address;

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @BeforeEach
    void setup() {
        address = new Address();
        address.setLocation("Av. de la República Argentina 72");
        address.setCity("Sevilla");
        address.setCountry("Spain");
        address.setPostalCode("41011");
    }

    @Test
    void shouldNotValidateWhenAddressEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        address.setLocation("");

        Validator validator = createValidator();
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Address> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("location");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenCityEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        address.setCity("");

        Validator validator = createValidator();
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Address> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("city");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenPostalCodeNull() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        address.setPostalCode(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Address> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("postalCode");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12", "123456789a"})
    void shouldNotValidateWhenPostalCodeNotFulfillsWithSize(String postalCode) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        address.setPostalCode(postalCode);

        Validator validator = createValidator();
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Address> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("postalCode");
        assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 9");
    }

    @Test
    void shouldNotValidateWhenCountryEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        address.setCountry("");

        Validator validator = createValidator();
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);

        assertThat(constraintViolations.size()).isEqualTo(2);
        assertTrue("Expecting propertyPath to be equal to 'country' but was not.", constraintViolations.stream().allMatch(x -> x.getPropertyPath().toString().equals("country")));
        assertTrue("Expecting one of the constraint violation messages to be equal to 'must not be blank', but was not.", constraintViolations.stream().anyMatch(x -> x.getMessage().equals("must not be blank")));
    }

    @Test
    void shouldNotValidateWhenCountryContainsNumbers() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        address.setCountry("4n1t3d st4t3s");

        Validator validator = createValidator();
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Address> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("country");
        assertThat(violation.getMessage()).isEqualTo("Numbers are not accepted here");
    }

    @ParameterizedTest
    @CsvSource({
        "A Street, Norfolk, 23324, United States",
        "Calle Francos, Sevilla, 41004, Sevilla"
    })
    void shouldValidate(String address, String city, String postalCode, String country) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        this.address.setLocation(address);
        this.address.setCity(city);
        this.address.setPostalCode(postalCode);
        this.address.setCountry(country);

        Validator validator = createValidator();
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(this.address);

        assertThat(constraintViolations).isEmpty();
    }
}
