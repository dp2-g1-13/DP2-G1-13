
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.util.TestUtils;
import org.springframework.samples.flatbook.web.validators.PasswordValidator;

public class PasswordValidatorTests {

	private static final String			PASSWORD	= "HOst__Pa77S";
	private static PersonForm			personForm;
	private static PasswordValidator	passwordValidator;


	@BeforeAll
	static void instaciateValidator() {
		PasswordValidatorTests.passwordValidator = new PasswordValidator();
	}

	@BeforeEach
	void instanciatePersonForm() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		PasswordValidatorTests.personForm = new PersonForm();
		PasswordValidatorTests.personForm.setPassword(PasswordValidatorTests.PASSWORD);
		PasswordValidatorTests.personForm.setConfirmPassword(PasswordValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		PasswordValidatorTests.personForm.setPassword("");

		TestUtils.multipleAssert(PasswordValidatorTests.passwordValidator, PasswordValidatorTests.personForm, "password->required");
	}

	@ParameterizedTest
	@CsvSource({
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		PasswordValidatorTests.personForm.setPassword(password);

		TestUtils.multipleAssert(PasswordValidatorTests.passwordValidator, PasswordValidatorTests.personForm, "password->must have 2 uppers, 2 lowers, 2 simbols, 2 numbers and 8 characters min");
	}

	void shouldNotValidateWhenPasswordNotMatchWithConfirmPassword() {

		PasswordValidatorTests.personForm.setConfirmPassword("anystring");

		TestUtils.multipleAssert(PasswordValidatorTests.passwordValidator, PasswordValidatorTests.personForm, "confirmPassword->password doesnt match");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(PasswordValidatorTests.passwordValidator, PasswordValidatorTests.personForm, (String[]) null);
	}
}
