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
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ReportValidatorTests {

    public static LocalDate creationDate;
    public static Person sender;
    public static Person receiver;

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @BeforeAll
    static void instantiateStatusDateAndTennants() {
        creationDate = LocalDate.now();

        sender = new Person();
        sender.setDni("12345678A");
        sender.setEmail("email@mail.com");
        sender.setFirstName("Antonio");
        sender.setLastName("Rosado");
        sender.setPassword("/4A]m^ub~4e$KFAY");
        sender.setPhoneNumber("123456789");
        sender.setUsername("anton");
        
        receiver = new Person();
        receiver.setDni("12345678B");
        receiver.setEmail("email@email.com");
        receiver.setFirstName("Juan");
        receiver.setLastName("Camacho");
        receiver.setPassword("/4A]m^ub~4e$KFAY");
        receiver.setPhoneNumber("123456788");
        receiver.setUsername("juanca");
    }

    @Test
    void shouldNotValidateWhenReasonEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Report report = new Report();
        report.setCreationDate(creationDate);
        report.setReason("");
        report.setReceiver(receiver);
        report.setSender(sender);

        Validator validator = createValidator();
        Set<ConstraintViolation<Report>> constraintViolations = validator.validate(report);
        
        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Report> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("reason");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenCreationDateNull() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        Report report = new Report();
        report.setCreationDate(null);
        report.setReason("test");
        report.setReceiver(receiver);
        report.setSender(sender);

        Validator validator = createValidator();
        Set<ConstraintViolation<Report>> constraintViolations = validator.validate(report);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Report> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }
    
    @Test
    void shouldNotValidateWhenCreationDateIsInFuture() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        Report report = new Report();
        report.setCreationDate(LocalDate.of(2050, 1, 1));
        report.setReason("test");
        report.setReceiver(receiver);
        report.setSender(sender);

        Validator validator = createValidator();
        Set<ConstraintViolation<Report>> constraintViolations = validator.validate(report);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Report> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must be a date in the past or in the present");
    }

    @Test
    void shouldNotValidateWhenSenderNull() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
    	Report report = new Report();
        report.setCreationDate(creationDate);
        report.setReason("test");
        report.setReceiver(receiver);
        report.setSender(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<Report>> constraintViolations = validator.validate(report);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Report> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("sender");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }
    
    @Test
    void shouldNotValidateWhenReceiverNull() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
    	Report report = new Report();
        report.setCreationDate(creationDate);
        report.setReason("test");
        report.setReceiver(null);
        report.setSender(sender);

        Validator validator = createValidator();
        Set<ConstraintViolation<Report>> constraintViolations = validator.validate(report);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Report> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("receiver");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @CsvSource({
        "sample reason"
    })
    void shouldValidate(String reason) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Report report = new Report();
        report.setCreationDate(creationDate);
        report.setReason(reason);
        report.setReceiver(receiver);
        report.setSender(sender);

        Validator validator = createValidator();
        Set<ConstraintViolation<Report>> constraintViolations = validator.validate(report);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }
	
}
