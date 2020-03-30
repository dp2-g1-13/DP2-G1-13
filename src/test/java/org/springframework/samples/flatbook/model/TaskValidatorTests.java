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
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class TaskValidatorTests {

	public static TaskStatus status;
    public static LocalDate creationDate;
    public static Tenant creator;
    public static Tenant asignee;

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @BeforeAll
    static void instantiateStatusDateAndTenants() {
        status = TaskStatus.TODO;

        creationDate = LocalDate.now();

        creator = new Tenant();
        creator.setDni("12345678A");
        creator.setEmail("email@mail.com");
        creator.setFirstName("Antonio");
        creator.setLastName("Rosado");
        creator.setPassword("/4A]m^ub~4e$KFAY");
        creator.setPhoneNumber("123456789");
        creator.setUsername("anton");

        asignee = new Tenant();
        asignee.setDni("12345678B");
        asignee.setEmail("email@email.com");
        asignee.setFirstName("Juan");
        asignee.setLastName("Camacho");
        asignee.setPassword("/4A]m^ub~4e$KFAY");
        asignee.setPhoneNumber("123456788");
        asignee.setUsername("juanca");
    }

    @Test
    void shouldNotValidateWhenTitleEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Task task = new Task();
        task.setTitle("");
        task.setAsignee(asignee);
        task.setCreationDate(creationDate);
        task.setCreator(creator);
        task.setDescription("test");
        task.setStatus(status);

        Validator validator = createValidator();
        Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Task> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("title");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenDescriptionEmpty() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        Task task = new Task();
        task.setTitle("sample");
        task.setAsignee(asignee);
        task.setCreationDate(creationDate);
        task.setCreator(creator);
        task.setDescription("");
        task.setStatus(status);

        Validator validator = createValidator();
        Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Task> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("description");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldNotValidateWhenStatusNull() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        Task task = new Task();
        task.setTitle("sample");
        task.setAsignee(asignee);
        task.setCreationDate(creationDate);
        task.setCreator(creator);
        task.setDescription("test");
        task.setStatus(null);

        Validator validator = createValidator();
        Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Task> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("status");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenCreationDateNull() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        Task task = new Task();
        task.setTitle("sample");
        task.setAsignee(asignee);
        task.setCreationDate(null);
        task.setCreator(creator);
        task.setDescription("test");
        task.setStatus(status);

        Validator validator = createValidator();
        Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Task> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldNotValidateWhenCreationDateIsInFuture() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        Task task = new Task();
        task.setTitle("sample");
        task.setAsignee(asignee);
        task.setCreationDate(LocalDate.of(2050, 1, 1));
        task.setCreator(creator);
        task.setDescription("test");
        task.setStatus(status);

        Validator validator = createValidator();
        Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Task> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creationDate");
        assertThat(violation.getMessage()).isEqualTo("must be a date in the past or in the present");
    }

    @Test
    void shouldNotValidateWhenCreatorNull() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        Task task = new Task();
        task.setTitle("sample");
        task.setAsignee(asignee);
        task.setCreationDate(creationDate);
        task.setCreator(null);
        task.setDescription("test");
        task.setStatus(status);

        Validator validator = createValidator();
        Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Task> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()) .isEqualTo("creator");
        assertThat(violation.getMessage()).isEqualTo("must not be null");
    }

    @ParameterizedTest
    @CsvSource({
        "sample task, sample description"
    })
    void shouldValidate(String title, String description) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setCreationDate(creationDate);
        task.setCreator(creator);
        task.setAsignee(asignee);

        Validator validator = createValidator();
        Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
